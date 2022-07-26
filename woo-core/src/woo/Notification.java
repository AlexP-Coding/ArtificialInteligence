package woo;

import java.io.*;

public class Notification implements Serializable {
    
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202012101423L;
    
    /** Attributes for notification:
     *   -productId
     *   -type
     */
    private String _productId;
    private String _type;

    /** Constructor. */
    public Notification(String productId, String type) {
        _productId = productId;
        _type = type;
    }

    public String getProductId() {
        return _productId;
    }

    public String getType() {
        return _type;
    }
}