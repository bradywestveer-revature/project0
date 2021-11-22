package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ClientDaoImplementation;
import exceptions.InvalidBodyException;
import exceptions.NotFoundException;
import io.javalin.http.Context;
import models.Client;
import services.ClientService;

import java.sql.SQLException;

public class ClientController {
	private static final ClientService clientService = new ClientService (new ClientDaoImplementation ());
	
	public static void getClients (Context context) throws SQLException, NotFoundException, JsonProcessingException {
		context.status (200);
		
		context.json (new ObjectMapper ().writeValueAsString (clientService.getClients ()));
	}
	
	public static void getClient (Context context) throws SQLException, NotFoundException, JsonProcessingException {
		int clientId = Integer.parseInt (context.pathParam ("clientId"));
		
		Client client = clientService.getClient (clientId);
		
		context.status (200);
		
		context.json (new ObjectMapper ().writeValueAsString (client));
	}
	
	public static void createClient (Context context) throws InvalidBodyException, SQLException {
		String clientName = context.bodyAsClass (Client.class).getName ();
		
		if (clientName == null) {
			throw new InvalidBodyException ();
		}
		
		clientService.createClient (clientName);
		
		context.status (201);
		
		context.result ("Created client with name: " + clientName);
	}
	
	public static void updateClientName (Context context) throws InvalidBodyException, SQLException, NotFoundException {
		int clientId = Integer.parseInt (context.pathParam ("clientId"));
		
		String clientName = context.bodyAsClass (Client.class).getName ();
		
		if (clientName == null) {
			throw new InvalidBodyException ();
		}
		
		clientService.updateClientName (clientId, clientName);
		
		context.status (200);
		
		context.result ("Updated client with client id: " + clientId + "'s name to: " + clientName);
	}
	
	public static void deleteClient (Context context) throws SQLException, NotFoundException {
		int clientId = Integer.parseInt (context.pathParam ("clientId"));
		
		clientService.deleteClient (clientId);
		
		context.status (205);
		
		context.result ("Deleted client with client id: " + clientId);
	}
}
