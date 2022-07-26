package woo.exceptions;

/** Exception for unknown product keys. */
public class UnknownProductException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011220120L;

  /** Unknown key. */
  private String _key;

  /** @param key Unknown key to report. */
  public UnknownProductException(String key) {
    _key = key;
  }

  /**
   * @return key
   */
  public String getKey() {
    return _key;
  }

}
