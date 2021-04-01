package control.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import com.github.cliftonlabs.json_simple.JsonException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import json.JsonReadWrite;
import control.Config;
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

    public void fillKeybinds() {
    	mainApp.getKeybinds().put(Config.SAVE, new Runnable(){@Override public void run() {handleSaveCurr();}});
    }
    
    /**
     * Opens a new box to add a game.
     */
    @FXML
    private void handleAddGame() {
    	TextInputDialog dialog = new TextInputDialog();
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource(Config.PSTYLESHEET).toExternalForm());
		dialogPane.getStyleClass().add(Config.STYLE);
    	dialog.setTitle(Config.ADDTITLE);
    	dialog.setHeaderText(null);
    	dialog.setGraphic(null);
    	dialog.setContentText(Config.ADDCONTENT);
    	
		Stage Stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		Stage.getIcons().add(new Image(Config.ICON));

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
    	    Map<String, ArrayList<Split>> allGames = new TreeMap<String, ArrayList<Split>>(mainApp.getInputFile().toHashMap());
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
    		mainApp.getAlert().setTitle(Config.DELTITLE);
			mainApp.getAlert().setHeaderText(Config.DELHEADER);
			mainApp.getAlert().setContentText(Config.DELCONTENT);
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
    	if(mainApp.getCurrentGame() == null) {
    		return;
    	}
    	HashMap<String, ArrayList<Split>> allGames = mainApp.getInputFile().toHashMap();
    	ArrayList<Split> myGame = new ArrayList<Split>(mainApp.getTableData());
    	myGame.remove(myGame.size()-1);
    	myGame.remove(myGame.size()-1);
    	allGames.get(mainApp.getCurrentGame()).clear();
    	allGames.get(mainApp.getCurrentGame()).addAll(myGame);
    	mainApp.getInputFile().toJson(allGames);
	}
    
    /**
     * Opens the keybinds dialog box.
     */
    @FXML
    private void handleKeybinds() {
		HashMap<KeyCombination,Runnable> accelerators = mainApp.showEditKeybindsDialog();
		if(accelerators != null) {
			mainApp.getPrimaryStage().getScene().getAccelerators().clear();
			mainApp.getPrimaryStage().getScene().getAccelerators().putAll(accelerators);
			//Saving key binding in config.json
			JsonReadWrite combo = new JsonReadWrite(Config.CONFIGPATH);
			combo.saveKeybinds(convertKeyMap(mainApp.getKeybinds(), accelerators));
		}
    }
    
    private HashMap<String, KeyCombination> convertKeyMap(HashMap<String, Runnable> keybinds, HashMap<KeyCombination, Runnable> accelerators){
    	HashMap<String, KeyCombination> newMap = new HashMap<String, KeyCombination>();
    	for(Map.Entry<KeyCombination, Runnable> value : accelerators.entrySet()) {
    		int indexVal = Arrays.asList(keybinds.values().toArray()).indexOf(value.getValue());
    		newMap.put((String) Arrays.asList(keybinds.keySet().toArray()).get(indexVal), value.getKey());
    	}
    	return newMap;
    }

    /**
     * Opens the project Guthub page in default web browser.
     */
    @FXML
    private void handleGithub() {
    	mainApp.getHostServices().showDocument(Config.GITHUB);
    	return;
    }

}