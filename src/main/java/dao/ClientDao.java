package dao;

import exceptions.NotFoundException;
import models.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientDao {
	List <Client> getClients () throws SQLException;
	Client getClient (int clientId) throws SQLException, NotFoundException;
	void createClient (String clientName) throws SQLException;
	void updateClientName (int clientId, String clientName) throws SQLException, NotFoundException;
	void deleteClient (int clientId) throws SQLException, NotFoundException;
}
