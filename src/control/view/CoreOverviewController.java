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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.ArrayList;

public class CoreOverviewController {
	
	/*Attributes*/
	private Chrono splitTimer = new Chrono();
	private int splitTableId = 0;
	private ArrayList<Double> currentSplitTimes = new ArrayList<Double>();
	
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
    private TableColumn<Split, String> timeColumn;
    @FXML
    private TableColumn<Split, String> bestTimeColumn;
    
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
     */
    @FXML
    private void initialize() {
    	// Initialize the person table with the two columns.
        logoColumn.setCellValueFactory(cellData -> cellData.getValue().logoProperty());
        splitColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        bestTimeColumn.setCellValueFactory(cellData -> cellData.getValue().bestTimeProperty());     
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
    			splitTable.getSelectionModel().select(splitTableId);
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
    
    
    
    /*Methods*/
    
    private double sumTime(ArrayList<Double> column) {
    	double total = 0.0;
    	double previous = 0.0;
    	for(double time: column) {
    		total += time - previous;
    		previous = time;
    	}
    	return total;
    }
}
