package woo.exceptions;

/** Exception for unknown supplier keys. */
public class UnknownSupplierException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202004192435L;

  /** Unknown key. */
  private String _key;

  /** @param key Unknown key to report. */
  public UnknownSupplierException(String key) {
    _key = key;
  }

  /**
   * @return key
   */
  public String getKey() {
    return _key;
  }

}
