package controllers;

import bodymodels.Transaction;
import bodymodels.Transfer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDaoImplementation;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidBodyException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedClientException;
import io.javalin.http.Context;
import models.Account;
import services.AccountService;

import java.sql.SQLException;

public class AccountController {
	private static final AccountService accountService = new AccountService (new AccountDaoImplementation ());
	
	public static void getAccounts (Context context) {
		try {
			context.status (200);
			context.contentType ("application/json");
			
			context.result (new ObjectMapper ().writeValueAsString (accountService.getAccounts (Integer.parseInt (context.pathParam ("clientId")))));
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
		
		catch (JsonProcessingException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! JSON processing error");
		}
	}
	
	public static void getAccount (Context context) {
		try {
			context.status (200);
			context.contentType ("application/json");
			
			context.result (new ObjectMapper ().writeValueAsString (accountService.getAccount (Integer.parseInt (context.pathParam ("clientId")), Integer.parseInt (context.pathParam ("accountId")))));
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
		
		catch (UnauthorizedClientException exception) {
			context.status (401);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (JsonProcessingException exception) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! JSON processing error");
		}
	}
	
	public static void createAccount (Context context) {
		try {
			String accountName = context.bodyAsClass (Account.class).getName ();
			
			if (accountName.equals ("")) {
				throw new InvalidBodyException ("Error! Invalid body!");
			}
			
			accountService.createAccount (Integer.parseInt (context.pathParam ("clientId")), accountName);
			
			context.status (201);
			context.contentType ("text/plain");
			
			context.result ("Created account with name: " + accountName);
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
	}
	
	public static void updateAccountName (Context context) {
		try {
			int accountId = Integer.parseInt (context.pathParam ("accountId"));
			String accountName = context.bodyAsClass (Account.class).getName ();
			
			if (accountName.equals ("")) {
				throw new InvalidBodyException ("Error! Invalid body!");
			}
			
			accountService.updateAccountName (Integer.parseInt (context.pathParam ("clientId")), accountId, accountName);
			
			context.status (200);
			context.contentType ("text/plain");
			
			context.result ("Updated account with account id: " + accountId + "'s name to: " + accountName);
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
		
		catch (UnauthorizedClientException exception) {
			context.status (401);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
	}
	
	public static void updateAccountBalance (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			int accountId = Integer.parseInt (context.pathParam ("accountId"));
			
			Transaction transaction = context.bodyAsClass (Transaction.class);
			
			float deposit = transaction.getDeposit ();
			float withdraw = transaction.getWithdraw ();
			
			if (deposit != -1F) {
				accountService.depositToAccount (clientId, accountId, deposit);
				
				context.status (200);
				context.contentType ("text/plain");
				
				context.result ("Deposited $" + deposit + " to account with account id: " + accountId);
			}
			
			else if (withdraw != -1F) {
				accountService.withdrawFromAccount (clientId, accountId, withdraw);
				
				context.status (200);
				context.contentType ("text/plain");
				
				context.result ("Withdrew $" + withdraw + " from account with account id: " + accountId);
			}
			
			else {
				throw new InvalidBodyException ("Error! Invalid body");
			}
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
		
		catch (UnauthorizedClientException exception) {
			context.status (401);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (InsufficientBalanceException exception) {
			context.status (422);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (InvalidBodyException exception) {
			context.status (400);
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
	
	public static void transferBetweenAccounts (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			int fromAccountId = Integer.parseInt (context.pathParam ("fromAccountId"));
			int toAccountId = Integer.parseInt (context.pathParam ("toAccountId"));
			
			Transfer transfer = context.bodyAsClass (Transfer.class);
			
			float amount = transfer.getAmount ();
			
			if (amount != -1F) {
				accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, amount);
				
				context.status (200);
				context.contentType ("text/plain");
				
				context.result ("Transferred $" + amount + " from account with account id: " + fromAccountId + " to account with account id: " + toAccountId);
			}
			
			else {
				throw new InvalidBodyException ("Error! Invalid body");
			}
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
		
		catch (UnauthorizedClientException exception) {
			context.status (401);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (InsufficientBalanceException exception) {
			context.status (422);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		catch (InvalidBodyException exception) {
			context.status (400);
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
}
