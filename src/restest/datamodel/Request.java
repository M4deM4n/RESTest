/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restest.datamodel;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javafx.collections.ObservableList;



/**
 *
 * @author Pizano
 */
public class Request
{
    private String m_method;
    private URL m_url;
    private ObservableList<RequestHeader> m_headerList;
    private ObservableList<PostData> m_postParamList;

    final public static String GET = "GET";
    final public static String POST = "POST";
    final public static String PUT = "PUT";
    final public static String DELETE = "DELETE";
    
    
    
    /**
     * 
     */
    public Request() { this.m_method = Request.GET; }
    
    
    
    /**
     * 
     * @param url
     * @throws MalformedURLException 
     */
    public Request(String url) throws MalformedURLException
    {
        this.m_method = Request.GET;
        this.m_url = new URL(url);
    }
    
    
    
    /**
     * 
     * @param url 
     */
    public Request(URL url)
    {
        this.m_method = Request.GET;
        this.m_url = url;
    }
    
    
    
    /**
     * 
     * @param url
     * @param headers
     * @throws MalformedURLException 
     */
    public Request(String url, ObservableList<RequestHeader> headers) throws MalformedURLException
    {
        this.m_method = Request.GET;
        this.m_url = new URL(url);
        this.m_headerList = headers;
    }
    
    
    
    /**
     * 
     * @param url
     * @param headers
     * @throws MalformedURLException 
     */
    public Request(URL url, ObservableList<RequestHeader> headers) throws MalformedURLException
    {
        this.m_method = Request.GET;
        this.m_url = url;
        this.m_headerList = headers;
    }
    
    
    
    /**
     * 
     * @param url
     * @param headers
     * @param postData
     * @throws MalformedURLException 
     */
    public Request(String url, ObservableList<RequestHeader> headers, ObservableList<PostData> postData) throws MalformedURLException
    {
        this.m_method = Request.POST;
        this.m_url = new URL(url);
        this.m_headerList = headers;
        this.m_postParamList = postData;
    }
    
    
    
    /**
     * 
     * @param url
     * @param headers
     * @param postData
     * @throws MalformedURLException 
     */
    public Request(URL url, ObservableList<RequestHeader> headers, ObservableList<PostData> postData) throws MalformedURLException
    {
        this.m_method = Request.POST;
        this.m_url = url;
        this.m_headerList = headers;
        this.m_postParamList = postData;
    }
    
    
    
    /**
     * 
     * @return 
     */
    public URL getUrl()
    {
        return this.m_url;
    }
    
    
    
    /**
     * 
     * @return 
     */
    public ObservableList<RequestHeader> getHeaderList()
    {
        return this.m_headerList;
    }
    
    
    
    /**
     * 
     * @return 
     */
    public ObservableList<PostData> getPostParamList()
    {
        return this.m_postParamList;
    }
    
    
    
    /**
     * 
     * @return 
     */
    public String getMethod()
    {
        return this.m_method;
    }
    
    
    
    /**
     * 
     * @param url
     * @throws MalformedURLException 
     */
    public void setUrl(String url) throws MalformedURLException
    {
        this.m_url = new URL(url);
    }
    
    
    
    /**
     * 
     * @param url 
     */
    public void setUrl(URL url)
    {
        this.m_url = url;
    }
    
    
    
    /**
     * 
     * @param method
     * @throws ProtocolException 
     */
    public void setMethod(String method) throws ProtocolException
    {
        switch(method.toUpperCase())
        {
            case Request.DELETE:
                this.m_method = Request.DELETE;
                break;
                
            case Request.GET:
                this.m_method = Request.GET;
                break;
                
            case Request.POST:
                this.m_method = Request.POST;
                break;
                
            case Request.PUT:
                this.m_method = Request.PUT;
                break;
                
            default:
                throw new ProtocolException("Invalid Protocol.");
        }
    }
    
    
    
    /**
     * 
     * @param headers 
     */
    public void setHeaderList(ObservableList<RequestHeader> headers)
    {
        this.m_headerList = headers;
    }
    
    
    
    /**
     * 
     * @param postData 
     */
    public void setPostData(ObservableList<PostData> postData)
    {
        this.m_postParamList = postData;
    }
}
