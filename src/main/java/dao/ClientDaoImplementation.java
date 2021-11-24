package dao;

import exceptions.NotFoundException;
import models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoImplementation implements ClientDao {
	private final String url;
	private final String username;
	private final String password;
	
	public ClientDaoImplementation () {
		this.url = DatabaseCredentials.url;
		this.username = DatabaseCredentials.username;
		this.password = DatabaseCredentials.password;
	}
	
	public ClientDaoImplementation (String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public List <Client> getClients () throws SQLException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("SELECT * FROM clients;");
			
			ResultSet resultSet = statement.executeQuery ();
			
			List <Client> clients = new ArrayList <> ();
			
			while (resultSet.next ()) {
				clients.add (new Client (resultSet.getInt (1), resultSet.getString (2)));
			}
			
			return clients;
		}
	}
	
	@Override
	public Client getClient (int clientId) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("SELECT * FROM clients WHERE id = ?;");
			
			statement.setInt (1, clientId);
			
			ResultSet resultSet = statement.executeQuery ();
			
			Client client = null;
			
			while (resultSet.next ()) {
				client = new Client (resultSet.getInt (1), resultSet.getString (2));
			}
			
			if (client == null) {
				throw new NotFoundException ("Error! No client found with client id: " + clientId);
			}
			
			return client;
		}
	}
	
	@Override
	public void createClient (String clientName) throws SQLException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("INSERT INTO clients (name) VALUES (?)");
			
			statement.setString (1, clientName);
			
			statement.executeUpdate ();
		}
	}
	
	@Override
	public void updateClientName (int clientId, String clientName) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("UPDATE clients SET name = ? WHERE id = ?;");
			
			statement.setString (1, clientName);
			statement.setInt (2, clientId);
			
			int updatedRows = statement.executeUpdate ();
			
			if (updatedRows == 0) {
				throw new NotFoundException ("Error! No client found with client id: " + clientId);
			}
		}
	}
	
	@Override
	public void deleteClient (int clientId) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("DELETE FROM clients WHERE id = ?;");
			
			statement.setInt (1, clientId);
			
			int updatedRows = statement.executeUpdate ();
			
			if (updatedRows == 0) {
				throw new NotFoundException ("Error! No client found with client id: " + clientId);
			}
		}
	}
}
