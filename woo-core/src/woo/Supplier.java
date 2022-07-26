package woo;

import java.io.*;
import java.util.ArrayList;

public class Supplier implements Serializable {
	
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202007112340L;
	
	/** Attributes for supplier:
	 *  -id
	 *  -name
	 *  -address
	 *  -toggle status
	*/
	private String _id;
	private String _name;
	private String _address;
	private boolean _toggle;
	private ArrayList<OrderTransaction> _orders;
	
	/** Constructor. */
	public Supplier(String id, String name, String address) {
		_id = id;
		_name = name;
		_address = address;
		_toggle = true;
		_orders = new ArrayList<OrderTransaction>();
	}
	
	/** Get id. */
	public String getId(){
		return _id;
	}
	
	/** Get name. */
	public String getName() {
		return _name;
	}
	
	/** Get address. */
	public String getAddress() {
		return _address;
	}
	
	/** Get toggle status. */
	public boolean getToggleStatus() {
		return _toggle;
	}
	
	/** Switches toggle status. */
	public void switchToggle() {
		if (_toggle == true)
			_toggle = false;
		else if (_toggle == false)
			_toggle = true;
	}
	
	/** Add an order to the list of orders. */
	public void addOrder(OrderTransaction order) {
		_orders.add(order);
	}
	
	/** Show all orders from the list. */
	public ArrayList<OrderTransaction> getOrders() {
		return _orders;
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		String toggle;
		String s;

		if (_toggle == true)
			toggle = "SIM";
		else {
			toggle = "NÃO";
		}
		s = String.format("%s|%s|%s|%s", _id, _name, _address, toggle);
		return s;
	}
}