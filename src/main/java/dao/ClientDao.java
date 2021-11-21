package dao;

import exceptions.NotFoundException;
import models.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientDao {
	List <Client> getClients () throws SQLException;
	Client getClient (Integer clientId) throws SQLException, NotFoundException;
	void createClient (String clientName) throws SQLException;
	void updateClientName (Integer clientId, String clientName) throws SQLException, NotFoundException;
	void deleteClient (Integer clientId) throws SQLException, NotFoundException;
}
