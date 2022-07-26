package woo;

import java.io.*;
import java.lang.Math;

public class SaleTransaction extends Transaction implements Serializable {

	/** Serial number for serialization. */
  	private static final long serialVersionUID = 202012101357L;
  	
    /** Attributes of a sale:
	 *  - id
	 *	- clientId
	 *	- product
	 *	- amount of product bought
	 *	- basePrice
	 *	- currentPrice, according to payment tardiness and status
	 * 	- datePayed
	 *	- paymentDueDate
	 */
    private Client _client;
    private Product _product;
    private int _amount;
    private double _currentPrice;
    private int _paymentDueDate;

    /** Constructor. */
    public SaleTransaction(int saleId, Client client, Product product, int amount,
    int paymentDueDate, int date) {
		
		super(saleId, product.getPrice() * amount);
		_client = client;
		_product = product;
		_amount = amount;
		_basePrice = product.getPrice() * amount;
		_paymentDueDate = paymentDueDate;
		_currentPrice = _client.getStatus().tellPrice(_basePrice, _product, date, paymentDueDate);
	}


	private void updateCurrentPrice(int date) {
		_currentPrice = _client.getStatus().tellPrice(_basePrice, _product, date, _paymentDueDate);
	}

	
	public double getPriceToPay(int date) {
		if (_datePayed < 0)
			this.updateCurrentPrice(date);
	
		return _currentPrice;
	}

	public void pay(int date) {
		_datePayed = date;
	}

	public int getDueDate() {
		return _paymentDueDate;
	}

	@Override
	public String showTransaction(int date) {
		return this.toString(date);
	}

	public String toString(int date) {

		String saleInfo = super.toString() + "|" + _client.getId() + "|" + 
			_product.getProductId() + "|" +  _amount + "|" + _basePrice
			+ "|" +  Math.round(this.getPriceToPay(date)) + "|" + _paymentDueDate;

		if (_datePayed >= 0) {
			saleInfo += "|" + _datePayed;
		}

		return saleInfo;
	}
}