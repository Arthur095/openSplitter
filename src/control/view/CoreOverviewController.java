package control.view; 

import control.model.Chrono;
import control.Config;
import control.MainApp;
import control.model.Split;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import json.JsonNotFoundException;
import json.JsonReadWrite;
import java.io.IOException;
import java.util.Optional;

import com.github.cliftonlabs.json_simple.JsonException;

/**
 * 
 * Always visible UI Pane controller, containing tableview, buttons and timer string.
 * Handles buttons actions and tableview changes.
 *
 */
public class CoreOverviewController {

	/*Attributes*/
	private Chrono splitTimer = new Chrono();
	private int splitTableId = 0;
	
	/*Labels*/
	@FXML
	private Label currentTimeSeconds; //Timer
	
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
    private void initialize(){
    	//Initializes timer label.
    	currentTimeSeconds.setText(Chrono.formatTime(0.0));
    	
    	// Initializes the splits table.
        logoColumn.setCellValueFactory(cellData -> cellData.getValue().logoProperty());
        splitColumn.setCellValueFactory(cellData -> cellData.getValue().splitNameProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        personalBestColumn.setCellValueFactory(cellData -> cellData.getValue().personalBestProperty());  
        sumOfBestColumn.setCellValueFactory(cellData -> cellData.getValue().sumOfBestProperty()); 
        deltaColumn.setCellValueFactory(cellData -> cellData.getValue().deltaProperty()); 
    }//initialize

    /**
     * Is called by the main application to give a reference back to itself and set attributes from mainapp data.
     * 
     * @param mainApp
     * @throws JsonException 
     * @throws IOException 
     */
    public void setMainApp(MainApp mainApp) throws JsonNotFoundException {
        this.mainApp = mainApp;
        // Add observable list data to the table
        splitTable.setItems(mainApp.getTableData());
        //Initialize inputfile 
        JsonReadWrite reader = new JsonReadWrite(mainApp.getFilePath());
        this.mainApp.setInputFile(reader);
    	//Initialize game combobox
    	gameBox.getItems().addAll(reader.gameList());
    }//setMainApp
    
    /**
     * Fill mainapp keybinds map with Runnable of this controller methods.
     */
    public void fillKeybinds() {
    	mainApp.getKeybinds().put(Config.START, new Runnable(){@Override public void run() {startSplitTimer();}});
    	mainApp.getKeybinds().put(Config.PAUSE, new Runnable(){@Override public void run() {pauseSplitTimer();}});
    	mainApp.getKeybinds().put(Config.RESET, new Runnable(){@Override public void run() {resetSplitTimer();}});
    	mainApp.getKeybinds().put(Config.HIDE, new Runnable(){@Override public void run() {hideShowTimer();}});
    }//fillKeybinds
    
    /*Buttons*/
    
    /**
     * Called when the user clicks on the Start/Split.
     */
    @FXML
    private void startSplitTimer() {
    	
    	if(mainApp.getCurrentGame() == null || mainApp.getTableData().size()<3) {
    		return;
    	}
    	//Binding timer to label
    	currentTimeSeconds.textProperty().bind(splitTimer.getFullTimer());
    	//If timer exists and is paused resume it
    	if(splitTimer.getTimeline() != null && splitTimer.getTimeline().getStatus().toString().equals(Config.PAUSED)) {
    		splitTimer.getTimeline().play();
    		return;
    	}
    	
    	//If timer does not exist create and start it
    	if(splitTimer.getTimeline() == null) { 
    		
    		mainApp.getCurrentSplitTimes().clear();
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
    			mainApp.getCurrentSplitTimes().add(time);
	    		getSplit().timeProperty().setValue(Chrono.formatTime(time));
	    		mainApp.getTableData().get(mainApp.getTableData().size()-1).timeProperty().setValue(Chrono.formatTime(Chrono.sumTime(mainApp.getCurrentSplitTimes())));
	    		
	    		checkSumOfBest();
	    		checkDelta();
	    		
	    		splitTableId += 1;
	    		splitTable.getSelectionModel().select(splitTableId);
	    		splitTable.scrollTo(splitTableId-1);
    		}
    		//If last split is reached stop timer, reset it and go to start.
    		else {
    			Double time = splitTimer.getTimeSeconds().doubleValue();
    			mainApp.getCurrentSplitTimes().add(time);
    			getSplit().timeProperty().setValue(Chrono.formatTime(time));		
    			mainApp.getTableData().get(mainApp.getTableData().size()-1).timeProperty().setValue(Chrono.formatTime(Chrono.sumTime(mainApp.getCurrentSplitTimes())));
    			
    			checkSumOfBest();
    			checkDelta();
    			
    			splitTimer.getTimeline().stop();
    			
    	    	Double pbtime = mainApp.getCurrentSplitTimes().get(mainApp.getCurrentSplitTimes().size()-1);
    	    	if( mainApp.getCurrentPersonalBest().contains(null) || mainApp.getCurrentPersonalBest().size() == 0 || mainApp.getCurrentPersonalBest().get(mainApp.getCurrentPersonalBest().size()-1) > pbtime ) {
    	    		mainApp.playAlertSound();
    	    		checkPbDiag();
    	    	}
    	    	
    			splitTimer = new Chrono();
    			splitTableId = 0;
    			splitTable.getSelectionModel().select(mainApp.getTableData().size()-1);
    			splitTable.scrollTo(mainApp.getTableData().size()-1);
    		}
    	}
    }//StartSplitTimer
    
    /**
     * Called when the user clicks on Pause : Pause timer.
     */
    @FXML
    private void pauseSplitTimer() {
    	if(splitTimer.getTimeline() != null) {
    		splitTimer.getTimeline().pause();
    	}
    	return;
    }//pauseSplitTimer
    
    /**
     * Called when the user clicks on Reset : Reset time and makes time table column empty.
     */
    @FXML
    private void resetSplitTimer() {
    	
    	if(splitTimer.getTimeline() == null) {
    		splitTimer.setTimeline(new Timeline());
    	}
    		mainApp.getCurrentSplitTimes().clear();
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
    		
    		//Set delta column cell label color back to normal.
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
     * Hide or show the timer label, on/off button.
     */
    @FXML
    private void hideShowTimer() {
    	if(currentTimeSeconds.isVisible()) {
    		currentTimeSeconds.setVisible(false);
    	}
    	else {
    		currentTimeSeconds.setVisible(true);
    	}
    }//hideShowTimer
    
    /**
     * Load the game selected in the combobox bar.
     * @param game
     */
    @FXML
    private void chooseGame(ActionEvent event) throws JsonNotFoundException {
    	if(!(splitTimer.getTimeline() == null)) {
    		gameBox.getSelectionModel().select(mainApp.getCurrentGame());
    	}
    	else {
    		resetSplitTimer();
    		mainApp.setCurrentGame(gameBox.getSelectionModel().getSelectedItem().toString());
			mainApp.getTableData().clear();
			JsonReadWrite reader = mainApp.getInputFile();
			mainApp.getTableData().setAll(reader.fromJson(gameBox.getValue().toString()));
			
			
			//Total column
			mainApp.getTableData().add(new Split());
			mainApp.getTableData().add(new Split(Config.TOTAL));
			
			//Current times
			mainApp.getCurrentSumOfBest().clear();
			mainApp.getCurrentSumOfBest().addAll(reader.getGameTimes(mainApp.getCurrentGame(), Config.SOB));
			mainApp.getCurrentPersonalBest().clear();
			mainApp.getCurrentPersonalBest().addAll(reader.getGameTimes(mainApp.getCurrentGame(), Config.PB));
			mainApp.getTableData().get(mainApp.getTableData().size()-1).sumOfBestProperty().setValue(Chrono.formatTime(Chrono.sumTimeSob(mainApp.getCurrentSumOfBest())));
			mainApp.getTableData().get(mainApp.getTableData().size()-1).personalBestProperty().setValue(Chrono.formatTime(Chrono.sumTime(mainApp.getCurrentPersonalBest())));
    	}
    }//chooseGame
    
    /*Methods*/
    
    /**
     * Put in mainApp.getCurrentSumOfBest() the current split time if better than previous one and update the tableview.
     */
    private void checkSumOfBest() {
    	Double oldTime = mainApp.getCurrentSumOfBest().get(splitTableId);
    	Double time;
    	if (splitTableId == 0) {
    		time = mainApp.getCurrentSplitTimes().get(splitTableId);
    	}
    	else {
    		time = mainApp.getCurrentSplitTimes().get(splitTableId)-mainApp.getCurrentSplitTimes().get(splitTableId-1);
    	}
    	if( oldTime == null || oldTime > time ) {
    		getSplit().sumOfBestProperty().setValue(Chrono.formatTime(time));
    		mainApp.getCurrentSumOfBest().set(splitTableId, time);
    		
    	}
    	mainApp.getTableData().get(mainApp.getTableData().size()-1).sumOfBestProperty().setValue(Chrono.formatTime(Chrono.sumTimeSob(mainApp.getCurrentSumOfBest())));
    }//checkSumOfBest
    
    /**
     * Put all row for personal best in tableview if total is better than previous one.
     */
    private void checkPersonalBest() {
		for(int i = 0; i <= mainApp.getCurrentSplitTimes().size()-1; i++) {
			mainApp.getTableData().get(i).personalBestProperty().setValue(Chrono.formatTime(mainApp.getCurrentSplitTimes().get(i)));
		}
		mainApp.getTableData().get(mainApp.getTableData().size()-1).personalBestProperty().setValue(Chrono.formatTime(Chrono.sumTime(mainApp.getCurrentSplitTimes())));
		mainApp.getCurrentPersonalBest().clear();
		mainApp.getCurrentPersonalBest().addAll(mainApp.getCurrentSplitTimes());
    }//checkPersonalBest
    
    /**
     * Check if PB is ready to be saved. If true, opens alert dialog box to confirm PB saving.
     */
    private void checkPbDiag() {
		mainApp.getAlert().setHeaderText(Config.PBHEADER);
		mainApp.getAlert().setContentText(Config.PBCONTENT);
		Optional<ButtonType> result = mainApp.getAlert().showAndWait();
		if (result.get() == ButtonType.OK){
		    checkPersonalBest();
		} 
		return;
    }//checkPbDiag
    
    
    /**
     * Put time difference between PB and current time with color & signed time.
     */
    private void checkDelta() {
    	if(mainApp.getCurrentPersonalBest().contains(null) || mainApp.getCurrentPersonalBest().size() == 0) {
    		return;
    	}
    	if(mainApp.getCurrentSplitTimes().get(splitTableId) == mainApp.getCurrentPersonalBest().get(splitTableId)) {
    		getSplit().deltaProperty().setValue(Config.ZERODELTA);
    		mainApp.getTableData().get(mainApp.getTableData().size()-1).deltaProperty().setValue(Config.ZERODELTA);
    		return;
    	}
    	// Per column
    	getSplit().deltaProperty().setValue(Chrono.formatTime(mainApp.getCurrentSplitTimes().get(splitTableId), mainApp.getCurrentPersonalBest().get(splitTableId)));
    	// Sum column
    	mainApp.getTableData().get(mainApp.getTableData().size()-1).deltaProperty().setValue(Chrono.formatTime(mainApp.getCurrentSplitTimes().get(splitTableId), mainApp.getCurrentPersonalBest().get(splitTableId)));
    	// Set color for delta column cell label.
    	deltaColumn.setCellFactory((deltaColumn) -> {
    	    TableCell<Split, String> tableCell = new TableCell<Split, String>() {
    	    	@Override
    	        protected void updateItem(String item, boolean empty) {
    	            super.updateItem(item, empty);
                    if (item != null) {
                        this.setTextFill(Color.GREEN);
                        // Change color based on data
                        if(item.contains(Config.PLUS)) 
                            this.setTextFill(Color.RED);
                        setText(item);
                    }
    	        }//updateItem
    	    };//tableCell
    	    return tableCell;
    	});//setCellFactory
    }//checkDelta
    
    /*Getter & Setters*/
    
    // Most used method pattern reduced to improve readability.
    private Split getSplit() {
    	return mainApp.getTableData().get(splitTableId);
    }
}
