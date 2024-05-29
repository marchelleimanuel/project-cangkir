package view.homepage;

public class Cart {

	private String cupName;
	private int cupPrice;
	private int quantity;
	private int totalPrice;
	
	public Cart(String cupName, int cupPrice, int quantity, int totalPrice) {
		this.cupName = cupName;
		this.cupPrice = cupPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		
	}
	
	public String getCupName() {
		return cupName;
	}

	public void setCupName(String cupName) {
		this.cupName = cupName;
	}

	public int getCupPrice() {
		return cupPrice;
	}

	public void setCupPrice(int cupPrice) {
		this.cupPrice = cupPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
}
