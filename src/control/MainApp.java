package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import json.JsonNotFoundException;
import json.JsonReadWrite;

/**
 * 
 * @author github: Arthur095
 *
 */
public class MainApp extends Application {
	
	/*Attributes*/
	
	/*UI & Dialog boxes*/
    private Stage primaryStage;
    private BorderPane rootLayout;
    private Alert Alert = new Alert(AlertType.CONFIRMATION);
    
    /*Time arrays and visualization list*/
    private ObservableList<Split> tableData = FXCollections.observableArrayList();
	private ArrayList<Double> currentSplitTimes = new ArrayList<Double>();
	private ArrayList<Double> currentSumOfBest = new ArrayList<Double>();
	private ArrayList<Double> currentPersonalBest = new ArrayList<Double>();
	
	/*Files*/
	private JsonReadWrite inputFile;
	private String currentGame;
	private String filePath = Config.FILEPATH;
	
	/*Keybinds array*/
	private HashMap<String, Runnable> Keybinds = new HashMap<String, Runnable>();
    
	/**
     * Empty Constructor, set up alert dialog box and program style sheet.
     */
	public MainApp() {
		// Put one empty Split when opening the program to display tableview.
		tableData.add(new Split());
		// Set Dialog box icon.
		Stage alertStage = (Stage) Alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(Config.DEFAULTURI));
		DialogPane dialogPane = Alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource(Config.STYLESHEET).toExternalForm());
		dialogPane.getStyleClass().add(Config.STYLE);
	}

	/**
	 * Initializes layouts and set layout configurations.
	 * @throws IOException 
	 */
    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Config.APPTITLE);
        
        initRootLayout();
        
        try {
			showCoreOverview();
        }catch (JsonNotFoundException jnfe) {
        	JsonReadWrite.createFile(Config.FILEPATH);
        	MainApp mainApp = new MainApp();
			mainApp.start(primaryStage);
        }
        
        // Blocking Width resizing.
        this.primaryStage.setMaxWidth(this.primaryStage.getWidth());
        this.primaryStage.setMinWidth(this.primaryStage.getWidth());
        
        // Adding app Icon.
        this.primaryStage.getIcons().add(new Image(Config.DEFAULTURI));
        
        // Loading key binding.
        JsonReadWrite combo = new JsonReadWrite(Config.CONFIGPATH);
        primaryStage.getScene().getAccelerators().clear();
        
        try {
        	primaryStage.getScene().getAccelerators().putAll(combo.loadKeybinds(Keybinds));
        }catch(JsonNotFoundException e) {
        	JsonReadWrite.createFile(Config.CONFIGPATH);
        }
    }//start
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Config.ROOTLAYOUT));
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            controller.fillKeybinds();
            
            primaryStage.show();
        } catch (Exception e) {
        	showCriticalError(Config.ROOTLAYOUT, 101);
        }
    }//initRootLayout

    /**
     * Shows the person overview inside the root layout.
     * @throws JsonException 
     * @throws JsonNotFoundException 
     */
    public void showCoreOverview() throws JsonNotFoundException {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(Config.COREOVERVIEW));
            AnchorPane coreOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(coreOverview);
            
            // Give the controller access to the main app.
            CoreOverviewController controller = loader.getController();
     
            // Add Runnable to keybinds array.
            controller.setMainApp(this);
            controller.fillKeybinds();
          
            
            
        } catch (Exception e) {
            showCriticalError(Config.COREOVERVIEW, 102);
        }
    }//showCoreOverview
    
    /**
     * Handles game editing dialog box.
     * @return
     */
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
            
            // Send splits to the controller and set up combo boxes.
            controller.setGameSplits(splits);
            controller.setCombobox();
            
            // Blocking dialog box size and adding icon.
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image(Config.DEFAULTURI));
            
            dialogStage.showAndWait();
            return controller.getGameSplits();
        } catch (JsonNotFoundException jnfe) {
        	JsonReadWrite.createFile(Config.FILEPATH);
        	return null;
        } catch (Exception e) {
        	showCriticalError(Config.EDITGAMEDIALOG, 103);
            return null;
        }
    }//showEditGameDialog
    
    /**
     * Handles keybind edition dialog box.
     * @return HashMap<KeyCombination, Runnable>; the javafx formatted keybind map.
     */
    public HashMap<KeyCombination, Runnable> showEditKeybindsDialog() {
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
            
            // Send to the controller MainApp keybinds and scene accelerators.
            controller.setDialogStage(dialogStage);
            controller.setKeybinds(Keybinds);
            HashMap<KeyCombination, Runnable> kb = new HashMap<KeyCombination, Runnable>(primaryStage.getScene().getAccelerators());
            controller.setAccelerators(kb);
            
            // Set up buttons text of controller according to keybinds name.
            controller.setupButton();
            
            // Blocking dialog box size and adding icon.
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image(Config.DEFAULTURI));
            dialogStage.showAndWait();

            return controller.getAccelerators();
        } catch (Exception e) {
        	showCriticalError(Config.EDITKEYBINDSDIALOG, 104);
            return null;
        }
    }//showEditKeybindsDialog
    
    /**
     * Plays a mp3 or wav file.
     */
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
    }//playAlertSound
    
    /**
     * Shows an alert if files are missing
     */
    private void showCriticalError(String header, int error) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle(Config.FAILTITLE);
    	alert.setContentText(Config.FAILCONTENT);
    	alert.setHeaderText(header);
    	alert.showAndWait();
    	System.exit(error);
    }
    
    /*MAIN*/
    
    /**
     * Main
     */
	public static void main(String[] args) {
        launch(MainApp.class, args);  
    }
	
	/*Getters & Setters*/
	
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
	public HashMap<String, Runnable> getKeybinds() {
		return Keybinds;
	}

}
