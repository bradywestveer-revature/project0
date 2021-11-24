package services;

import dao.ClientDao;
import dao.ClientDaoImplementation;
import exceptions.NotFoundException;
import models.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
	private final ClientDao clientDao;
	
	public ClientService () {
		this.clientDao = new ClientDaoImplementation ();
	}
	
	public ClientService (ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	
	public List <Client> getClients () throws SQLException, NotFoundException {
		List <Client> clients = clientDao.getClients ();
		
		if (clients.size () == 0) {
			throw new NotFoundException ("Error! No clients found");
		}
		
		return clients;
	}
	
	public Client getClient (int clientId) throws SQLException, NotFoundException {
		return clientDao.getClient (clientId);
	}
	
	public void createClient (String clientName) throws SQLException {
		clientDao.createClient (clientName);
	}
	
	public void updateClientName (int clientId, String clientName) throws SQLException, NotFoundException {
		clientDao.updateClientName (clientId, clientName);
	}
	
	public void deleteClient (int clientId) throws SQLException, NotFoundException {
		clientDao.deleteClient (clientId);
	}
}
