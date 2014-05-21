/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest.datamodel;

import javafx.beans.property.SimpleStringProperty;



/**
 *
 * @author Pizano
 */
public class RequestHeader
{
    final private SimpleStringProperty header = new SimpleStringProperty();
    final private SimpleStringProperty value = new SimpleStringProperty();
    
    
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
        header.set(h);
        value.set(v);
    }
    
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return header.get() + ": " + value.get();
    }
    
    
    /**
     * 
     * @param h 
     */
    public void setHeader(String h)
    {
        header.set(h);
    }
    
    
    /**
     * 
     * @param v 
     */
    public void setValue(String v)
    {
        value.set(v);
    }
    
    
    /**
     * 
     * @return 
     */
    public String getHeader()
    {
        return header.get();
    }
    
    
    /**
     * 
     * @return 
     */
    public String getValue()
    {
        return value.get();
    }
}
