package woo;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class OrderTransaction extends Transaction implements Serializable {

	/** Serial number for serialization. */
  	private static final long serialVersionUID = 202012062319L;
  	
    /** Attributes of an order:
	 *  - id
	 *	- basePrice
	 * 	- datePayed
	 *	- supplierId
	 *	- list of products requested
	 * 	- amount of each product requested 
	 */
    private ArrayList<String> _products = new ArrayList<String>();
    private List<Integer> _amounts = new ArrayList<Integer>();
    private String _supplierId;

    /** Constructor. */
  public OrderTransaction(String supplierId, int id, int basePrice, int datePayed,
	  ArrayList<String> products, ArrayList<Integer> amounts) {
		super(id, basePrice, datePayed);
		_supplierId = supplierId;
		_products = products;
		_amounts = amounts;	
	}
	
	@Override
	public double getPriceToPay() {
		return (double) -this.getBasePrice();
	}

	@Override
	public String showTransaction(int date) {
		return this.toString();
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {

		String orderHeader = super.toString() + "|" + _supplierId + "|" + _basePrice + "|" + _datePayed;
		String orderDetails = "";

	 	for (int i = 0; i < _products.size(); i++) {
	 		orderDetails += _products.get(i) + "|" + _amounts.get(i) + '\n';
	 	}

		return orderHeader + '\n' + orderDetails;
	}
}