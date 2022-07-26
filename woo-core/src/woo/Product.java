package woo;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Product implements Serializable {
	
	/** Serial number for serialization. */
    private static final long serialVersionUID = 202007112115L;
	
	/** Attributes of a product:
	 *  -id
	 *  -supplier id
	 *  -price
	 *  -critical amount
	 *  -amount
	 *  -extended due date
	 */
	private String _productId;
	private String _supplierId;
	private int _price;
	private int _criticalValue;
	private int _amount;
	private int _extendedDueDate;
	private ArrayList<Client> _clients;
	
	/** Constructor. */
	public Product(String productId, String supplierId, int price,
	int criticalValue, int amount) {
		_productId = productId;
		_supplierId = supplierId;
		_price = price;
		_criticalValue = criticalValue;
		_amount = amount;
		_clients = new ArrayList<>();
	}
	
	/** Get product id. */
	public String getProductId() {
		return _productId;
	}
	
	/** Get supplier id. */
	public String getSupplierId() {
		return _supplierId;
	}
	
	/** Get price. */
	public int getPrice() {
		return _price;
	}
	
	/** Get critical level. */
	public int getCritLvl() {
		return _criticalValue;
	}
	
	/** Get amount. */
	public int getAmount() {
		return _amount;
	}
	
	/** Add stock to an existing product. */
	public void addStock(int amount) {
		boolean notify = false;
		if(_amount == 0 && amount > 0){
			notify = true;
		}
		_amount += amount;
		if(notify == true){
			Notification notif = new Notification(_productId, "NEW");
			for(Client c : _clients){
				c.updateNotifs(notif);
			}
		}
	}
	
	/** Remove stock from an existing product. */
	public void removeStock(int amount) {
		_amount -= amount;
	}

	/** Get the extended due date. */
	public int getExtendedDueDate() {
		return _extendedDueDate;
	}

	/** Set the extended due date. */
	public void setDueDate(int date) {
		_extendedDueDate = date;
	}

	/** Sets price to new value */
	public void setPrice(int price) {
		boolean priceDown = false;
		if(price < _price){
			priceDown = true;
		}
		_price = price;
		if(priceDown == true){
			Notification notif = new Notification(_productId, "BARGAIN");
			for(Client c : _clients){
				c.updateNotifs(notif);
			}
		}
	}

	/** Adds a a client to the observer list. */
	public void addObserver(Client c) {
		_clients.add(c);
	}

	/** Removes a client from the observer list. */
	public void removeObserver(Client c) {
		_clients.remove(c);
	}

	/** Get the client list. */
	public ArrayList<Client> getClientList() {
		return _clients;
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return _productId + "|" + _supplierId + "|" + _price + "|" + _criticalValue + "|" + _amount;
	}
}