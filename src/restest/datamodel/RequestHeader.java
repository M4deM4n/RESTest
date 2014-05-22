/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest.datamodel;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;



/**
 *
 * @author Pizano
 */
public class RequestHeader implements Serializable
{
//    final private SimpleStringProperty header = new SimpleStringProperty();
//    final private SimpleStringProperty value = new SimpleStringProperty();
    
    private String header;
    private String value;
    
    
    /**
     * 
     */
    public RequestHeader()
    {
        // Do Nothing
    }
    
    
    /**
     * 
     * @param h
     * @param v 
     */
    public RequestHeader(String h, String v)
    {
//        header.set(h);
//        value.set(v);
        header = h;
        value = v;
    }
    
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return header + ": " + value;
    }
    
    
    /**
     * 
     * @param h 
     */
    public void setHeader(String h)
    {
        header = h;
    }
    
    
    /**
     * 
     * @param v 
     */
    public void setValue(String v)
    {
        value = v;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getHeader()
    {
        return header;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getValue()
    {
        return value;
    }
}
