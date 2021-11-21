package bodymodels;

public class Transfer {
	private Float amount;
	
	//used by ObjectMapper
	public Transfer () {}
	
	public Transfer (Float amount) {
		this.amount = amount;
	}
	
	public Float getAmount () {
		return amount;
	}
	
	public void setAmount (Float amount) {
		this.amount = amount;
	}
}
