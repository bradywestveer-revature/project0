package services;

import dao.AccountDao;
import exceptions.InsufficientBalanceException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedClientException;
import models.Account;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
	private final AccountDao accountDao;
	
	public AccountService (AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public List <Account> getAccounts (int clientId) throws SQLException, NotFoundException {
		List <Account> accounts = new ArrayList <> ();
		
		for (Account account : accountDao.getAccounts ()) {
			if (account.getClientId () == clientId) {
				accounts.add (account);
			}
		}
		
		if (accounts.size () == 0) {
			throw new NotFoundException ("Error! No accounts found for client with client id: " + clientId);
		}
		
		return accounts;
	}
	
	public List <Account> getAccountsInRange (int clientId, float minimumBalance, float maximumBalance) throws SQLException, NotFoundException {
		List <Account> accounts = new ArrayList <> ();
		
		for (Account account : accountDao.getAccounts ()) {
			if (account.getClientId () == clientId && account.getBalance () > minimumBalance && account.getBalance () < maximumBalance) {
				accounts.add (account);
			}
		}
		
		if (accounts.size () == 0) {
			throw new NotFoundException ("Error! No accounts found within range for a client with client id: " + clientId);
		}
		
		return accounts;
	}
	
	public Account getAccount (int clientId, int accountId) throws SQLException, NotFoundException, UnauthorizedClientException {
		Account account = accountDao.getAccount (accountId);
		
		if (account.getClientId () != clientId) {
			throw new UnauthorizedClientException ("Error! Account with account id: " + accountId + " is not owned by a client with client id: " + clientId);
		}
		
		return account;
	}
	
	public void createAccount (int clientId, String accountName) throws SQLException, NotFoundException {
		accountDao.createAccount (clientId, accountName);
	}
	
	public void updateAccountName (int clientId, int accountId, String accountName) throws SQLException, NotFoundException, UnauthorizedClientException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			accountDao.updateAccountName (accountId, accountName);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Account with account id: " + accountId + " is not owned by a client with client id: " + clientId);
		}
	}
	
	public void deleteAccount (int clientId, int accountId) throws SQLException, NotFoundException, UnauthorizedClientException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			accountDao.deleteAccount (accountId);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Account with account id: " + accountId + " is not owned by a client with client id: " + clientId);
		}
	}
	
	public void depositToAccount (int clientId, int accountId, float amount) throws SQLException, NotFoundException, UnauthorizedClientException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			accountDao.updateAccountBalance (accountId, accountDao.getAccountBalance (accountId) + amount);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Account with account id: " + accountId + " is not owned by a client with client id: " + clientId);
		}
	}
	
	public void withdrawFromAccount (int clientId, int accountId, float amount) throws SQLException, NotFoundException, UnauthorizedClientException, InsufficientBalanceException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			float currentBalance = accountDao.getAccountBalance (accountId);
			
			if (currentBalance < amount) {
				throw new InsufficientBalanceException ("Error! Amount is more than the balance in account with account id: " + accountId);
			}
			
			accountDao.updateAccountBalance (accountId, currentBalance - amount);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Account with account id: " + accountId + " is not owned by a client with client id: " + clientId);
		}
	}
	
	public void transferBetweenAccounts (int clientId, int fromAccountId, int toAccountId, float amount) throws SQLException, NotFoundException, UnauthorizedClientException, InsufficientBalanceException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (fromAccountId) != clientId) {
			throw new UnauthorizedClientException ("Error! Account with account id: " + fromAccountId + " is not owned by a client with client id: " + clientId);
		}
		
		//if account is owned by clientId
		if (accountDao.getAccountClientId (toAccountId) != clientId) {
			throw new UnauthorizedClientException ("Error! Account with account id: " + toAccountId + " is not owned by a client with client id: " + clientId);
		}
		
		float fromAccountBalance = accountDao.getAccountBalance (fromAccountId);
		float toAccountBalance = accountDao.getAccountBalance (toAccountId);
		
		if (fromAccountBalance < amount) {
			throw new InsufficientBalanceException ("Error! Amount is more than the balance in account with account id: " + fromAccountId);
		}
		
		accountDao.updateAccountBalance (fromAccountId, fromAccountBalance - amount);
		accountDao.updateAccountBalance (toAccountId, toAccountBalance + amount);
	}
}
