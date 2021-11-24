package dao;

import exceptions.NotFoundException;
import models.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.H2Utilities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoImplementationTest {
	private final ClientDao clientDao = new ClientDaoImplementation (DatabaseCredentials.H2Url, DatabaseCredentials.H2Username, DatabaseCredentials.H2Password);
	private final AccountDao accountDao = new AccountDaoImplementation (clientDao, DatabaseCredentials.H2Url, DatabaseCredentials.H2Username, DatabaseCredentials.H2Password);
	
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
	void getAccounts () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		List <Account> accounts = new ArrayList <> ();
		
		accounts.add (new Account (1, 1, 0.0F, "Account 1"));
		accounts.add (new Account (2, 1, 0.0F, "Account 2"));
		accounts.add (new Account (3, 1, 0.0F, "Account 3"));
		
		accountDao.createAccount (accounts.get (0).getClientId (), accounts.get (0).getName ());
		accountDao.createAccount (accounts.get (1).getClientId (), accounts.get (1).getName ());
		accountDao.createAccount (accounts.get (2).getClientId (), accounts.get (2).getName ());
		
		assertEquals (accounts, accountDao.getAccounts ());
	}
	
	@Test
	void getAccount () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		Account account = new Account (1, 1, 0.0F, "Account 1");
		
		accountDao.createAccount (account.getClientId (), account.getName ());
		
		assertEquals (account, accountDao.getAccount (1));
	}
	
	@Test
	void getAccountClientId () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		int clientId = 1;
		
		accountDao.createAccount (clientId, "Account 1");
		
		assertEquals (clientId, accountDao.getAccountClientId (1));
	}
	
	@Test
	void getAccountBalance () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		accountDao.createAccount (1, "Account 1");
		
		assertEquals (0.0F, accountDao.getAccountBalance (1));
	}
	
	@Test
	void createAccount () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		accountDao.createAccount (1, "Account 1");
		accountDao.createAccount (1, "Account 2");
		accountDao.createAccount (1, "Account 3");
		
		assertEquals (3, accountDao.getAccounts ().size ());
	}
	
	@Test
	void updateAccountName () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		int accountId = 1;
		String accountName = "New Client 1";
		
		accountDao.createAccount (1, "Account 1");
		
		accountDao.updateAccountName (accountId, accountName);
		
		assertEquals (accountName, accountDao.getAccount (accountId).getName ());
	}
	
	@Test
	void updateAccountBalance () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		int accountId = 1;
		float accountBalance = 1.0F;
		
		accountDao.createAccount (1, "Account 1");
		
		accountDao.updateAccountBalance (accountId, accountBalance);
		
		assertEquals (accountBalance, accountDao.getAccount (accountId).getBalance ());
	}
	
	@Test
	void deleteAccount () throws SQLException, NotFoundException {
		clientDao.createClient ("Client 1");
		
		accountDao.createAccount (1, "Account 1");
		
		accountDao.deleteAccount (1);
		
		assertEquals (0, accountDao.getAccounts ().size ());
	}
}