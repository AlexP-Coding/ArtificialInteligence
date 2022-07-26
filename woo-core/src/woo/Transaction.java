package woo;

import java.io.*;

public class Transaction implements Serializable {

	/** Serial number for serialization. */
  	private static final long serialVersionUID = 202012062300L;

    /** Attributes of a transaction:
	 *  - id
	 *  - basePrice
	 *  - datePayed
	 */
	private int _id;
	protected int _basePrice;
	protected int _datePayed;

	/** Constructor. */
	public Transaction(int id, int basePrice, int datePayed) {
		_id = id;
		_basePrice = basePrice;
		_datePayed = datePayed;
	}

	/** Constructor (unpayed transaction). */
	public Transaction(int id, int basePrice) {
		_id = id;
		_basePrice = basePrice;
		_datePayed = -1;
	}

	public int getBasePrice() {
		return _basePrice;
	}

	public double getPriceToPay() {
		return (double) this.getBasePrice();
	}

	public void pay(int date) {}

	public int getDatePayed() {
		return _datePayed;
	}

	public String showTransaction(int date) {
		return "";
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return "" + _id;
	}

}
