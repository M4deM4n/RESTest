/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import javafx.scene.control.CheckMenuItem;
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
import restest.application.RESTUtil;
import restest.application.RESTestService;
import restest.datamodel.PersistantRequest;
import restest.datamodel.PostParameter;
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
    final private IntegerProperty rqSelectedIndex = new SimpleIntegerProperty();
    final private IntegerProperty pstSelectedIndex = new SimpleIntegerProperty();
    
    private ObservableList<ResponseHeader> responseHeaderList;
    private ObservableList<RequestHeader> requestHeaderList;
    private ObservableList<PostParameter> postParameterList;
    
    private SingleSelectionModel<Tab> selectionModel;
    
    private String currentFile;
    private String defaultPath;
    
    /**
     * 
     */
    @FXML
    TextField urlEntry;
    
    /**
     * 
     */
    @FXML
    ChoiceBox methodChoice;
    
    /**
     * 
     */
    @FXML
    TableView<RequestHeader> requestHeaderTable;
    
    /**
     * 
     */
    @FXML
    TableView<PostParameter> postDataTable;
    
    /**
     * 
     */
    @FXML
    TableView<ResponseHeader> responseHeaderTable;
    
    /**
     * 
     */
    @FXML
    Button btnRqAdd;
    
    /**
     * 
     */
    @FXML
    Button btnRqDelete;
    
    /**
     * 
     */
    @FXML 
    Button btnPDAdd;
    
    /**
     * 
     */
    @FXML
    Button btnPDDel;
    
    /**
     * 
     */
    @FXML
    Button btnPDClear;
    
    /**
     * 
     */
    @FXML
    Label statusLabel;
    
    /**
     * 
     */
    @FXML
    Hyperlink lnkJeffPizano; 
    
    /**
     * 
     */
    @FXML
    TextArea txtResponseBody;
    
    /**
     * 
     */
    @FXML
    ProgressBar progBar;
    
    /**
     * 
     */
    @FXML
    TabPane tabsResponse;
    
    @FXML
    CheckMenuItem optFormatJson;
    
    
    /**
     * 
     */
    @FXML
    private void saveRequest()
    {
        if(currentFile == null)
            saveRequestAs();
        
        PersistantRequest dat = new PersistantRequest();
        dat.setHeaders(requestHeaderList);
        dat.setPostData(postParameterList);
        dat.setURL(urlEntry.getText());
        
        try {
            FileOutputStream fout = new FileOutputStream(currentFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(dat);
            oos.close();
            }
        catch (Exception e) { e.printStackTrace(); }
        
        RESTest.stage.setTitle(RESTest.title + " - " + currentFile);
        statusLabel.setText("File Saved: " + currentFile);
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void saveRequestAs()
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Request As...");
        fc.setInitialDirectory(new File(defaultPath));
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Request File (*.request)", "*.request");
        fc.getExtensionFilters().add(extFilter);
        
        try {
            File file = fc.showSaveDialog(null);
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
        fc.setInitialDirectory(new File(defaultPath));
        PersistantRequest pq;
        
        try {
            File file = fc.showOpenDialog(null);
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fin);
            pq = (PersistantRequest) ois.readObject();
            ois.close();
            
            currentFile = file.getPath();
            
            requestHeaderList.clear();
            postParameterList.clear();
            
            urlEntry.setText(pq.getURL());
            requestHeaderList.addAll(pq.getHeaderList());
            postParameterList.addAll(pq.getPostDataList());
            
            resetTabs();
            progBar.setProgress(0);
            RESTest.stage.setTitle(RESTest.title + " - " + currentFile);
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
        requestHeaderList.clear();
        postParameterList.clear();
        requestHeaderList.add(new RequestHeader("User-Agent", "RESTest/" + RESTest.version));
        requestHeaderList.add(new RequestHeader("Date", RESTUtil.getDate()));
        postParameterList.clear();
        RESTest.stage.setTitle(RESTest.title);
        urlEntry.setText("");
        resetTabs();
        progBar.setProgress(0);
        statusLabel.setText("idle...");
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
                .masthead("RESTest " + RESTest.version + "\nAuthor: Jeff Pizano")
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
     * This method is called when the user clicks the "Add" button in the
     * request headers section.
     */
    @FXML
    private void addRequestHeader()
    {
        requestHeaderList.add(requestHeaderList.size(), new RequestHeader("New Header", "Value"));
    }
    
    
    
    /**
     * This method is called when the user clicks the "Delete" button in the
     * request headers section.
     */
    @FXML
    private void deleteRequestHeader()
    {
        requestHeaderList.remove(rqSelectedIndex.get());
    }
    
    
    
    /**
     * This method is called when the user clicks the "Clear" button in the
     * request headers section.
     */
    @FXML
    private void clearRequestHeaders()
    {
        requestHeaderList.clear();
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void addPostData()
    {
        postParameterList.add(postParameterList.size(), new PostParameter("param", "data"));
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void deletePostData()
    {
        postParameterList.remove(pstSelectedIndex.get());
    }
    
    
    
    /**
     * 
     */
    @FXML
    private void clearPostData()
    {
        postParameterList.clear();
    }
    
    
    
    private void resetTabs()
    {
        selectionModel.select(1);
        selectionModel.getSelectedItem().setDisable(true);
        selectionModel.select(0);
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
        methodChoice.getSelectionModel().selectFirst();
        
        requestHeaderList = FXCollections.observableArrayList(new RequestHeader("User-Agent", "RESTest/" + RESTest.version), new RequestHeader("Date", RESTUtil.getDate()));
        postParameterList = FXCollections.observableArrayList();
        responseHeaderList = FXCollections.observableArrayList();
        statusLabel.setText("idle...");
        txtResponseBody.setText("");
        
        selectionModel = tabsResponse.getSelectionModel();
        resetTabs();
        
        defaultPath = RESTUtil.getWorkingPath() + "requests" + File.separatorChar;
        File file = new File(defaultPath);
        file.mkdir();
        
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
        
        requestHeaderTable.setEditable(true);
        requestHeaderTable.setItems(requestHeaderList);
        requestHeaderTable.getColumns().addAll(colHeader, colValue);
        requestHeaderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                rqSelectedIndex.set(requestHeaderList.indexOf(newValue));
            }
        });
    }
    
    
    
    /**
     * This method initializes the TableView for the POST data section.
     * This is where the TableView events are defined and configured.
     */
    private void initPostDataTable()
    {
        postDataTable.setEditable(true);
        
        Callback<TableColumn, TableCell> cellFactory = 
                (TableColumn p) -> new EditingCell();
        
        TableColumn colParam = new TableColumn("Parameter");
        colParam.setMinWidth(200);
        colParam.setCellValueFactory(new PropertyValueFactory<PostParameter, String>("param"));
        colParam.setCellFactory(cellFactory);
        colParam.setOnEditCommit(
            new EventHandler<CellEditEvent<PostParameter, String>>() {
                @Override
                public void handle(CellEditEvent<PostParameter, String> t) {
                    ((PostParameter) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setParam(t.getNewValue());
                }
            }
        );
        
        TableColumn colData = new TableColumn("Data");
        colData.setMinWidth(300);
        colData.setCellValueFactory(new PropertyValueFactory<PostParameter, String>("data"));
        colData.setCellFactory(cellFactory);
        colData.setOnEditCommit(
            new EventHandler<CellEditEvent<PostParameter, String>>() {
                @Override
                public void handle(CellEditEvent<PostParameter, String> t) {
                    ((PostParameter) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setData(t.getNewValue());
                }
            }
        );
        
        postDataTable.setItems(postParameterList);
        postDataTable.getColumns().addAll(colParam, colData);
        postDataTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                pstSelectedIndex.set(postParameterList.indexOf(newValue));
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

        responseHeaderTable.setItems(responseHeaderList);
        responseHeaderTable.getColumns().addAll(colHeader, colValue);
    }
    
    
    
    /**
     * This method is called when the user presses the "Send Request" button.
     */
    @FXML
    private void sendRequest()
    {
        resetTabs();
        statusLabel.setText("Preparing...");
        
        String endpoint = urlEntry.getText();
        if(!endpoint.startsWith("http")) {
            urlEntry.setText("http://" + endpoint);
        }
        
        responseHeaderList.clear();
        txtResponseBody.setText("");
        progBar.setProgress(-1);
        
        Request req = new Request();
        try {
            req.setMethod(methodChoice.getSelectionModel().getSelectedItem().toString());
            req.setHeaderList(requestHeaderList);
            req.setPostData(postParameterList);
            req.setUrl(urlEntry.getText());
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
            progBar.setProgress(1);
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
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        String line;
        String h;
        String httpResponse = null;
        
        if(rsp.responseHeaders != null) {
            for (Map.Entry<String, List<String>> k : rsp.responseHeaders) {
                for (String v : k.getValue()){
                    if((h = k.getKey()) != null) {
                        responseHeaderList.add(new ResponseHeader(h, v));
                        line = h+": "+v+"\n";
                    }
                    else {
                        httpResponse = v;
                    }
                }
            }
        }
        
        if(optFormatJson.isSelected()) {
            try {
                Object tmp = json.fromJson(rsp.responseBody, Object.class);
                txtResponseBody.setText(json.toJson(tmp));
            }
            catch(Exception e) {
                txtResponseBody.setText(rsp.responseBody);
            }
        }
        else {
            txtResponseBody.setText(rsp.responseBody);
        }
        
        
        
        
        
        if(rsp.responseCode > 0) {
            statusLabel.setText(httpResponse);
            selectionModel.select(1);
            selectionModel.getSelectedItem().setDisable(false);
        }
        else {
            selectionModel.select(1);
            selectionModel.getSelectedItem().setDisable(true);
            selectionModel.select(0);
            
            statusLabel.setText("Failed.");
            progBar.setProgress(0);
        }
    }
}
