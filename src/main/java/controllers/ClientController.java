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
	
	public static void getClients (Context context) {
		try {
			context.status (200);
			context.contentType ("application/json");
			
			context.result (new ObjectMapper ().writeValueAsString (clientService.getClients ()));
		}
		
		catch (JsonProcessingException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! JSON processing error");
		}
		
		catch (SQLException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! SQL error");
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
		
		catch (NumberFormatException exception) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result ("Error! Invalid client id");
		}
		
		catch (SQLException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! SQL error");
		}
		
		catch (NotFoundException exception) {
			context.status (404);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (JsonProcessingException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! JSON processing error");
		}
	}
	
	public static void createClient (Context context) {
		try {
			String clientName = context.bodyAsClass (Client.class).getName ();
			
			if (clientName.equals ("")) {
				throw new InvalidBodyException ("Error! Invalid body!");
			}
			
			clientService.createClient (clientName);
			
			context.status (201);
			context.contentType ("text/plain");
			
			context.result ("Created client with name: " + clientName);
		}
		
		catch (InvalidBodyException exception) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (SQLException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! SQL error");
		}
		
		//catching specific exception doesn't work for context.bodyAsClass for some reason
		catch (Exception exception) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result ("Error! Invalid body");
		}
	}
	
	public static void updateClientName (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			
			String clientName = context.bodyAsClass (Client.class).getName ();
			
			if (clientName.equals ("")) {
				throw new InvalidBodyException ("Error! Invalid body!");
			}
			
			clientService.updateClientName (clientId, clientName);
			
			context.status (200);
			context.contentType ("text/plain");
			
			context.result ("Updated client with id: " + clientId + "'s name to: " + clientName);
		}
		
		catch (NumberFormatException exception) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result ("Error! Invalid client id");
		}
		
		catch (InvalidBodyException exception) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (SQLException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! SQL error");
		}
		
		catch (NotFoundException exception) {
			context.status (404);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		//catching specific exception doesn't work for context.bodyAsClass for some reason
		catch (Exception exception) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result ("Error! Invalid body");
		}
	}
	
	public static void deleteClient (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			
			clientService.deleteClient (clientId);
			
			context.status (205);
			context.contentType ("text/plain");
			
			context.result ("Deleted client with id: " + clientId);
		}
		
		catch (NumberFormatException exception) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result ("Error! Invalid client id");
		}
		
		catch (SQLException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! SQL error");
		}
		
		catch (NotFoundException exception) {
			context.status (404);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
	}
}
