package woo.exceptions;

/** Exception for date-related problems. */
public class InvalidDateNumberException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202009192335L;

  /** Bad date. */
  private int _date;

  /** @param date bad date to report. */
  public InvalidDateNumberException(int date) {
    _date = date;
  }

  public int getDate() {
    return _date;
  }

}
