package services;

import dao.ClientDao;
import exceptions.NotFoundException;
import models.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
	private ClientDao clientDao;
	
	public ClientService (ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	
	public List <Client> getClients () throws SQLException {
		return clientDao.getClients ();
	}
	
	public Client getClient (Integer clientId) throws SQLException, NotFoundException {
		return clientDao.getClient (clientId);
	}
	
	public void createClient (String clientName) throws SQLException {
		clientDao.createClient (clientName);
	}
	
	public void updateClientName (Integer clientId, String clientName) throws SQLException, NotFoundException {
		clientDao.updateClientName (clientId, clientName);
	}
	
	public void deleteClient (Integer clientId) throws SQLException, NotFoundException {
		clientDao.deleteClient (clientId);
	}
}
