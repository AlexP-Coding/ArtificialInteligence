package woo.exceptions;

/** Exception for too large sales requests. */
public class UnderstockedProductException extends Exception {

	/** Serial number for serialization. */
  private static final long serialVersionUID = 202012101951L;

  /** Product key. */
  private String _key;

   /** Amount requested. */
  private int _requested;

  private int _available;

  /** 
   * @param key the requested key
   * @param requested
   * @param available
   */
  public UnderstockedProductException(String key, int requested, int available) {
    _key = key;
    _requested = requested;
    _available = available;
  }

  public String getKey() {
  	return _key;
  }

  public int getRequested() {
  	return _requested;
  }

  public int getAvailable() {
  	return _available;
  }
}