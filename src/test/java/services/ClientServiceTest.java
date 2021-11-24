package services;

import dao.ClientDao;
import dao.ClientDaoImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import exceptions.NotFoundException;
import models.Client;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {
	private final ClientDao clientDao = Mockito.mock (ClientDaoImplementation.class);
	private final ClientService clientService = new ClientService (clientDao);
	
	@Test
	void getClients () throws SQLException, NotFoundException {
		List <Client> clients = new ArrayList <> ();
		
		clients.add (new Client (1, "Client 1"));
		clients.add (new Client (2, "Client 2"));
		clients.add (new Client (3, "Client 3"));
		
		Mockito.when (clientDao.getClients ()).thenReturn (clients);
		
		assertEquals (clients, clientService.getClients ());
	}
	
	@Test
	void getClientsWhenNoClients () throws SQLException {
		Mockito.when (clientDao.getClients ()).thenReturn (new ArrayList <> ());
		
		assertThrows (NotFoundException.class, clientService::getClients);
	}
	
	@Test
	void getClient () throws SQLException, NotFoundException {
		Client client = new Client (1, "Client 1");
		
		Mockito.when (clientDao.getClient (client.getId ())).thenReturn (client);
		
		assertEquals (client, clientService.getClient (client.getId ()));
	}
	
	@Test
	void createClient () throws SQLException {
		String clientName = "Client 4";
		
		clientService.createClient (clientName);
		
		Mockito.verify (clientDao).createClient (clientName); 
	}
	
	@Test
	void updateClientName () throws SQLException, NotFoundException {
		int clientId = 1;
		String clientName = "New Client 1";
		
		clientService.updateClientName (clientId, clientName);
		
		Mockito.verify (clientDao).updateClientName (clientId, clientName);
	}
	
	@Test
	void deleteClient () throws SQLException, NotFoundException {
		int clientId = 1;
		
		clientService.deleteClient (clientId);
		
		Mockito.verify (clientDao).deleteClient (clientId);
	}
}