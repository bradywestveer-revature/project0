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
	private AccountDao accountDao;
	
	public AccountService (AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public List <Account> getAccounts (int clientId) throws SQLException {
		List <Account> accounts = new ArrayList <> ();
		
		for (Account account : accountDao.getAccounts ()) {
			if (account.getClientId () == clientId) {
				accounts.add (account);
			}
		}
		
		return accounts;
	}
	
	public List <Account> getAccountsInRange (int clientId, Float minimum, Float maximum) throws SQLException {
		List <Account> accounts = new ArrayList <> ();
		
		for (Account account : accountDao.getAccounts ()) {
			if (account.getClientId () == clientId && account.getBalance () >= minimum && account.getBalance () <= maximum) {
				accounts.add (account);
			}
		}
		
		return accounts;
	}
	
	public Account getAccount (int clientId, Integer accountId) throws SQLException, NotFoundException, UnauthorizedClientException {
		Account account = accountDao.getAccount (accountId);
		
		if (account.getClientId () != clientId) {
			throw new UnauthorizedClientException ("Error! Client with client id: " + clientId + " does not own account with account id: " + accountId);
		}
		
		return account;
	}
	
	public void createAccount (Integer clientId, String accountName) throws SQLException, NotFoundException {
		accountDao.createAccount (clientId, accountName);
	}
	
	public void updateAccountName (int clientId, Integer accountId, String accountName) throws SQLException, NotFoundException, UnauthorizedClientException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			accountDao.updateAccountName (accountId, accountName);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Client with client id: " + clientId + " does not own account with account id: " + accountId);
		}
	}
	
	public void deleteAccount (int clientId, Integer accountId) throws SQLException, NotFoundException, UnauthorizedClientException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			accountDao.deleteAccount (accountId);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Client with client id: " + clientId + " does not own account with account id: " + accountId);
		}
	}
	
	public void depositToAccount (int clientId, Integer accountId, Float amount) throws SQLException, NotFoundException, UnauthorizedClientException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			Float currentBalance = accountDao.getAccountBalance (accountId);
			
			accountDao.updateAccountBalance (accountId, currentBalance + amount);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Client with client id: " + clientId + " does not own account with account id: " + accountId);
		}
	}
	
	public void withdrawFromAccount (int clientId, Integer accountId, Float amount) throws SQLException, NotFoundException, UnauthorizedClientException, InsufficientBalanceException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (accountId) == clientId) {
			Float currentBalance = accountDao.getAccountBalance (accountId);
			
			if (currentBalance < amount) {
				throw new InsufficientBalanceException ("Error! Amount is more than the balance in account with account id: " + accountId);
			}
			
			accountDao.updateAccountBalance (accountId, currentBalance - amount);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Client with client id: " + clientId + " does not own account with account id: " + accountId);
		}
	}
	
	public void transferBetweenAccounts (int clientId, Integer fromAccountId, Integer toAccountId, Float amount) throws SQLException, NotFoundException, UnauthorizedClientException, InsufficientBalanceException {
		//if account is owned by clientId
		if (accountDao.getAccountClientId (fromAccountId) == clientId && accountDao.getAccountClientId (toAccountId) == clientId) {
			Float fromAccountBalance = accountDao.getAccountBalance (fromAccountId);
			Float toAccountBalance = accountDao.getAccountBalance (toAccountId);
			
			if (fromAccountBalance < amount) {
				throw new InsufficientBalanceException ("Error! Amount is more than the balance in account with account id: " + fromAccountId);
			}
			
			accountDao.updateAccountBalance (fromAccountId, fromAccountBalance - amount);
			accountDao.updateAccountBalance (toAccountId, toAccountBalance + amount);
		}
		
		else {
			throw new UnauthorizedClientException ("Error! Client with client id: " + clientId + " does not own accounts with account ids: " + fromAccountId + " and " + toAccountId);
		}
	}
}
