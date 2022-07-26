package woo.exceptions;

/** Exception for unknown supplier keys. */
public class InvalidServiceLevelException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201004192435L;

  /** Unknown key. */
  private String _service_level;

  /** @param service_level Unknown key to report. */
  public InvalidServiceLevelException(String service_level) {
    _service_level = service_level;
  }

  /**
   * @return service_level
   */
  public String getServiceLevel() {
    return _service_level;
  }

}
