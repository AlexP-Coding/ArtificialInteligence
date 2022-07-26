package woo;

import java.io.*;

public abstract class Status implements Serializable {
	
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202011070029L;
	
	/** Attributes for status:
	 *  -points
     *  -client
	 *  -percentage of points remaining
	 *  -points multiplier
	 *  -points to elite status
	 *  -points to selection status
	 */
	protected double _pts;
    protected Client _client;
    protected int _pointLossDivisor;
    protected int _pointsMultiplier;
    protected int _ptsToEliteStatus;
    protected int _ptsToSelectionStatus;
    protected int _tardinessAllowance;


	/** Constructor with points. */
	public Status(Client client, double points) {
		_pts = points;
		_client = client;
		_pointLossDivisor = 0;
		_pointsMultiplier = 10;
		_ptsToEliteStatus = 25000;
		_ptsToSelectionStatus = 2000;
		_tardinessAllowance = 0;
	}

	public abstract void upgradeSelection();

	public abstract void upgradeElite();

	public abstract void demoteSelection();

	public abstract void demoteNormal();

	/** Return the client. */
	public Client getClient() {
		return _client;
	}
	
	/** Get points. */
	public double getPoints() {
		return _pts;
	}

	protected int computeDeadlinePeriod(Product product, int currentDate, int paymentDueDate) {
		int deadlinePeriod = 0;
		int daysTillDue = paymentDueDate - currentDate;
		int payPeriod = product.getExtendedDueDate();

		if (daysTillDue >= payPeriod) {
			deadlinePeriod = 1;
		}
		else if ((0 <= daysTillDue) && (daysTillDue < payPeriod)) {
			deadlinePeriod = 2;
		}
		else if (daysTillDue >= (-payPeriod)) {
			deadlinePeriod = 3;
		}
		else if (daysTillDue < (-payPeriod)) {
			deadlinePeriod = 4;
		}

		return deadlinePeriod;
	}

	public abstract double tellPrice(int basePrice, Product product, int date, int paymentDueDate);

	protected abstract void registerPayment(int currentDate, int paymentDueDate, double payment);

	/** Set points. */
	protected void setPoints(double points){
	  _pts = points;
  }
}
