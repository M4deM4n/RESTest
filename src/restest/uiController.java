/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;
import restest.application.RESTApp;
import restest.application.RESTestService;
import restest.datamodel.PersistantRequest;
import restest.datamodel.PostData;
import restest.datamodel.Request;
import restest.datamodel.RequestHeader;
import restest.datamodel.Response;
import restest.datamodel.ResponseHeader;



/**
 *
 * @author Jeff Pizano
 */
public class uiController implements Initializable
{
    private final RESTApp app = new RESTApp();
    
    final private IntegerProperty rqSelectedIndex = new SimpleIntegerProperty();
    final private IntegerProperty pstSelectedIndex = new SimpleIntegerProperty();
    
    public ObservableList<ResponseHeader> responseHeaders;
    private ObservableList<RequestHeader> requestHeaders;
    private ObservableList<PostData> postData;
    
    public String response;
    public String bodyData;
    
    private String currentFile;
    
    @FXML
    TextField txtEndpoint;
    
    @FXML
    ChoiceBox choiceMethod;
    
    @FXML
    TableView<RequestHeader> tableReqH;
    
    @FXML
    TableView<PostData> tablePostData;
    
    @FXML
    TableView<ResponseHeader> tableRspHeader;
    
    @FXML
    Button btnRqAdd;
    
    @FXML
    Button btnRqDelete;
    
    @FXML 
    Button btnPDAdd;
    
    @FXML
    Button btnPDDel;
    
    @FXML
    Button btnPDClear;
    
    @FXML
    Label statusLabel;
    
    @FXML
    Hyperlink lnkJeffPizano; 
    
    @FXML
    TextArea txtResponseBody;
    
    @FXML
    ProgressBar progBar;
    
    @FXML
    TabPane tabsResponse;
    
    
    
    /**
     * 
     */
    @FXML
    private void saveRequest()
    {
        if(currentFile == null)
            saveRequestAs();
        
        PersistantRequest dat = new PersistantRequest();
        dat.setHeaders(requestHeaders);
        dat.setPostData(postData);
        dat.setURL(txtEndpoint.getText());
        
        try {
            FileOutputStream fout = new FileOutputStream(currentFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(dat);
            oos.close();
            }
        catch (Exception e) { e.printStackTrace(); }
        
        RESTest.stage.setTitle(RESTest.title + "- " + currentFile);
        statusLabel.setText("File Saved: " + currentFile);
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void saveRequestAs()
    {
        FileChooser myFile = new FileChooser();
        myFile.setTitle("Save Request As...");
        
        try {
            File file = myFile.showSaveDialog(null);
            currentFile = file.getPath();
            saveRequest();
        }
        catch(Exception ex) {
            Dialogs.create().showException(ex);
        }
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void openSaveFile()
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Request");
        PersistantRequest pq;
        
        try {
            File file = fc.showOpenDialog(null);
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fin);
            pq = (PersistantRequest) ois.readObject();
            ois.close();
            
            currentFile = file.getPath();
            
            requestHeaders.clear();
            postData.clear();
            
            txtEndpoint.setText(pq.getURL());
            requestHeaders.addAll(pq.getHeaderList());
            postData.addAll(pq.getPostDataList());
            
            RESTest.stage.setTitle(RESTest.title + "- " + currentFile);
            statusLabel.setText("Loaded File: " + currentFile);
            
        }
        catch(Exception ex) {
            Dialogs.create().showException(ex);
        }
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void resetApplication()
    {
        currentFile = null;
        requestHeaders.clear();
        postData.clear();
        requestHeaders.add(new RequestHeader("User-Agent", "RESTest/0.1"));
        requestHeaders.add(new RequestHeader("Date", app.getDate()));
        postData.clear();
        RESTest.stage.setTitle(RESTest.title);
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void exitCalled()
    {
        Platform.exit();
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void visitAuthorAddress()
    {
        try {
         String url = "http://www.jeffpizano.com";
         java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
       }
       catch (java.io.IOException e) {
           statusLabel.setText("Something went wrong :(");
       }
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void showAbout()
    {
        Dialogs.create()
                .title("About")
                .masthead("RESTest v0.1\nAuthor: Jeff Pizano")
                .message("Copyright 2014 - Jeff Pizano")
                .showInformation();
    }
    
    
    
    /**
     * This method is called when the user presses [ENTER] within the 
     * URL/Endpoint text box.
     */
    @FXML
    private void onEnter()
    {
        this.sendRequest();
    }

    
    
    /**
     * This method is called when the user presses the "Send Request" button.
     */
    @FXML
    private void sendRequest()
    {
        statusLabel.setText("Preparing...");
        
        String endpoint = txtEndpoint.getText();
        if(!endpoint.startsWith("http")) {
            txtEndpoint.setText("http://" + endpoint);
        }
        
        responseHeaders.clear();
        txtResponseBody.setText("");
        progBar.setProgress(-1);
        
        Request req = new Request();
        try {
            req.setMethod(choiceMethod.getSelectionModel().getSelectedItem().toString());
            req.setHeaderList(requestHeaders);
            req.setPostData(postData);
            req.setUrl(txtEndpoint.getText());
        }
        catch (ProtocolException ex) {
            statusLabel.setText("Invalid Protocol.");
            progBar.setProgress(0);
            return;
        }
        catch (MalformedURLException ex) {
            statusLabel.setText("Bad URL.");
            progBar.setProgress(0);
            return;
        }
        
        statusLabel.setText("Attempting...");
        RESTestService service = new RESTestService();
        service.setRequest(req);
        service.setOnSucceeded((WorkerStateEvent t) ->
        {
            progBar.setProgress(0);
            Response rsp = (Response) t.getSource().getValue();
            updateUI(rsp);
        });
        service.start();
    }
    
    
    
    /**
     * 
     * @param rsp 
     */
    private void updateUI(Response rsp)
    {
        String line;
        String h;
        String httpResponse = null;
        
        if(rsp.responseHeaders != null) {
            for (Map.Entry<String, List<String>> k : rsp.responseHeaders) {
                for (String v : k.getValue()){
                    if((h = k.getKey()) != null) {
                        responseHeaders.add(new ResponseHeader(h, v));
                        line = h+": "+v+"\n";
                    }
                    else {
                        httpResponse = v;
                    }
                }
            }
        }
        
        txtResponseBody.setText(rsp.responseBody);
        
        if(rsp.responseCode > 0) {
            statusLabel.setText(httpResponse);
            SingleSelectionModel<Tab> selectionModel = tabsResponse.getSelectionModel();
            selectionModel.select(1);
        }
        else {
            statusLabel.setText("Failed.");
        }
    }
    
    
    
    /**
     * This method is called when the user clicks the "Add" button in the
     * request headers section.
     */
    @FXML
    private void addRequestHeader()
    {
        requestHeaders.add(requestHeaders.size(), new RequestHeader("New Header", "Value"));
    }
    
    
    
    /**
     * This method is called when the user clicks the "Delete" button in the
     * request headers section.
     */
    @FXML
    private void deleteRequestHeader()
    {
        requestHeaders.remove(rqSelectedIndex.get());
    }
    
    
    
    /**
     * This method is called when the user clicks the "Clear" button in the
     * request headers section.
     */
    @FXML
    private void clearRequestHeaders()
    {
        requestHeaders.clear();
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void addPostData()
    {
        postData.add(postData.size(), new PostData("param", "data"));
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void deletePostData()
    {
        postData.remove(pstSelectedIndex.get());
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void clearPostData()
    {
        postData.clear();
    }
    
    
    
    /**
     * This method is used to initialize the application and configure default
     * settings
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Set defaults
        choiceMethod.getSelectionModel().selectFirst();
        
        requestHeaders = FXCollections.observableArrayList(new RequestHeader("User-Agent", "RESTest/0.1"), new RequestHeader("Date", app.getDate()));
        postData = FXCollections.observableArrayList();
        responseHeaders = FXCollections.observableArrayList();
        statusLabel.setText("idle...");
        txtResponseBody.setText("");
        
        // Init User Interface
        initRequestHeaderTable();
        initPostDataTable();
        initResponseHeaderTable();
    }   
    
    
    
    /**
     * This method initializes the TableView for the Request Headers section.
     * This is where the TableView events are defined and configured.
     */
    private void initRequestHeaderTable()
    {
        Callback<TableColumn, TableCell> cellFactory =
             (TableColumn p) -> new EditingCell();
        
        TableColumn colHeader = new TableColumn("Request Header");
        colHeader.setMinWidth(200);
        colHeader.setCellValueFactory(new PropertyValueFactory<>("header"));
        colHeader.setCellFactory(cellFactory);
        colHeader.setOnEditCommit(
            new EventHandler<CellEditEvent<RequestHeader, String>>() {
                @Override
                public void handle(CellEditEvent<RequestHeader, String> t) {
                    ((RequestHeader) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setHeader(t.getNewValue());
                }
            }
        );
        
        TableColumn colValue = new TableColumn("Value");
        colValue.setMinWidth(300);
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        colValue.setCellFactory(cellFactory);
        colValue.setOnEditCommit(
            new EventHandler<CellEditEvent<RequestHeader, String>>() {
                @Override
                public void handle(CellEditEvent<RequestHeader, String> t) {
                    ((RequestHeader) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setValue(t.getNewValue());
                }
            }
        );
        
        tableReqH.setEditable(true);
        tableReqH.setItems(requestHeaders);
        tableReqH.getColumns().addAll(colHeader, colValue);
        tableReqH.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                rqSelectedIndex.set(requestHeaders.indexOf(newValue));
            }
        });
    }
    
    
    
    /**
     * This method initializes the TableView for the POST data section.
     * This is where the TableView events are defined and configured.
     */
    private void initPostDataTable()
    {
        tablePostData.setEditable(true);
        
        Callback<TableColumn, TableCell> cellFactory = 
                (TableColumn p) -> new EditingCell();
        
        TableColumn colParam = new TableColumn("Parameter");
        colParam.setMinWidth(200);
        colParam.setCellValueFactory(new PropertyValueFactory<PostData, String>("param"));
        colParam.setCellFactory(cellFactory);
        colParam.setOnEditCommit(
            new EventHandler<CellEditEvent<PostData, String>>() {
                @Override
                public void handle(CellEditEvent<PostData, String> t) {
                    ((PostData) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setParam(t.getNewValue());
                }
            }
        );
        
        TableColumn colData = new TableColumn("Data");
        colData.setMinWidth(300);
        colData.setCellValueFactory(new PropertyValueFactory<PostData, String>("data"));
        colData.setCellFactory(cellFactory);
        colData.setOnEditCommit(
            new EventHandler<CellEditEvent<PostData, String>>() {
                @Override
                public void handle(CellEditEvent<PostData, String> t) {
                    ((PostData) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setData(t.getNewValue());
                }
            }
        );
        
        tablePostData.setItems(postData);
        tablePostData.getColumns().addAll(colParam, colData);
        tablePostData.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                pstSelectedIndex.set(postData.indexOf(newValue));
            }
        });
    }
    
    
    
    /**
     * 
     */
    private void initResponseHeaderTable()
    {
        TableColumn colHeader = new TableColumn("Request Header");
        colHeader.setMinWidth(200);
        colHeader.setCellValueFactory(new PropertyValueFactory<>("header"));

        TableColumn colValue = new TableColumn("Value");
        colValue.setMinWidth(300);
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableRspHeader.setItems(responseHeaders);
        tableRspHeader.getColumns().addAll(colHeader, colValue);
    }
}
