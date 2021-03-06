package dao;

import exceptions.NotFoundException;
import models.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImplementation implements AccountDao {
	private final ClientDao clientDao;
	
	private final String url;
	private final String username;
	private final String password;
	
	public AccountDaoImplementation () {
		this.clientDao = new ClientDaoImplementation ();
		this.url = DatabaseCredentials.url;
		this.username = DatabaseCredentials.username;
		this.password = DatabaseCredentials.password;
	}
	
	public AccountDaoImplementation (ClientDao clientDao, String url, String username, String password) {
		this.clientDao = clientDao;
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public List <Account> getAccounts () throws SQLException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("SELECT * FROM accounts;");
			
			ResultSet resultSet = statement.executeQuery ();
			
			List <Account> accounts = new ArrayList <> ();
			
			while (resultSet.next ()) {
				accounts.add (new Account (resultSet.getInt (1), resultSet.getInt (2), resultSet.getFloat (3), resultSet.getString (4)));
			}
			
			return accounts;
		}
	}
	
	@Override
	public Account getAccount (int accountId) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("SELECT * FROM accounts WHERE id = ?;");
			
			statement.setInt (1, accountId);
			
			ResultSet resultSet = statement.executeQuery ();
			
			Account account = null;
			
			while (resultSet.next ()) {
				account = new Account (resultSet.getInt (1), resultSet.getInt (2), resultSet.getFloat (3), resultSet.getString (4));
			}
			
			if (account == null) {
				throw new NotFoundException ("Error! No account found with account id: " + accountId);
			}
			
			return account;
		}
	}
	
	@Override
	public int getAccountClientId (int accountId) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("SELECT * FROM accounts WHERE id = ?;");
			
			statement.setInt (1, accountId);
			
			ResultSet resultSet = statement.executeQuery ();
			
			Integer clientId = null;
			
			while (resultSet.next ()) {
				clientId = resultSet.getInt (2);
			}
			
			if (clientId == null) {
				throw new NotFoundException ("Error! No account found with account id: " + accountId);
			}
			
			return clientId;
		}
	}
	
	@Override
	public float getAccountBalance (int accountId) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("SELECT * FROM accounts WHERE id = ?;");
			
			statement.setInt (1, accountId);
			
			ResultSet resultSet = statement.executeQuery ();
			
			Float balance = null;
			
			while (resultSet.next ()) {
				balance = resultSet.getFloat (3);
			}
			
			if (balance == null) {
				throw new NotFoundException ("Error! No account found with account id: " + accountId);
			}
			
			return balance;
		}
	}
	
	@Override
	public void createAccount (int clientId, String accountName) throws SQLException, NotFoundException {
		//throws NotFoundException if client doesn't exist
		clientDao.getClient (clientId);
		
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("INSERT INTO accounts (client_id_fk, name) VALUES (?, ?)");
			
			statement.setInt (1, clientId);
			statement.setString (2, accountName);
			
			statement.executeUpdate ();
		}
	}
	
	@Override
	public void updateAccountName (int accountId, String accountName) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("UPDATE accounts SET name = ? WHERE id = ?;");
			
			statement.setString (1, accountName);
			statement.setInt (2, accountId);
			
			//if no rows were updated
			if (statement.executeUpdate () == 0) {
				throw new NotFoundException ("Error! No account found with account id: " + accountId);
			}
		}
	}
	
	@Override
	public void updateAccountBalance (int accountId, float balance) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("UPDATE accounts SET balance = ? WHERE id = ?;");
			
			statement.setFloat (1, balance);
			statement.setInt (2, accountId);
			
			//if no rows were updated
			if (statement.executeUpdate () == 0) {
				throw new NotFoundException ("Error! No account found with account id: " + accountId);
			}
		}
	}
	
	@Override
	public void deleteAccount (int accountId) throws SQLException, NotFoundException {
		try (Connection connection = DriverManager.getConnection (url, username, password)) {
			PreparedStatement statement = connection.prepareStatement ("DELETE FROM accounts WHERE id = ?;");
			
			statement.setInt (1, accountId);
			
			//if no rows were updated
			if (statement.executeUpdate () == 0) {
				throw new NotFoundException ("Error! No account found with account id: " + accountId);
			}
		}
	}
}
