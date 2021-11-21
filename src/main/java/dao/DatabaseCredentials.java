package dao;

public class DatabaseCredentials {
	public static final String url = "jdbc:postgresql://" + System.getenv ("AWS_RDS_ENDPOINT") + "/project0";
	public static final String username = System.getenv ("AWS_RDS_USERNAME");
	public static final String password = System.getenv ("AWS_RDS_PASSWORD");
}
