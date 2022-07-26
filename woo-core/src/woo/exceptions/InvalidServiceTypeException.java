package woo.exceptions;

/** Exception for unknown service type. */
public class InvalidServiceTypeException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201004192435L;

  /** Unknown key. */
  private String _service_type;

  /** @param service_type Unknown service type to report. */
  public InvalidServiceTypeException(String service_type) {
    _service_type = service_type;
  }

  /**
   * @return service_type
   */
  public String getServiceType() {
    return _service_type;
  }

}
