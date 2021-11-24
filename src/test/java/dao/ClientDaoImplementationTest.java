package dao;

import exceptions.NotFoundException;
import models.Client;
import org.junit.jupiter.api.*;
import utilities.H2Utilities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientDaoImplementationTest {
	private final ClientDao clientDao = new ClientDaoImplementation (DatabaseCredentials.H2Url, DatabaseCredentials.H2Username, DatabaseCredentials.H2Password);
	
	@BeforeEach
	void beforeEach () {
		H2Utilities.createClientsTable ();
		H2Utilities.createAccountsTable ();
	}
	
	@AfterEach
	void afterEach () {
		H2Utilities.dropAccountsTable ();
		H2Utilities.dropClientsTable ();
	}
	
	@Test
	void getClients () throws SQLException {
		List <Client> clients = new ArrayList <> ();
		
		clients.add (new Client (1, "Client 1"));
		clients.add (new Client (2, "Client 2"));
		clients.add (new Client (3, "Client 3"));
		
		clientDao.createClient (clients.get (0).getName ());
		clientDao.createClient (clients.get (1).getName ());
		clientDao.createClient (clients.get (2).getName ());
		
		assertEquals (clients, clientDao.getClients ());
	}
	
	@Test
	void getClient () throws SQLException, NotFoundException {
		Client client = new Client (1, "Client 1");
		
		clientDao.createClient (client.getName ());
		
		assertEquals (client, clientDao.getClient (client.getId ()));
	}
	
	@Test
	void createClient () throws SQLException {
		clientDao.createClient ("Client 1");
		clientDao.createClient ("Client 2");
		clientDao.createClient ("Client 3");
		
		assertEquals (3, clientDao.getClients ().size ());
	}
	
	@Test
	void updateClientName () throws SQLException, NotFoundException {
		int clientId = 1;
		String clientName = "New Client 1";
		
		clientDao.createClient ("Client 1");
		
		clientDao.updateClientName (clientId, clientName);
		
		assertEquals (clientName, clientDao.getClient (clientId).getName ());
	}
	
	@Test
	void deleteClient () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		clientDao.deleteClient (1);
		
		assertEquals (0, clientDao.getClients ().size ());
	}
}