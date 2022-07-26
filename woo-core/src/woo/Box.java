package woo;

import java.io.*;
import woo.Store.ServiceType;

public class Box extends Product implements Serializable {
    
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202007111850L;
 
    /** Service type of the box. */
    private ServiceType _serviceType;
    
    /** Constructor. */
    public Box(String productId, String supplierId, int price, int criticalValue,
    int amount, ServiceType serviceType){
        super(productId, supplierId, price, criticalValue, amount);
        _serviceType = serviceType;
        super.setDueDate(5);
    }

    /** Get the service type. */
    public ServiceType getServiceType(){
        return _serviceType;
    }

    @Override
    @SuppressWarnings("nls")
    public String toString(){
        String s = "BOX|";
        String s1 = super.toString();
        String s2 = "|" + _serviceType.toString();
        s = s + s1 + s2;
        return s;
    }
}
