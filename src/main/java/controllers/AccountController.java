package controllers;

import bodymodels.Transaction;
import bodymodels.Transfer;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.AccountDaoImplementation;
import exceptions.InvalidBodyException;
import frontcontroller.Dispatcher;
import io.javalin.http.Context;
import models.Account;
import services.AccountService;

import java.text.DecimalFormat;

public class AccountController {
	private static final AccountService accountService = new AccountService (new AccountDaoImplementation ());
	
	public static void getAccounts (Context context) {
		try {
			String minimumBalance = context.queryParam ("amountGreaterThan");
			String maximumBalance = context.queryParam ("amountLessThan");
			
			context.status (200);
			context.contentType ("application/json");
			
			if (minimumBalance != null && maximumBalance != null) {
				context.result (new ObjectMapper ().writeValueAsString (accountService.getAccountsInRange (Integer.parseInt (context.pathParam ("clientId")), Float.parseFloat (minimumBalance), Float.parseFloat (maximumBalance))));
			}
			
			else {
				context.result (new ObjectMapper ().writeValueAsString (accountService.getAccounts (Integer.parseInt (context.pathParam ("clientId")))));
			}
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void getAccount (Context context) {
		try {
			context.status (200);
			context.contentType ("application/json");
			
			context.result (new ObjectMapper ().writeValueAsString (accountService.getAccount (Integer.parseInt (context.pathParam ("clientId")), Integer.parseInt (context.pathParam ("accountId")))));
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void createAccount (Context context) {
		try {
			String accountName = context.bodyAsClass (Account.class).getName ();
			
			if (accountName == null) {
				throw new InvalidBodyException ();
			}
			
			accountService.createAccount (Integer.parseInt (context.pathParam ("clientId")), accountName);
			
			context.status (201);
			context.contentType ("text/plain");
			
			context.result ("Created account with name: " + accountName);
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void updateAccountName (Context context) {
		try {
			int accountId = Integer.parseInt (context.pathParam ("accountId"));
			String accountName = context.bodyAsClass (Account.class).getName ();
			
			if (accountName == null) {
				throw new InvalidBodyException ();
			}
			
			accountService.updateAccountName (Integer.parseInt (context.pathParam ("clientId")), accountId, accountName);
			
			context.status (200);
			context.contentType ("text/plain");
			
			context.result ("Updated account with account id: " + accountId + "'s name to: " + accountName);
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void updateAccountBalance (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			int accountId = Integer.parseInt (context.pathParam ("accountId"));
			
			Transaction transaction = context.bodyAsClass (Transaction.class);
			
			Float deposit = transaction.getDeposit ();
			Float withdraw = transaction.getWithdraw ();
			
			if (deposit != null && deposit > 0) {
				accountService.depositToAccount (clientId, accountId, deposit);
				
				context.status (200);
				context.contentType ("text/plain");
				
				context.result ("Deposited $" + new DecimalFormat ("0.00").format (deposit) + " to account with account id: " + accountId);
			}
			
			else if (withdraw != null && withdraw > 0) {
				accountService.withdrawFromAccount (clientId, accountId, withdraw);
				
				context.status (200);
				context.contentType ("text/plain");
				
				context.result ("Withdrew $" + new DecimalFormat ("0.00").format (withdraw) + " from account with account id: " + accountId);
			}
			
			else {
				throw new InvalidBodyException ();
			}
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void transferBetweenAccounts (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			int fromAccountId = Integer.parseInt (context.pathParam ("fromAccountId"));
			int toAccountId = Integer.parseInt (context.pathParam ("toAccountId"));
			
			Transfer transfer = context.bodyAsClass (Transfer.class);
			
			Float amount = transfer.getAmount ();
			
			if (amount != null && amount > 0F) {
				accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, amount);
				
				context.status (200);
				context.contentType ("text/plain");
				
				context.result ("Transferred $" + new DecimalFormat ("0.00").format (amount) + " from account with account id: " + fromAccountId + " to account with account id: " + toAccountId);
			}
			
			else {
				throw new InvalidBodyException ();
			}
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
	
	public static void deleteAccount (Context context) {
		try {
			int clientId = Integer.parseInt (context.pathParam ("clientId"));
			int accountId = Integer.parseInt (context.pathParam ("accountId"));
			
			accountService.deleteAccount (clientId, accountId);
			
			context.status (205);
			context.contentType ("text/plain");
			
			context.result ("Deleted account with account id: " + accountId);
		}
		
		catch (Exception exception) {
			Dispatcher.handleException (exception, context);
		}
	}
}
