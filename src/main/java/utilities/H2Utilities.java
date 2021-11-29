package utilities;

import dao.DatabaseCredentials;

import java.sql.*;

public class H2Utilities {
	private static void executeStatement (String sqlStatement) {
		try (Connection connection = DriverManager.getConnection (DatabaseCredentials.H2Url, DatabaseCredentials.H2Username, DatabaseCredentials.H2Password)) {
			PreparedStatement statement = connection.prepareStatement (sqlStatement);
			
			statement.executeUpdate ();
		}
		
		catch (SQLException exception) {
			exception.printStackTrace ();
		}
	}
	
	public static void createClientsTable () {
		executeStatement ("CREATE TABLE clients (id serial PRIMARY KEY, name varchar (100) NOT NULL);");
	}
	
	public static void dropClientsTable () {
		executeStatement ("DROP TABLE clients;");
	}
	
	public static void createAccountsTable () {
		executeStatement ("CREATE TABLE accounts (id serial PRIMARY KEY, client_id_fk int NOT NULL REFERENCES clients (id) ON DELETE CASCADE, balance float NOT NULL DEFAULT 0.0, name varchar (100) NOT NULL);");
	}
	
	public static void dropAccountsTable () {
		executeStatement ("DROP TABLE accounts;");
	}
}
