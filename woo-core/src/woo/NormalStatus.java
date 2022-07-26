package woo;

import java.io.*;

public class NormalStatus extends Status implements Serializable {
	
	/** Serial number for serialization. */
	private static final long serialVersionUID = 202011070035L;
	
	/** Constructor with points. */
	public NormalStatus(Client client, double points) {
		super(client, points);
	}

	/** Upgrade the status to selection. */
    public void upgradeSelection() {
		_client.setStatus(new SelectionStatus(getClient(), getPoints()));
	}

	/** Upgrade the status to elite. */
    public void upgradeElite() {
		getClient().setStatus(new EliteStatus(getClient(), getPoints()));
	}

	public void demoteNormal() {}

	public void demoteSelection() {}

	@Override
	public void registerPayment(int currentDate, int paymentDueDate, double payment) {
		int daysTillDue = paymentDueDate - currentDate;

		if (daysTillDue < 0) {
			// left empty on purpose
		}
		else {
			double _newPts = payment * _pointsMultiplier;
			this.setPoints(_pts+_newPts);
			if (_pts > _ptsToEliteStatus) {
				this.upgradeElite();
			}
			else if (_pts > _ptsToSelectionStatus) {
				this.upgradeSelection();
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
			// Purposefully left empty
		}
		else if (deadlinePeriod == 3) {
			currentPrice = basePrice + 0.5*basePrice* daysTillDue;
		}
		else if (deadlinePeriod == 4) {
			currentPrice = basePrice + 0.1*basePrice*daysTillDue;
		}

		return currentPrice;
	}

	/** Return the client's status. */
	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return "NORMAL";
	}
}
