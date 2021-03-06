/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 *
 * @author Pizano
 */
public class RESTest extends Application
{
    public final static String version = "0.1.0";
    
    public static Stage stage;
    public static String title = "RESTest " + version;
    
    
    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle(RESTest.title);
        stage.setScene(scene);
        stage.show();
    }



    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}