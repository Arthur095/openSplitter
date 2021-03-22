package control.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import control.MainApp;


public class RootLayoutController {

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
     * Opens a new box to configure new game and its splits.
     */
    @FXML
    private void handleAddGame() {

    }
    
    /**
     * Delete the current game from the game list.
     */
    @FXML
    private void handleDeleteGame() {

    }

    /**
     * Saves current Pb & Sob in json.
     */
    @FXML
    private void handleSaveCurr() {

	}

    /**
     * Opens the project Guthub page in default web browser.
     */
    @FXML
    private void handleGithub() {

    }

}