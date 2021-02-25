package control.view; 

import control.model.Chrono;
import control.MainApp;
import control.model.Split;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import json.JsonReadWrite;

import java.io.IOException;
import java.util.ArrayList;

import com.github.cliftonlabs.json_simple.JsonException;

public class CoreOverviewController {
	
	/*Attributes*/
	private boolean hidden = false;
	private String currentGame;
	private Chrono splitTimer = new Chrono();
	private int splitTableId = 0;
	private ArrayList<Double> currentSplitTimes = new ArrayList<Double>();
	private ArrayList<Double> currentSumOfBest = new ArrayList<Double>();
	private ArrayList<Double> currentPersonalBest = new ArrayList<Double>();
	
	/*Timer attribute*/
	@FXML
	private Label currentTimeSeconds;
	
	/*Labels*/
	@FXML
	private Label gameName;
	
	/*Table attributes*/
	@FXML
    private TableView<Split> splitTable;
    @FXML
    private TableColumn<Split, ImageView> logoColumn;
    @FXML
    private TableColumn<Split, String> splitColumn;
    @FXML
    private TableColumn<Split, String> personalBestColumn;
    @FXML
    private TableColumn<Split, String> sumOfBestColumn;
    @FXML
    private TableColumn<Split, String> timeColumn;
    @FXML
    private TableColumn<Split, String> deltaColumn;
    
    /*ComboBox*/
    @FXML 
    private ComboBox<String> gameBox; 
    
    /* Reference to the main application.*/
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public CoreOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * @throws JsonException 
     * @throws IOException 
     */
    @FXML
    private void initialize() throws IOException, JsonException {
    	JsonReadWrite inputFile = new JsonReadWrite("./resources/json/games.json");
    	//Initialize timer label
    	currentTimeSeconds.setText(splitTimer.formatTime(0.0));
    	//Initialize game combobox
    	gameBox.getItems().addAll(inputFile.gameList());
    	
    	// Initialize the person table with the two columns.
        logoColumn.setCellValueFactory(cellData -> cellData.getValue().logoProperty());
        splitColumn.setCellValueFactory(cellData -> cellData.getValue().splitNameProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        personalBestColumn.setCellValueFactory(cellData -> cellData.getValue().personalBestProperty());  
        sumOfBestColumn.setCellValueFactory(cellData -> cellData.getValue().sumOfBestProperty()); 
        deltaColumn.setCellValueFactory(cellData -> cellData.getValue().deltaProperty()); 
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        splitTable.setItems(mainApp.getTableData());
        
        //Selection examples
        splitTable.getSelectionModel().selectFirst();
    }
    
    /*Buttons*/
    
    /**
     * Called when the user clicks on the Start/Split.
     */
    @FXML
    private void startSplitTimer() {
    	
    	if(currentGame == null) {
    		return;
    	}
    	//Binding timer to label
    	currentTimeSeconds.textProperty().bind(splitTimer.getFullTimer());
    	//If timer exists and is paused resume it
    	if(splitTimer.getTimeline() != null && splitTimer.getTimeline().getStatus().toString().equals("PAUSED")) {
    		splitTimer.getTimeline().play();
    		return;
    	}
    	
    	//If timer does not exist create and start it
    	if(splitTimer.getTimeline() == null) {  
    		currentSplitTimes.clear();
    		splitTable.getSelectionModel().select(splitTableId);
            splitTimer.setTimeline(new Timeline(
                new KeyFrame(Duration.millis(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        Duration duration = ((KeyFrame)t.getSource()).getTime();
                        splitTimer.setTime(splitTimer.getTime().add(duration));
                        splitTimer.getTimeSeconds().set(splitTimer.getTime().toSeconds());
                        splitTimer.getFullTimer().setValue(splitTimer.formatTime(splitTimer.getTimeSeconds().doubleValue()));
                    }
                })
            ));
            splitTimer.getTimeline().setCycleCount(Timeline.INDEFINITE);
            splitTimer.getTimeline().play();
        }
    	//If timer exists and is not paused go to next split.
    	else {
    		//If timer is running save current time in current split and go to next one
    		if(splitTableId < mainApp.getTableData().size()-3) {
    			currentSplitTimes.add(splitTimer.getTimeSeconds().doubleValue());
	    		mainApp.getTableData().get(splitTableId).timeProperty().setValue(splitTimer.formatTime(splitTimer.getTimeSeconds().doubleValue()));
	    		mainApp.getTableData().get(mainApp.getTableData().size()-1).timeProperty().setValue(splitTimer.formatTime(sumTime(currentSplitTimes)));
	    		splitTableId += 1;
	    		splitTable.getSelectionModel().select(splitTableId);
    		}
    		//If last split is reached stop timer, reset it and go to start.
    		else {
    			currentSplitTimes.add(splitTimer.getTimeSeconds().doubleValue());
    			mainApp.getTableData().get(splitTableId).timeProperty().setValue(splitTimer.formatTime(splitTimer.getTimeSeconds().doubleValue()));
    			mainApp.getTableData().get(mainApp.getTableData().size()-1).timeProperty().setValue(splitTimer.formatTime(sumTime(currentSplitTimes)));
    			splitTimer.getTimeline().stop();
    			splitTimer = new Chrono();
    			splitTableId = 0;
    			splitTable.getSelectionModel().select(mainApp.getTableData().size()-1);
    		}
    	}
    }//StartPlitTimer
    
    /**
     * Called when the user clicks on Pause : Pauses timer.
     */
    @FXML
    private void pauseSplitTimer() {
    	if(splitTimer.getTimeline() != null) {
    		splitTimer.getTimeline().pause();
    	}
    	return;
    }//pauseSplitTimer
    
    /**
     * Called when the user clicks on Reset : Reset time and make time table white.
     */
    @FXML
    private void resetSplitTimer() {
    	if(splitTimer.getTimeline() == null) {
    		splitTimer.setTimeline(new Timeline());
    	}
    		splitTimer.getTimeline().stop();
    		splitTableId = 0;
    		splitTable.getSelectionModel().select(splitTableId);
    		for(Split split : mainApp.getTableData()) {
    			split.timeProperty().setValue(null);
    		splitTimer = new Chrono();
    		splitTimer.getFullTimer().setValue(splitTimer.formatTime(splitTimer.getTimeSeconds().doubleValue()));
    		currentTimeSeconds.textProperty().unbind();
    		currentTimeSeconds.setText(splitTimer.formatTime(0.0));
    	}
    }//resetSplitTimer
    
    /**
     * Hide or show the timer label 
     */
    @FXML
    private void hideShowTimer() {
    	if(hidden == false) {
    		currentTimeSeconds.setVisible(false);
    		hidden = true;
    	}
    	else {
    		currentTimeSeconds.setVisible(true);
    		hidden = false;
    	}
    }//hideShowTimer
    
    /**
     * Load the game selected in the combobox bar.
     * @param event
     */
    @FXML
    private void chooseGame(ActionEvent event) {
    	if(!(splitTimer.getTimeline() == null)) {
    		gameBox.getSelectionModel().select(currentGame);
    	}
    	else {
    		currentGame = gameBox.getSelectionModel().getSelectedItem().toString();
			mainApp.getTableData().clear();
			JsonReadWrite reader = new JsonReadWrite("D:\\java_workspace\\SpeedrunTimer\\resources\\json\\games.json");
			mainApp.getTableData().setAll(reader.fromJson(gameBox.getValue().toString()));
			
			//Total column
			mainApp.getTableData().add(new Split());
			mainApp.getTableData().add(new Split("Total"));
    	}
    }//chooseGame
    
    /*Methods*/
    
    private double sumTime(ArrayList<Double> column) {
    	double total = 0.0;
    	double previous = 0.0;
    	for(double time: column) {
    		total += time - previous;
    		previous = time;
    	}
    	return total;
    }//sumTime
}
