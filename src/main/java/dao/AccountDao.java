package dao;

import exceptions.NotFoundException;
import models.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao {
	List <Account> getAccounts () throws SQLException;
	Account getAccount (int accountId) throws SQLException, NotFoundException;
	int getAccountClientId (int accountId) throws SQLException, NotFoundException;
	float getAccountBalance (int accountId) throws SQLException, NotFoundException;
	void createAccount (int clientId, String accountName) throws SQLException, NotFoundException;
	void updateAccountName (int accountId, String accountName) throws SQLException, NotFoundException;
	void updateAccountBalance (int accountId, float balance) throws SQLException, NotFoundException;
	void deleteAccount (int accountId) throws SQLException, NotFoundException;
}
