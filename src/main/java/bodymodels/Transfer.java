package bodymodels;

public class Transfer {
	private float amount;
	
	//used by ObjectMapper
	public Transfer () {
		this.amount = -1F;
	}
	
	public Transfer (float amount) {
		this.amount = amount;
	}
	
	public float getAmount () {
		return amount;
	}
	
	public void setAmount (float amount) {
		this.amount = amount;
	}
}
