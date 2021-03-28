package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.github.cliftonlabs.json_simple.JsonException;

import control.model.Split;
import control.view.CoreOverviewController;
import control.view.EditGameController;
import control.view.EditKeybindsController;
import control.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import json.JsonReadWrite;

public class MainApp extends Application {
	
	/*Attributes*/
    private Stage primaryStage;
    private BorderPane rootLayout;
    private Alert Alert = new Alert(AlertType.CONFIRMATION);
    
    private ObservableList<Split> tableData = FXCollections.observableArrayList();
	private ArrayList<Double> currentSplitTimes = new ArrayList<Double>();
	private ArrayList<Double> currentSumOfBest = new ArrayList<Double>();
	private ArrayList<Double> currentPersonalBest = new ArrayList<Double>();
	
	private JsonReadWrite inputFile;
	private String currentGame;
	private String filePath = Config.FILEPATH;
    
	/**
     * Empty Constructor
     */
	public MainApp() {
		//Put one empty Split when opening the program to display tableview
		tableData.add(new Split());
		//Set Dialog box icon.
		Stage alertStage = (Stage) Alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(Config.DEFAULTURI));
		DialogPane dialogPane = Alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource(Config.STYLESHEET).toExternalForm());
		dialogPane.getStyleClass().add(Config.STYLE);
	}

	/**
	 * Initializes layouts and set layout configurations.
	 */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Config.APPTITLE);
        
        initRootLayout();
        
        try {
			showCoreOverview();
		} catch (JsonException e) {
			e.printStackTrace();
		}
        
        this.primaryStage.setMaxWidth(this.primaryStage.getWidth());
        this.primaryStage.setMinWidth(this.primaryStage.getWidth());
        this.primaryStage.getIcons().add(new Image(Config.DEFAULTURI));
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Config.ROOTLAYOUT));
            rootLayout = (BorderPane) loader.load();
            
            //Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     * @throws JsonException 
     */
    public void showCoreOverview() throws JsonException {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Config.COREOVERVIEW));
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
    
    public ArrayList<Split> showEditGameDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Config.EDITGAMEDIALOG));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(Config.EDTITLE);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the stage into the controller.
            EditGameController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            ArrayList<Split> splits = new ArrayList<Split>(inputFile.fromJson(currentGame));
            controller.setGameSplits(splits);
            controller.setCombobox();
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image(Config.DEFAULTURI));
            dialogStage.showAndWait();
            return controller.getGameSplits();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void showEditKeybindsDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Config.EDITKEYBINDSDIALOG));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(Config.KEYTITLE);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the stage into the controller.
            EditKeybindsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image(Config.DEFAULTURI));
            dialogStage.showAndWait();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
    
    public void playAlertSound() {
        try{
   
	    	Media sound = new Media(new File(Config.SOUND).toURI().toString());
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

	public ArrayList<Double> getCurrentSplitTimes() {
		return currentSplitTimes;
	}

	public ArrayList<Double> getCurrentSumOfBest() {
		return currentSumOfBest;
	}

	public ArrayList<Double> getCurrentPersonalBest() {
		return currentPersonalBest;
	}
	
    public JsonReadWrite getInputFile() {
		return inputFile;
	}

	public void setInputFile(JsonReadWrite inputFile) {
		this.inputFile = inputFile;
	}

	public String getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(String currentGame) {
		this.currentGame = currentGame;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Alert getAlert() {
		return Alert;
	}

}
