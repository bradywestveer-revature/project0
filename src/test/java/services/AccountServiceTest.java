package services;

import dao.AccountDao;
import dao.AccountDaoImplementation;
import exceptions.InsufficientBalanceException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedClientException;
import models.Account;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
	private final AccountDao accountDao = Mockito.mock (AccountDaoImplementation.class);
	private final AccountService accountService = new AccountService (accountDao);
	
	@Test
	void getAccounts () throws SQLException, NotFoundException {
		List <Account> accounts = new ArrayList <> ();
		
		int client1Id = 1;
		
		accounts.add (new Account (1, client1Id, 100.0F, "Account 1"));
		accounts.add (new Account (2, client1Id, 999.0F, "Account 2"));
		accounts.add (new Account (3, 2, 1000.0F, "Account 3"));
		
		//copy accounts to clientAccounts
		List <Account> expectedResult = new ArrayList <> (accounts);
		
		//set up expectedResult to only contain Accounts with a clientId of client1Id
		expectedResult.remove (2);
		
		Mockito.when (accountDao.getAccounts ()).thenReturn (accounts);
		
		assertEquals (expectedResult, accountService.getAccounts (client1Id));
	}
	
	@Test
	void getAccountsWhenNoAccounts () throws SQLException {
		Mockito.when (accountDao.getAccounts ()).thenReturn (new ArrayList <> ());
		
		assertThrows (NotFoundException.class, () -> {
			accountService.getAccounts (1);
		});
	}
	
	@Test
	void getAccountsInRange () throws SQLException, NotFoundException {
		List <Account> accounts = new ArrayList <> ();
		
		int client1Id = 1;
		
		accounts.add (new Account (1, client1Id, 100.0F, "Account 1"));
		accounts.add (new Account (2, client1Id, 999.0F, "Account 2"));
		accounts.add (new Account (3, client1Id, 1000.0F, "Account 3"));
		accounts.add (new Account (4, 2, 500.0F, "Account 4"));
		
		//copy accounts to expectedResult
		List <Account> expectedResult = new ArrayList <> (accounts);
		
		//set up expectedResult to only contain Accounts within our range and with a clientId of client1Id
		expectedResult.remove (2);
		expectedResult.remove (2);
		
		Mockito.when (accountDao.getAccounts ()).thenReturn (accounts);
		
		assertEquals (expectedResult, accountService.getAccountsInRange (client1Id, 99.0F, 1000.0F));
	}
	
	@Test
	void getAccountsInRangeWhenNoAccounts () throws SQLException {
		Mockito.when (accountDao.getAccounts ()).thenReturn (new ArrayList <> ());
		
		assertThrows (NotFoundException.class, () -> {
			accountService.getAccountsInRange (1, 0.0F, 0.0F);
		});
	}
	
	@Test
	void getAccount () throws SQLException, NotFoundException, UnauthorizedClientException {
		int clientId = 1;
		int accountId = 1;
		
		Account account = new Account (accountId, clientId, 0.0F, "Account 1");
		
		Mockito.when (accountDao.getAccount (accountId)).thenReturn (account);
		
		assertEquals (account, accountService.getAccount (clientId, accountId));
	}
	
	@Test
	void getAccountWhenAccountIsNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int accountId = 1;
		
		Account account = new Account (accountId, 2, 0.0F, "Account 1");
		
		Mockito.when (accountDao.getAccount (accountId)).thenReturn (account);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.getAccount (clientId, accountId);
		});
	}
	
	@Test
	void createAccount () throws SQLException, NotFoundException {
		int clientId = 1;
		String accountName = "Client 1";
		
		accountService.createAccount (clientId, accountName);
		
		Mockito.verify (accountDao).createAccount (clientId, accountName);
	}
	
	@Test
	void updateAccountName () throws SQLException, UnauthorizedClientException, NotFoundException {
		int clientId = 1;
		int accountId = 1;
		String accountName = "Client 1";
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (clientId);
		
		accountService.updateAccountName (clientId, accountId, accountName);
		
		Mockito.verify (accountDao).updateAccountName (accountId, accountName);
	}
	
	@Test
	void updateAccountNameWhenAccountIsNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (2);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.updateAccountName (clientId, accountId, "Client 1");
		});
	}
	
	@Test
	void deleteAccount () throws SQLException, NotFoundException, UnauthorizedClientException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (clientId);
		
		accountService.deleteAccount (clientId, accountId);
		
		Mockito.verify (accountDao).deleteAccount (accountId);
	}
	
	@Test
	void deleteAccountWhenAccountIsNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (2);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.deleteAccount (clientId, accountId);
		});
	}
	
	@Test
	void depositToAccount () throws SQLException, UnauthorizedClientException, NotFoundException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (clientId);
		Mockito.when (accountDao.getAccountBalance (accountId)).thenReturn (0.0F);
		
		accountService.depositToAccount (clientId, accountId, 1.0F);
		
		Mockito.verify (accountDao).updateAccountBalance (accountId, 1.0F);
	}
	
	@Test
	void depositToAccountWhenAccountIsNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (2);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.depositToAccount (clientId, accountId, 0.0F);
		});
	}
	
	@Test
	void withdrawFromAccount () throws SQLException, NotFoundException, UnauthorizedClientException, InsufficientBalanceException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (clientId);
		Mockito.when (accountDao.getAccountBalance (accountId)).thenReturn (1.0F);
		
		accountService.withdrawFromAccount (clientId, accountId, 1.0F);
		
		Mockito.verify (accountDao).updateAccountBalance (accountId, 0.0F);
	}
	
	@Test
	void withdrawFromAccountWhenAccountIsNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (2);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.withdrawFromAccount (clientId, accountId, 0.0F);
		});
	}
	
	@Test
	void withdrawFromAccountWhenBalanceIsInsufficient () throws SQLException, NotFoundException, UnauthorizedClientException, InsufficientBalanceException {
		int clientId = 1;
		int accountId = 1;
		
		Mockito.when (accountDao.getAccountClientId (accountId)).thenReturn (clientId);
		Mockito.when (accountDao.getAccountBalance (accountId)).thenReturn (0.0F);
		
		assertThrows (InsufficientBalanceException.class, () -> {
			accountService.withdrawFromAccount (clientId, accountId, 1.0F);
		});
	}
	
	@Test
	void transferBetweenAccounts () throws SQLException, NotFoundException, UnauthorizedClientException, InsufficientBalanceException {
		int clientId = 1;
		int fromAccountId = 1;
		int toAccountId = 2;
		
		Mockito.when (accountDao.getAccountClientId (fromAccountId)).thenReturn (clientId);
		Mockito.when (accountDao.getAccountClientId (toAccountId)).thenReturn (clientId);
		Mockito.when (accountDao.getAccountBalance (fromAccountId)).thenReturn (1.0F);
		Mockito.when (accountDao.getAccountBalance (toAccountId)).thenReturn (0.0F);
		
		accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, 1.0F);
		
		Mockito.verify (accountDao).updateAccountBalance (fromAccountId, 0.0F);
		Mockito.verify (accountDao).updateAccountBalance (toAccountId, 1.0F);
	}
	
	@Test
	void transferBetweenAccountsWhenFromAccountIsNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int fromAccountId = 1;
		int toAccountId = 2;
		
		Mockito.when (accountDao.getAccountClientId (fromAccountId)).thenReturn (2);
		Mockito.when (accountDao.getAccountClientId (toAccountId)).thenReturn (clientId);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, 0.0F);
		});
	}
	
	@Test
	void transferBetweenAccountsWhenToAccountIsNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int fromAccountId = 1;
		int toAccountId = 2;
		
		Mockito.when (accountDao.getAccountClientId (fromAccountId)).thenReturn (clientId);
		Mockito.when (accountDao.getAccountClientId (toAccountId)).thenReturn (2);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, 0.0F);
		});
	}
	
	@Test
	void transferBetweenAccountsWhenBothAccountsAreNotOwnedByClient () throws SQLException, NotFoundException {
		int clientId = 1;
		int fromAccountId = 1;
		int toAccountId = 2;
		
		Mockito.when (accountDao.getAccountClientId (fromAccountId)).thenReturn (2);
		Mockito.when (accountDao.getAccountClientId (toAccountId)).thenReturn (2);
		
		assertThrows (UnauthorizedClientException.class, () -> {
			accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, 0.0F);
		});
	}
	
	@Test
	void transferBetweenAccountsWhenBalanceIsInsufficient () throws SQLException, NotFoundException {
		int clientId = 1;
		int fromAccountId = 1;
		int toAccountId = 2;
		
		Mockito.when (accountDao.getAccountClientId (fromAccountId)).thenReturn (clientId);
		Mockito.when (accountDao.getAccountClientId (toAccountId)).thenReturn (clientId);
		
		Mockito.when (accountDao.getAccountBalance (fromAccountId)).thenReturn (0.0F);
		
		assertThrows (InsufficientBalanceException.class, () -> {
			accountService.transferBetweenAccounts (clientId, fromAccountId, toAccountId, 1.0F);
		});
	}
}