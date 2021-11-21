package bodymodels;

public class Transaction {
	private Float deposit;
	private Float withdraw;
	
	//used by ObjectMapper
	public Transaction () {}
	
	public Transaction (Float deposit, Float withdraw) {
		this.deposit = deposit;
		this.withdraw = withdraw;
	}
	
	public Float getDeposit () {
		return deposit;
	}
	
	public void setDeposit (Float deposit) {
		this.deposit = deposit;
	}
	
	public Float getWithdraw () {
		return withdraw;
	}
	
	public void setWithdraw (Float withdraw) {
		this.withdraw = withdraw;
	}
}
