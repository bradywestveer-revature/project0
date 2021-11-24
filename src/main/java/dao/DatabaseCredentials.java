package dao;

public class DatabaseCredentials {
	public static final String url = "jdbc:postgresql://" + System.getenv ("AWS_RDS_ENDPOINT") + "/project0";
	public static final String username = System.getenv ("AWS_RDS_USERNAME");
	public static final String password = System.getenv ("AWS_RDS_PASSWORD");
	
	public static final String H2Url = "jdbc:h2:./h2/database";
	public static final String H2Username = "sa";
	public static final String H2Password = "sa";
}
