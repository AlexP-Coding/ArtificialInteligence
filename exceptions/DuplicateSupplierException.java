package woo.exceptions;

/** Exception thrown when a supplier key is duplicated. */
public class DuplicateSupplierException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201709021724L;

  /** Supplier key. */
  private String _key;

  /** @param key the duplicated key */
  public DuplicateSupplierException(String key) {
    _key = key;
  }

  /**
   * @return key
   */
  public String getKey() {
    return _key;
  }

}
