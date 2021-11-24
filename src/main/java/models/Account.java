package models;

import java.util.Objects;

public class Account {
	private int id;
	private int clientId;
	private Float balance;
	private String name;
	
	//used by ObjectMapper
	public Account () {}
	
	public Account (int id, int clientId, Float balance, String name) {
		this.id = id;
		this.clientId = clientId;
		this.balance = balance;
		this.name = name;
	}
	
	public int getId () {
		return id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public int getClientId () {
		return clientId;
	}
	
	public void setClientId (int clientId) {
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
	
	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || getClass () != o.getClass ()) return false;
		Account account = (Account) o;
		return id == account.id && clientId == account.clientId && Objects.equals (balance, account.balance) && Objects.equals (name, account.name);
	}
}
