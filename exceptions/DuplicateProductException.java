package woo.exceptions;


/** Exception thrown when a product key is duplicated. */
public class DuplicateProductException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201709021329L;

  /** Product key. */
  private String _key;

  /** @param key the duplicated key */
  public DuplicateProductException(String key) {
    _key = key;
  }

  /**
   * @return key
   */
  public String getKey() {
    return _key;
  }

}
