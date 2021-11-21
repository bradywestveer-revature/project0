package bodymodels;

public class Transaction {
	private float deposit;
	private float withdraw;
	
	//used by ObjectMapper
	public Transaction () {
		this.deposit = -1F;
		this.withdraw = -1F;
	}
	
	public Transaction (float deposit, float withdraw) {
		this.deposit = deposit;
		this.withdraw = withdraw;
	}
	
	public float getDeposit () {
		return deposit;
	}
	
	public void setDeposit (float deposit) {
		this.deposit = deposit;
	}
	
	public float getWithdraw () {
		return withdraw;
	}
	
	public void setWithdraw (float withdraw) {
		this.withdraw = withdraw;
	}
}
