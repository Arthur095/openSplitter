package control.view; 

import control.model.Chrono;
import com.sun.tools.javac.Main;
import control.MainApp;
import control.model.Split;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;

public class CoreOverviewController {
	
	/*Attributes*/
	Chrono splitTimer = new Chrono();
	private int splitTableId = 0;
	
	/*Timer attribute*/
	@FXML
	private Label currentTimeSeconds;
	
	/*Table attributes*/
	@FXML
    private TableView<Split> splitTable;
    @FXML
    private TableColumn<Split, String> logoColumn;
    @FXML
    private TableColumn<Split, String> splitColumn;
    @FXML
    private TableColumn<Split, String> timeColumn;
    @FXML
    private TableColumn<Split, Double> bestTimeColumn;
    
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
        logoColumn.setCellValueFactory(cellData -> cellData.getValue().splitProperty());
        splitColumn.setCellValueFactory(cellData -> cellData.getValue().logoProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        bestTimeColumn.setCellValueFactory(cellData -> cellData.getValue().bestTimeProperty().asObject());     
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
        splitTable.getSelectionModel().selectNext();
        splitTable.getSelectionModel().select(mainApp.getTableData().get(0));
    }
    
    /*Buttons*/
    
    /**
     * Called when the user clicks on the Start/Split
     */
    @FXML
    private void startSplitTimer() {	
    	currentTimeSeconds.textProperty().bind(splitTimer.getFullTimer());
    	
    	if (splitTimer.getTimeline() == null) {  
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
    	else {
    	
    		if(splitTableId < mainApp.getTableData().size()-1) {
	    		mainApp.getTableData().get(splitTableId).timeProperty().setValue(splitTimer.formatTime(splitTimer.getTimeSeconds().doubleValue()));
	    		splitTableId += 1;
	    		splitTable.getSelectionModel().select(splitTableId);
	    		splitTable.setItems(mainApp.getTableData());
    		}
    		else {
    			mainApp.getTableData().get(splitTableId).timeProperty().setValue(splitTimer.formatTime(splitTimer.getTimeSeconds().doubleValue()));
    			splitTimer.getTimeline().stop();
    			splitTimer = new Chrono();
    			splitTableId = 0;
    			splitTable.getSelectionModel().select(splitTableId);
    		}
    	}
    }//StartPlitTimer
    
}
