package woo;

import java.io.*;
import java.util.ArrayList;

public class Client implements Serializable {
	
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202011070006L;
	
	/** Attributes for client:
	 *  -id
	 *  -name
	 *  -address
	 *  -status
	 *  -total amount payed
	 *  -total amount purchased
	 *	- sales
	 *	- notifications
	 */
	private String _id;
	private String _name;
	private String _address;
	private Status _status;
	private int _totalPayed;
	private int _totalPurchased;
	private ArrayList<SaleTransaction> _sales;
	private ArrayList<Notification> _notifs;
	
	/** Constructor. */
	public Client(String id, String name, String address) {
		_id = id;
		_name = name;
		_address = address;
		_status = new NormalStatus(this, 0);
		_totalPurchased = 0;
		_totalPayed = 0;
		_notifs = new ArrayList<Notification>();
		_sales = new ArrayList<SaleTransaction>();
	}
	
	/** Get id. */
	public String getId() {
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
	
	/** Get total payed. */
	public int getTotalPayed() {
		return _totalPayed;
	}
	
	/** Get total purchased. */
	public int getTotalPurchased() {
		return _totalPurchased;
	}

	/** Get the client status. */
	public Status getStatus() {
		return _status;
	}

	/** Define the client's new status. */
	public void setStatus(Status newStatus) {
		_status = newStatus;
	}

	/** Get the client's points. */
	public double getPoints() {
		return _status.getPoints();
	}

	/** Process upgrade to selection. */
	public void upgradeSelection() {
		_status.upgradeSelection();
	}

	/** Process upgrade to elite. */
	public void upgradeElite() {
		_status.upgradeElite();
	}

	/** Process demotion to selection. */
	public void demoteSelection() {
		_status.demoteSelection();
	}

	/** Process demote to normal. */
	public void demoteNormal() {
		_status.demoteNormal();
	}
	
	/** Adds a new notification to the list. */
	public void updateNotifs(Notification notif) {
		_notifs.add(notif);
	}

	/** Get the client's notifications. */
	public ArrayList<Notification> getNotifs() {
		return _notifs;
	}

	/** Remove the notifications from the client's list. */
	public void removeNotifs() {
		Notification notif;
		for (int i = 0; i < _notifs.size(); i++) {
				notif = _notifs.get(i);
			_notifs.remove(notif);
		}
	}
	
	/** Pay a client's sale. */
	public boolean pay(int saleId, int date) {
		SaleTransaction sale = _sales.get(saleId);

		if (sale != null && sale.getDatePayed() < 0) {
			double payment = sale.getPriceToPay(date);
			int dueDate = sale.getDueDate();
			_status.registerPayment(date, dueDate, payment);
			sale.pay(date);
			return true;
		}
		else
			return false;
	}
	
	/** Add sale to the list of sales. */
	public void addSale(SaleTransaction sale) {
		_sales.add(sale);
	} 
	
	/** Get the list of sales. */
	public ArrayList<SaleTransaction> getSales() {
		return _sales;
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		String s = String.format("%s|%s|%s|%s|%d|%d", _id, _name, _address,
		_status.toString(), _totalPurchased, _totalPayed);
		return s;
	}
}
