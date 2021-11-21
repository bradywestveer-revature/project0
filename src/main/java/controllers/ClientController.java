package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ClientDaoImplementation;
import exceptions.InvalidBodyException;
import frontcontroller.Dispatcher;
import io.javalin.http.Context;
import models.Client;
import services.ClientService;

public class ClientController {
	private static final ClientService clientService = new ClientService (new ClientDaoImplementation ());
	
	public static void getClients (Context context) {
		try {
			context.status (200);
			context.contentType ("application/json");
			
			context.result (new ObjectMapper ().writeValueAsString (clientService.getClients ()));
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void getClient (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			
			Client client = clientService.getClient (clientId);
			
			context.status (200);
			context.contentType ("application/json");
			
			context.result (new ObjectMapper ().writeValueAsString (client));
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void createClient (Context context) {
		try {
			String clientName = context.bodyAsClass (Client.class).getName ();
			
			if (clientName == null) {
				throw new InvalidBodyException ();
			}
			
			clientService.createClient (clientName);
			
			context.status (201);
			context.contentType ("text/plain");
			
			context.result ("Created client with name: " + clientName);
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void updateClientName (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			
			String clientName = context.bodyAsClass (Client.class).getName ();
			
			if (clientName == null) {
				throw new InvalidBodyException ();
			}
			
			clientService.updateClientName (clientId, clientName);
			
			context.status (200);
			context.contentType ("text/plain");
			
			context.result ("Updated client with client id: " + clientId + "'s name to: " + clientName);
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void deleteClient (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			
			clientService.deleteClient (clientId);
			
			context.status (205);
			context.contentType ("text/plain");
			
			context.result ("Deleted client with client id: " + clientId);
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
}
