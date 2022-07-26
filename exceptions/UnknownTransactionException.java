package woo.exceptions;


/** Exception for unknown transaction keys. */
public class UnknownTransactionException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202012092119L;

  /** Unknown key. */
  private int _key;

  /** @param key Unknown key to report. */
  public UnknownTransactionException(int key) {
    _key = key;
  }

  /**
   * @return transaction key
   */
  public int getKey() {
    return _key;
  }

}
