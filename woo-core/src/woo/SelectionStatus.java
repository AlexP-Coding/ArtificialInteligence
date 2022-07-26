package woo;

import java.io.*;

public class SelectionStatus extends Status implements Serializable {
	
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202011070044L;
	
	/** Constructor with points. */
	public SelectionStatus(Client client, double points) {
		super(client, points);
		_pointLossDivisor = 10;
		_tardinessAllowance = 2;
	}

	/** Upgrade the status to elite. */
    public void upgradeElite() {
		getClient().setStatus(new EliteStatus(getClient(), getPoints()));
	}

	/** Demote the status to normal. */
    public void demoteNormal() {
		getClient().setStatus(new NormalStatus(getClient(), getPoints()/_pointLossDivisor));
	}

	public void upgradeSelection() {}

	public void demoteSelection() {}

	@Override
	public void registerPayment(int currentDate, int paymentDueDate, double payment) {
		int daysTillDue = paymentDueDate - currentDate;

		if (daysTillDue < 0) {
			if (daysTillDue < -_tardinessAllowance)
				this.demoteNormal();
		}
		else {
			double _newPts = payment * _pointsMultiplier;
			this.setPoints(_pts+_newPts);
			if (_pts > _ptsToEliteStatus) {
				this.upgradeElite();
			}
		} 
	}

	@Override
	public double tellPrice(int basePrice, Product product, int currentDate, int paymentDueDate) {		
		int deadlinePeriod = super.computeDeadlinePeriod(product, currentDate, paymentDueDate);
		int daysTillDue = paymentDueDate - currentDate;
		double currentPrice = basePrice;
		
		if (deadlinePeriod == 1) {
			currentPrice = 0.90 * basePrice; 
		}
		else if (deadlinePeriod == 2) {
			if (daysTillDue >= 2) {
				currentPrice = 0.95*basePrice;
			}
		}
		else if (deadlinePeriod == 3) {
			if (daysTillDue < -1) {
				currentPrice = basePrice - 0.2*basePrice* daysTillDue;
			}
		}
		else if (deadlinePeriod == 4) {
			currentPrice = basePrice - 0.5*basePrice*daysTillDue;
		}

		return currentPrice;
	}

	/** Return the client's status. */
	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return "SELECTION";
	}
}
