package woo.exceptions;

/** Exception for price-related problems. */
public class InvalidPriceException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011212218L;

  /** Bad price. */
  private int _price;

  /** @param price bad price to report. */
  public InvalidPriceException(int price) {
    _price = price;
  }

  public int getPrice() {
    return _price;
  }

}
