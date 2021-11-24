package controllers;

import bodymodels.Transaction;
import bodymodels.Transfer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidBodyException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedClientException;
import io.javalin.http.Context;
import models.Account;
import services.AccountService;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class AccountController {
	private static final AccountService accountService = new AccountService ();
	
	public static void getAccounts (Context context) throws SQLException, NotFoundException, JsonProcessingException {
		String minimumBalance = context.queryParam ("amountGreaterThan");
		String maximumBalance = context.queryParam ("amountLessThan");
		
		context.status (200);
		
		if (minimumBalance != null && maximumBalance != null) {
			context.json (new ObjectMapper ().writeValueAsString (accountService.getAccountsInRange (Integer.parseInt (context.pathParam ("clientId")), Float.parseFloat (minimumBalance), Float.parseFloat (maximumBalance))));
		}
		
		else {
			context.json (new ObjectMapper ().writeValueAsString (accountService.getAccounts (Integer.parseInt (context.pathParam ("clientId")))));
		}
	}
	
	public static void getAccount (Context context) throws SQLException, UnauthorizedClientException, NotFoundException, JsonProcessingException {
		context.status (200);
		
		context.json (new ObjectMapper ().writeValueAsString (accountService.getAccount (Integer.parseInt (context.pathParam ("clientId")), Integer.parseInt (context.pathParam ("accountId")))));
	}
	
	public static void createAccount (Context context) throws InvalidBodyException, SQLException, NotFoundException {
		String accountName = context.bodyAsClass (Account.class).getName ();
		
		if (accountName == null) {
			throw new InvalidBodyException ();
		}
		
		accountService.createAccount (Integer.parseInt (context.pathParam ("clientId")), accountName);
		
		context.status (201);
		
		context.result ("Created account with name: " + accountName);
	}
	
	public static void updateAccountName (Context context) throws InvalidBodyException, SQLException, UnauthorizedClientException, NotFoundException {
		int accountId = Integer.parseInt (context.pathParam ("accountId"));
		String accountName = context.bodyAsClass (Account.class).getName ();
		
		if (accountName == null) {
			throw new InvalidBodyException ();
		}
		
		accountService.updateAccountName (Integer.parseInt (context.pathParam ("clientId")), accountId, accountName);
		
		context.status (200);
		
		context.result ("Updated account with account id: " + accountId + "'s name to: " + accountName);
	}
	
	public static void updateAccountBalance (Context context) throws SQLException, UnauthorizedClientException, NotFoundException, InsufficientBalanceException, InvalidBodyException {
		int clientId = Integer.parseInt (context.pathParam ("clientId"));
		int accountId = Integer.parseInt (context.pathParam ("accountId"));
		
		Transaction transaction = context.bodyAsClass (Transaction.class);
		
		Float deposit = transaction.getDeposit ();
		Float withdraw = transaction.getWithdraw ();
		
		if (deposit != null && deposit > 0) {
			accountService.depositToAccount (clientId, accountId, deposit);
			
			context.status (200);
			
			context.result ("Deposited $" + new DecimalFormat ("0.00").format (deposit) + " to account with account id: " + accountId);
		}
		
		else if (withdraw != null && withdraw > 0) {
			accountService.withdrawFromAccount (clientId, accountId, withdraw);
			
			context.status (200);
			
			context.result ("Withdrew $" + new DecimalFormat ("0.00").format (withdraw) + " from account with account id: " + accountId);
		}
		
		else {
			throw new InvalidBodyException ();
		}
	}
	
	public static void transferBetweenAccounts (Context context) throws SQLException, UnauthorizedClientException, InsufficientBalanceException, NotFoundException, InvalidBodyException {
		int clientId = Integer.parseInt (context.pathParam ("clientId"));
		int fromAccountId = Integer.parseInt (context.pathParam ("fromAccountId"));
		int toAccountId = Integer.parseInt (context.pathParam ("toAccountId"));
		
		Transfer transfer = context.bodyAsClass (Transfer.class);
		
		Float amount = transfer.getAmount ();
		
		if (amount != null && amount > 0F) {
			accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, amount);
			
			context.status (200);
			
			context.result ("Transferred $" + new DecimalFormat ("0.00").format (amount) + " from account with account id: " + fromAccountId + " to account with account id: " + toAccountId);
		}
		
		else {
			throw new InvalidBodyException ();
		}
	}
	
	public static void deleteAccount (Context context) throws SQLException, UnauthorizedClientException, NotFoundException {
		int clientId = Integer.parseInt (context.pathParam ("clientId"));
		int accountId = Integer.parseInt (context.pathParam ("accountId"));
		
		accountService.deleteAccount (clientId, accountId);
		
		context.status (205);
		
		context.result ("Deleted account with account id: " + accountId);
	}
}
