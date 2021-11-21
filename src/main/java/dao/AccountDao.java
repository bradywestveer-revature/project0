package dao;

import exceptions.NotFoundException;
import models.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao {
	List <Account> getAccounts () throws SQLException;
	Account getAccount (Integer accountId) throws SQLException, NotFoundException;
	Integer getAccountClientId (int accountId) throws SQLException, NotFoundException;
	Float getAccountBalance (Integer accountId) throws SQLException, NotFoundException;
	void createAccount (Integer clientId, String accountName) throws SQLException, NotFoundException;
	void updateAccountName (Integer accountId, String accountName) throws SQLException, NotFoundException;
	void updateAccountBalance (Integer accountId, Float balance) throws SQLException, NotFoundException;
	void deleteAccount (Integer accountId) throws SQLException, NotFoundException;
}
