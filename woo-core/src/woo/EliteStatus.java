package woo;

import java.io.*;

public class EliteStatus extends Status implements Serializable {
	
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202011072320L;
	
	/** Constructor with points. */
	public EliteStatus(Client client, double points) {
		super(client, points);
		_pointLossDivisor = 4;
		_tardinessAllowance = 15;
	}

	/** Demote the status to selection. */
	public void demoteSelection() {
		getClient().setStatus(new SelectionStatus(getClient(), getPoints()*_pointLossDivisor));
	}

	public void demoteNormal() {}

	public void upgradeSelection() {}

	public void upgradeElite() {}

	@Override
	public void registerPayment(int currentDate, int paymentDueDate, double payment) {
		int daysTillDue = paymentDueDate - currentDate;

		if (daysTillDue < 0) {
			if (daysTillDue < -_tardinessAllowance)
				this.demoteSelection();
		}
		else {
			double _newPts = payment * _pointsMultiplier;
			this.setPoints(_pts+_newPts);
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
			currentPrice = 0.90 * basePrice; 
		}
		else if (deadlinePeriod == 3) {
			currentPrice = 0.95 * basePrice; 
		}
		else if (deadlinePeriod == 4) {
			// Purposefully left empty
		}

		return currentPrice;
	}

	/** Return the client's status. */
	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return "ELITE";
	}
}
