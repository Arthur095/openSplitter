package control.view; 

import control.model.Chrono;
import control.MainApp;
import control.model.Split;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import json.JsonReadWrite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.github.cliftonlabs.json_simple.JsonException;

public class CoreOverviewController {
	
	/*Attributes*/
	private String filePath = ".\\resources\\json\\games.json";
	private boolean hidden = false;
	private String currentGame;
	private Chrono splitTimer = new Chrono();
	private int splitTableId = 0;
	private ArrayList<Double> currentSplitTimes = new ArrayList<Double>();
	private ArrayList<Double> currentSumOfBest = new ArrayList<Double>();
	private ArrayList<Double> currentPersonalBest = new ArrayList<Double>();
	
	/*Labels*/
	@FXML
	private Label currentTimeSeconds; //Timer
	@FXML
	private Label game;
	
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
    	currentTimeSeconds.setText(Chrono.formatTime(0.0));
    	//Initialize game combobox
    	gameBox.getItems().addAll(inputFile.gameList());
    	//Initialize keybinds.
    	
    	
    	
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
        //splitTable.getSelectionModel().selectFirst();
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
    		splitTable.scrollTo(splitTableId);
            splitTimer.setTimeline(new Timeline(
                new KeyFrame(Duration.millis(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        Duration duration = ((KeyFrame)t.getSource()).getTime();
                        splitTimer.setTime(splitTimer.getTime().add(duration));
                        splitTimer.getTimeSeconds().set(splitTimer.getTime().toSeconds());
                        splitTimer.getFullTimer().setValue(Chrono.formatTime(splitTimer.getTimeSeconds().doubleValue()));
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
    			Double time = splitTimer.getTimeSeconds().doubleValue();
    			currentSplitTimes.add(time);
	    		getSplit().timeProperty().setValue(Chrono.formatTime(time));
	    		mainApp.getTableData().get(mainApp.getTableData().size()-1).timeProperty().setValue(Chrono.formatTime(Chrono.sumTime(currentSplitTimes)));
	    		
	    		checkSumOfBest();
	    		checkDelta();
	    		
	    		splitTableId += 1;
	    		splitTable.getSelectionModel().select(splitTableId);
	    		splitTable.scrollTo(splitTableId-1);
    		}
    		//If last split is reached stop timer, reset it and go to start.
    		else {
    			Double time = splitTimer.getTimeSeconds().doubleValue();
    			currentSplitTimes.add(time);
    			getSplit().timeProperty().setValue(Chrono.formatTime(time));		
    			mainApp.getTableData().get(mainApp.getTableData().size()-1).timeProperty().setValue(Chrono.formatTime(Chrono.sumTime(currentSplitTimes)));
    			
    			checkSumOfBest();
    			checkDelta();
    			
    			splitTimer.getTimeline().stop();
    			
    	    	Double pbtime = currentSplitTimes.get(currentSplitTimes.size()-1);
    	    	if( currentPersonalBest.contains(null) || currentPersonalBest.get(currentPersonalBest.size()-1) > pbtime ) {
    	    		mainApp.playAlertSound();
    	    		checkPbDiag();
    	    	}
    	    	
    			splitTimer = new Chrono();
    			splitTableId = 0;
    			splitTable.getSelectionModel().select(mainApp.getTableData().size()-1);
    			splitTable.scrollTo(mainApp.getTableData().size()-1);
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
    		currentSplitTimes.clear();
    		splitTimer.getTimeline().stop();
    		splitTableId = 0;
    		splitTable.getSelectionModel().select(splitTableId);
    		splitTable.scrollTo(splitTableId);
    		for(Split split : mainApp.getTableData()) {
    			split.timeProperty().setValue(null);
    			split.deltaProperty().setValue(null);
    		}
    		
    		splitTimer = new Chrono();
    		splitTimer.getFullTimer().setValue(Chrono.formatTime(0.0));
    		currentTimeSeconds.textProperty().unbind();
    		currentTimeSeconds.setText(Chrono.formatTime(0.0));
    		
        	deltaColumn.setCellFactory((deltaColumn) -> {
        	    TableCell<Split, String> tableCell = new TableCell<Split, String>() {
        	    	@Override
        	        protected void updateItem(String item, boolean empty) {
        	            super.updateItem(item, empty);
        	        }//updateItem
        	    };//tableCell
        	    return tableCell;
        	});//setCellFactory
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
     * @param game
     */
    @FXML
    private void chooseGame(ActionEvent event) {
    	if(!(splitTimer.getTimeline() == null)) {
    		gameBox.getSelectionModel().select(currentGame);
    	}
    	else {
    		resetSplitTimer();
    		currentGame = gameBox.getSelectionModel().getSelectedItem().toString();
			mainApp.getTableData().clear();
			JsonReadWrite reader = new JsonReadWrite(filePath);
			mainApp.getTableData().setAll(reader.fromJson(gameBox.getValue().toString()));
			
			//Total column
			mainApp.getTableData().add(new Split());
			mainApp.getTableData().add(new Split("Total"));
			
			//Current times
			currentSumOfBest.clear();
			currentSumOfBest.addAll(reader.getGameTimes(currentGame,"sob"));
			currentPersonalBest.clear();
			currentPersonalBest.addAll(reader.getGameTimes(currentGame,"pb"));
			
			//Ram usage check for debugging purpose
			System.gc();
    	}
    }//chooseGame
    
    /*Methods*/
    
    /**
     * Put in currentSumofBest the current split time if better than previous one and update the tableview.
     */
    private void checkSumOfBest() {
    	Double oldTime = currentSumOfBest.get(splitTableId);
    	Double time;
    	if (splitTableId == 0) {
    		time = currentSplitTimes.get(splitTableId);
    	}
    	else {
    		time = currentSplitTimes.get(splitTableId)-currentSplitTimes.get(splitTableId-1);
    	}
    	if( oldTime == null || oldTime > time ) {
    		getSplit().sumOfBestProperty().setValue(Chrono.formatTime(time));
    		currentSumOfBest.set(splitTableId, time);
    		
    	}
    	mainApp.getTableData().get(mainApp.getTableData().size()-1).sumOfBestProperty().setValue(Chrono.formatTime(Chrono.sumTimeSob(currentSumOfBest)));
    }//checkSumOfBest
    
    /**
     * Put all row for personal best in tableview if total is better than previous one.
     */
    private void checkPersonalBest() {
		for(int i = 0; i <= currentSplitTimes.size()-1; i++) {
			mainApp.getTableData().get(i).personalBestProperty().setValue(Chrono.formatTime(currentSplitTimes.get(i)));
		}
		mainApp.getTableData().get(mainApp.getTableData().size()-1).personalBestProperty().setValue(Chrono.formatTime(Chrono.sumTime(currentSplitTimes)));
		currentPersonalBest.clear();
		currentPersonalBest.addAll(currentSplitTimes);
    }//checkPersonalBest
    
    /**
     * Check if PB is ready to be saved even if player press the reset button or change game.
     */
    private void checkPbDiag() {
		Optional<ButtonType> result = mainApp.getPbAlert().showAndWait();
		if (result.get() == ButtonType.OK){
		    checkPersonalBest();
		} 
		return;
    }//checkPbDiag
    
    /**
     * Put time difference between PB and current time with color & signed time.
     */
    private void checkDelta() {
    	if(currentPersonalBest.contains(null)) {
    		return;
    	}
    	if(currentSplitTimes.get(splitTableId) == currentPersonalBest.get(splitTableId)) {
    		mainApp.getTableData().get(splitTableId).deltaProperty().setValue("+00.000s");
    		mainApp.getTableData().get(mainApp.getTableData().size()-1).deltaProperty().setValue("+00.000s");
    		return;
    	}
    	//Per column
    	mainApp.getTableData().get(splitTableId).deltaProperty().setValue(Chrono.formatTimeDelta(currentSplitTimes.get(splitTableId), currentPersonalBest.get(splitTableId)));
    	//Sum column
    	mainApp.getTableData().get(mainApp.getTableData().size()-1).deltaProperty().setValue(Chrono.formatTimeDelta(currentSplitTimes.get(splitTableId), currentPersonalBest.get(splitTableId)));
    	
    	deltaColumn.setCellFactory((deltaColumn) -> {
    	    TableCell<Split, String> tableCell = new TableCell<Split, String>() {
    	    	@Override
    	        protected void updateItem(String item, boolean empty) {
    	            super.updateItem(item, empty);
                    if (item != null) {
                        this.setTextFill(Color.GREEN);
                        // Get fancy and change color based on data
                        if(item.contains("+")) 
                            this.setTextFill(Color.RED);
                        setText(item);
                    }
    	        }//updateItem
    	    };//tableCell
    	    return tableCell;
    	});//setCellFactory
    }//checkDelta
    
    /*Getter & Setters*/
    
    private Split getSplit() {
    	return mainApp.getTableData().get(splitTableId);
    }
}
