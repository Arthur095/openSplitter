package control.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import com.github.cliftonlabs.json_simple.JsonException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import control.MainApp;
import control.model.Split;


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
     * Opens a new box to add a game.
     */
    @FXML
    private void handleAddGame() {
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("Adding a game");
    	dialog.setHeaderText(null);
    	dialog.setGraphic(null);
    	dialog.setContentText("Enter a game name:");
    	
		Stage Stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		Stage.getIcons().add(new Image("file:./resources/logo/default.png"));

    	// Traditional way to get the response value.
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    	    try {
				if(mainApp.getInputFile().gameList().contains(result.get())){
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonException e) {
				e.printStackTrace();
			}
    	    HashMap<String, ArrayList<Split>> allGames = mainApp.getInputFile().toHashMap();
    	    allGames.put(result.get(), new ArrayList<Split>());
			mainApp.getInputFile().toJson(allGames);
			MainApp mainApp = new MainApp();
			mainApp.start(this.mainApp.getPrimaryStage());
    	}
    	else {
    		return;
    	}
    }
    
    /**
     * Opens a new box to configure new game and its splits.
     */
    @FXML
    private void handleEditGame() {
    	if(mainApp.getCurrentGame() != null) {
    		ArrayList<Split> editedGame = mainApp.showEditGameDialog();
    		if(editedGame == null) {
    			return;
    		}
			HashMap<String, ArrayList<Split>> allGames = mainApp.getInputFile().toHashMap();
			allGames.get(mainApp.getCurrentGame()).clear();
			allGames.get(mainApp.getCurrentGame()).addAll(editedGame);
			mainApp.getInputFile().toJson(allGames);
			MainApp mainApp = new MainApp();
			mainApp.start(this.mainApp.getPrimaryStage());
    	}
    }
    
    /**
     * Delete the current game from the game list.
     */
    @FXML
    private void handleDeleteGame() {
    	if(mainApp.getCurrentGame() != null) {	
			mainApp.getAlert().setHeaderText("The selected game will be deleted. Are you sure to proceed");
			mainApp.getAlert().setContentText("Click OK to confirm, else click CANCEL.");
			Optional<ButtonType> result = mainApp.getAlert().showAndWait();
			if (result.get() == ButtonType.OK){
				HashMap<String, ArrayList<Split>> allGames = mainApp.getInputFile().toHashMap();
				allGames.remove(mainApp.getCurrentGame());
				mainApp.getInputFile().toJson(allGames);
				MainApp mainApp = new MainApp();
				mainApp.start(this.mainApp.getPrimaryStage());
			} 
			return;
    	}
    }

    /**
     * Saves current Pb & Sob in json.
     */
    @FXML
    private void handleSaveCurr() {
    	HashMap<String, ArrayList<Split>> allGames = mainApp.getInputFile().toHashMap();
    	ArrayList<Split> myGame = new ArrayList<Split>(mainApp.getTableData());
    	myGame.remove(myGame.size()-1);
    	myGame.remove(myGame.size()-1);
    	allGames.get(mainApp.getCurrentGame()).clear();
    	allGames.get(mainApp.getCurrentGame()).addAll(myGame);
    	mainApp.getInputFile().toJson(allGames);
	}

    /**
     * Opens the project Guthub page in default web browser.
     */
    @FXML
    private void handleGithub() {
    	mainApp.getHostServices().showDocument("https://github.com/Arthur095/openSplitter");
    	return;
    }

}