package models;

import java.util.Objects;

public class Client {
	private int id;
	private String name;
	
	//used by ObjectMapper
	public Client () {}
	
	public Client (int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId () {
		return id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getName () {
		return name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	@Override
	public String toString () {
		return "Client{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
	
	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || getClass () != o.getClass ()) return false;
		Client client = (Client) o;
		return id == client.id && Objects.equals (name, client.name);
	}
}
