package control;

import java.io.File;
import java.io.IOException;

import control.model.Split;
import control.view.CoreOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import json.JsonReadWrite;

public class MainApp extends Application {
	
	/*Attributes*/
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Split> tableData = FXCollections.observableArrayList();
    
	private Alert PbAlert = new Alert(AlertType.CONFIRMATION);
	
    /**
     * Empty Constructor
     */
	public MainApp() {
		//Put one empty Split when opening the program to display tableview
		tableData.add(new Split());
		//Set Dialog box icon.
		PbAlert.setHeaderText("You beat your PB ! Do you want to save your time");
		PbAlert.setContentText("Click ok to save your PB, else click cancel.");
		Stage stage = (Stage) PbAlert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:./resources/logo/default.png"));
	}

	/**
	 * Initializes layouts and set layout configurations.
	 */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("openSplitter v1.0.0");

        initRootLayout();
        showCoreOverview();
        
        this.primaryStage.setMaxWidth(this.primaryStage.getWidth());
        this.primaryStage.setMinWidth(this.primaryStage.getWidth());
        this.primaryStage.getIcons().add(new Image("file:./resources/logo/default.png"));
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            //Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showCoreOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CoreOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
            
            // Give the controller access to the main app.
            CoreOverviewController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void playAlertSound() {
        try{
   
	    	Media sound = new Media(new File("resources/sound/alert_sound.mp3").toURI().toString());
	    	if (sound != null){
		    	MediaPlayer alertSound = new MediaPlayer(sound);
		    	alertSound.play();
	    	}
        }
        catch(Exception ex){
        	return;
        }
    }
    
    /**
     * 
     * Main
     */
	public static void main(String[] args) {
        launch(args);  
    }
	
	
	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Returns the game splits in tableview.
	 * @return
	 */
    public ObservableList<Split> getTableData() {
		return tableData;
	}

	public Alert getPbAlert() {
		return PbAlert;
	}

}
