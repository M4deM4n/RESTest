/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest.application.thread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import restest.datamodel.PostData;
import restest.datamodel.Request;
import restest.datamodel.RequestHeader;
import restest.datamodel.Response;



/**
 *
 * @author Pizano
 */
public class RESTConnection extends Task<Response>
{
    private HttpURLConnection connection;
    private URL url;
    private ObservableList<RequestHeader> headers;
    private ObservableList<PostData> postData;
    
    private String method;

    
    /**
     * 
     * @param url
     * @throws MalformedURLException 
     */
    public RESTConnection(String url) throws MalformedURLException
    {
        this.url = new URL(url);
    }

    
    
    /**
     * 
     * @param r 
     */
    public RESTConnection(Request r) throws IOException
    {
        this.url = r.getUrl();
        this.headers = r.getHeaderList();
        this.postData = r.getPostParamList();
        this.method = r.getMethod();
        this.connection = (HttpURLConnection) this.url.openConnection();
        setHeaders();
    }
    
    
    /**
     * 
     * @param url
     * @param headers
     * @throws MalformedURLException
     * @throws IOException 
     */
    public RESTConnection(String url, ObservableList<RequestHeader> headers, ObservableList<PostData> postData) throws MalformedURLException, IOException
    {
        this.headers = headers;
        this.postData = postData;
        this.url = new URL(url);
        this.connection = (HttpURLConnection) this.url.openConnection();
        
        setHeaders();
    }
    
    
    /**
     * 
     * @throws ProtocolException 
     */
    private void setHeaders() throws ProtocolException
    {
        
        int limit = this.headers.size();
        for(int i = 0; i < limit; i++)
        {
            RequestHeader header = this.headers.get(i);
            this.connection.setRequestProperty(header.getHeader(), header.getValue());
        }
    }
    
    
    /**
     * 
     * @param method
     * @throws ProtocolException 
     */
    public void setMethod(String method) throws ProtocolException
    {
        switch(method)
        {
            case "get":
            case "Get":
            case "GET":
                this.method = "GET";
                break;
                
            case "post":
            case "Post":
            case "POST":
                this.method = "POST";
                break;
                
            case "DELETE":
                this.method = "DELETE";
                break;
                
            case "PUT":
                this.method = "PUT";
                break;
                
            default:
                //Do Nothing
                break;
        }
        
        this.connection.setRequestMethod(this.method);
    }
    
    
    /**
     * 
     * @param code
     * @return 
     */
    private String getDataFromConnection(int code)
    {
        StringBuilder response = new StringBuilder();
        String tmp = "";
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = in.readLine()) != null)
            {
                response.append(line + "\n");
            }
            in.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return response.toString();
    }
    
    
    /**
     * 
     * @param responseCode 
     */
    private Response getResponse(int responseCode)
    {
        Response rsp = new Response();
        String data = getDataFromConnection(responseCode);

        rsp.responseCode = responseCode;
        rsp.responseHeaders = connection.getHeaderFields().entrySet();
        rsp.responseBody = data;
        
        return rsp;
    }
    

    
    /**
     * 
     * @return 
     */
    private String getPostData()
    {
        String data = "";
        int limit = postData.size();
        for(int i = 0; i < limit; i++)
        {
            PostData pstd = postData.get(i);
            if(i > 0) { data += "&"; }
            data += pstd.toString();
        }
        return data;
    }



    /**
     * 
     * @return
     * @throws Exception 
     */
    @Override
    protected Response call() throws Exception
    {
        System.out.println(method);
        
        
        try
        {
            if(method.equals("POST") || method.equals("PUT"))
            {
                connection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(getPostData());
                wr.flush();
                wr.close();
            }
            
            
            int responseCode = connection.getResponseCode();
            return getResponse(responseCode);
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        finally
        {
            if(connection != null)
            {
                connection.disconnect();
            }
        }
        
        return new Response();
    }
    
    @Override protected void succeeded() {
        super.succeeded();
        updateMessage("Done!");
    }
}
