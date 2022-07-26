package woo.exceptions;

/** Exception for off-toggled supplier keys. */
public class BlacklistedSupplierException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202012091302L;

  /** Blacklisted key. */
  private String _key;

  /** @param key Blacklisted key to report. */
  public BlacklistedSupplierException(String key) {
    _key = key;
  }

  /**
   * @return key
   */
  public String getKey() {
    return _key;
  }

}
