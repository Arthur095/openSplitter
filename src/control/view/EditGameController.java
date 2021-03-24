package control.view;

import control.MainApp;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class EditGameController {
	
	private Stage dialogStage;
	private boolean okClicked = false;
	
    // Reference to the main application
    private MainApp mainApp;
	
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
    public boolean isOkClicked() {
        return okClicked;
    }
	
	
	
}
