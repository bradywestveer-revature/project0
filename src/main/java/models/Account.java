package models;

public class Account {
	private Integer id;
	private Integer clientId;
	private Float balance;
	private String name;
	
	//used by ObjectMapper
	public Account () {}
	
	public Account (Integer id, Integer clientId, Float balance, String name) {
		this.id = id;
		this.clientId = clientId;
		this.balance = balance;
		this.name = name;
	}
	
	public Integer getId () {
		return id;
	}
	
	public void setId (Integer id) {
		this.id = id;
	}
	
	public Integer getClientId () {
		return clientId;
	}
	
	public void setClientId (Integer clientId) {
		this.clientId = clientId;
	}
	
	public Float getBalance () {
		return balance;
	}
	
	public void setBalance (Float balance) {
		this.balance = balance;
	}
	
	public String getName () {
		return name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	@Override
	public String toString () {
		return "Account{" +
				"id=" + id +
				", clientId=" + clientId +
				", balance=" + balance +
				", name='" + name + '\'' +
				'}';
	}
}
