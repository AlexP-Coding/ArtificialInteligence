package woo;

import woo.Store.ServiceType;
import woo.Store.ServiceLevel;
import java.io.*;

public class Container extends Box implements Serializable{
    
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202007111924L;
    
    /** Service level of the container. */
    private ServiceLevel _serviceLevel;
    
    /** Constructor. */
    public Container(String productId, String supplierId, int price, int criticalValue,
    int amount, ServiceType serviceType, ServiceLevel serviceLevel){
        super(productId, supplierId, price, criticalValue, amount, serviceType);
        _serviceLevel = serviceLevel;
        super.setDueDate(8);
    }


    /** Get the service level. */
    public ServiceLevel getServiceLevel(){
        return _serviceLevel;
    }

    @Override
    @SuppressWarnings("nls")
    public String toString(){
        String s = "CONTAINER|";
        String s1 = super.toString().substring(4);
        String s2 = "|" + _serviceLevel.toString();
        s = s + s1 + s2;
        return s;
    }
}
