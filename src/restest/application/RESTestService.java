/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restest.application;

import java.io.IOException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import restest.application.thread.RESTConnection;
import restest.datamodel.Request;
import restest.datamodel.Response;



/**
 *
 * @author Pizano
 */
public class RESTestService extends Service<Response>
{
    protected Request m_request;
    
    public void setRequest(Request request)
    {
        this.m_request = request;
    }
    
    @Override
    protected Task<Response> createTask()
    {
        Task<Response> task;
        
        try
        {
            return new RESTConnection(this.m_request);
        }
        catch (IOException ex)
        {
            return new Task<Response>() {

                @Override
                protected Response call() throws Exception
                {
                    Response rsp = new Response();
                    rsp.failed();
                    return rsp;
                }
                
            };
        }
    }
    
}
