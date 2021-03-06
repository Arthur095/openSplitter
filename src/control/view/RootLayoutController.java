package control.view;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;


import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;

import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import json.JsonNotFoundException;
import json.JsonReadWrite;
import control.Config;
import control.MainApp;
import control.model.Split;

/**
 * 
 * MenuBar options UI controller. Handles dialog and alert box opening, adding game, deleting game, saving current game data and deleting current game specific data.
 * Does not have attribute.
 */
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
     * Fill mainapp keybinds map with Runnable of this controller methods.
     */
    public void fillKeybinds() {
    	mainApp.getKeybinds().put(Config.SAVE, new Runnable(){@Override public void run() {handleSaveCurr();}});
    }//fillKeybinds
    
    /**
     * Opens a dialog box to add a free split game and save it to json file if confirmed.
     * @throws IOException 
     * @throws JsonNotFoundException 
     */
    @FXML
    private void handleAddGame() throws IOException, JsonNotFoundException{
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

		
    	// Check if game already exists.
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    		Map<String, ArrayList<Split>> allGames = new TreeMap<String, ArrayList<Split>>();
    		try {
				if(mainApp.getInputFile().gameList().contains(result.get())){
					return;
				}
			    allGames = mainApp.getInputFile().toHashMap();
			    allGames.put(result.get(), new ArrayList<Split>());
				mainApp.getInputFile().toJson(allGames);
    		} catch (JsonNotFoundException jnfe) {
            	JsonReadWrite.createFile(Config.FILEPATH);
            	allGames.put(result.get(), new ArrayList<Split>());
				mainApp.getInputFile().toJson(allGames);
    		}
			MainApp mainApp = new MainApp();
			mainApp.start(this.mainApp.getPrimaryStage());
    	}
		else {
			return;
		}
    }//handleAddGame
    
    /**
     * Opens a dialog box to configure a game and its splits and save changes to json file if confirmed.
     * @throws IOException 
     * @throws JsonNotFoundException 
     */
    @FXML
    private void handleEditGame() throws IOException, JsonNotFoundException {
    	if(mainApp.getCurrentGame() != null) {
    		ArrayList<Split> editedGame = mainApp.showEditGameDialog();
    		if(editedGame == null) {
    			return;
    		}
			HashMap<String, ArrayList<Split>> allGames = new HashMap<String, ArrayList<Split>>();
			try {
				allGames = mainApp.getInputFile().toHashMap();
			} catch (JsonNotFoundException e) {
				JsonReadWrite.createFile(Config.FILEPATH);
				MainApp mainApp = new MainApp();
				mainApp.start(this.mainApp.getPrimaryStage());
				return;
			}
			allGames.get(mainApp.getCurrentGame()).clear();
			allGames.get(mainApp.getCurrentGame()).addAll(editedGame);
			mainApp.getInputFile().toJson(allGames);
			MainApp mainApp = new MainApp();
			mainApp.start(this.mainApp.getPrimaryStage());
    	}
    }//handleEditGame
    
    /**
     * Delete the current game from the game list and save changes to json file if confirmed.
     * @throws IOException 
     * @throws JsonNotFoundException 
     */
    @FXML
    private void handleDeleteGame() throws IOException, JsonNotFoundException {
    	if(mainApp.getCurrentGame() != null) {
    		mainApp.getAlert().setTitle(Config.DELTITLE);
			mainApp.getAlert().setHeaderText(Config.DELHEADER);
			mainApp.getAlert().setContentText(Config.DELCONTENT);
			Optional<ButtonType> result = mainApp.getAlert().showAndWait();
			if (result.get() == ButtonType.OK){
				HashMap<String, ArrayList<Split>> allGames = new HashMap<String, ArrayList<Split>>();
				try {
					allGames = mainApp.getInputFile().toHashMap();
				} catch (JsonNotFoundException jnfe) {
					JsonReadWrite.createFile(Config.FILEPATH);
					MainApp mainApp = new MainApp();
					mainApp.start(this.mainApp.getPrimaryStage());
					return;
				}
				allGames.remove(mainApp.getCurrentGame());
				mainApp.getInputFile().toJson(allGames);
				MainApp mainApp = new MainApp();
				mainApp.start(this.mainApp.getPrimaryStage());
			} 
			return;
    	}
    }//handleDeleteGame

    /**
     * Saves current Pb & Sob in json.
     * @throws JsonNotFoundException 
     */
    @FXML
    private void handleSaveCurr(){
    	if(mainApp.getCurrentGame() == null) {
    		return;
    	}
    	HashMap<String, ArrayList<Split>> allGames = new HashMap<String, ArrayList<Split>>();
    	try {
    		allGames = mainApp.getInputFile().toHashMap();
    	}catch(JsonNotFoundException jnfe) {
        	JsonReadWrite.createFile(Config.FILEPATH);
    	}
    	ArrayList<Split> myGame = new ArrayList<Split>(mainApp.getTableData());
    	myGame.remove(myGame.size()-1);
    	myGame.remove(myGame.size()-1);
    	if(allGames.size() > 0) {
	    	allGames.get(mainApp.getCurrentGame()).clear();
    		allGames.get(mainApp.getCurrentGame()).addAll(myGame);
    	}
    	else{
    		allGames.put(mainApp.getCurrentGame(), myGame);
    	}
    	try {
    		mainApp.getInputFile().toJson(allGames);
    	}
    	catch (JsonNotFoundException jnfe){
    		JsonReadWrite.createFile(Config.FILEPATH);
    		handleSaveCurr();
    	}
	}//handleSaveCurr
    
    /**
     * Opens the keybinds dialog box and save changes to json file if confirmed.
     */
    @FXML
    private void handleKeybinds() {
		HashMap<KeyCombination,Runnable> accelerators = mainApp.showEditKeybindsDialog();
		if(accelerators != null) {
			mainApp.getPrimaryStage().getScene().getAccelerators().clear();
			mainApp.getPrimaryStage().getScene().getAccelerators().putAll(accelerators);
			//Saving key binding in config.json
			JsonReadWrite combo = new JsonReadWrite(Config.CONFIGPATH);
			HashMap<String, KeyCombination> convertedMap = convertKeyMap(mainApp.getKeybinds(), accelerators);
			try {
				combo.saveKeybinds(convertedMap);
			} catch (JsonNotFoundException jnfe) {
				JsonReadWrite.createFile(Config.CONFIGPATH);
				try {
					combo.saveKeybinds(convertedMap);
				} catch (JsonNotFoundException e) {
					e.printStackTrace();
				}	
			}
		}
    }//handleKeybinds

    /*
     * Delete current game PB value in every split.
     */
    @FXML
    private void handleDeletePB(){
    	if(mainApp.getCurrentGame() != null) {
    		mainApp.getAlert().setTitle(Config.DELPBTITLE);
			mainApp.getAlert().setHeaderText(Config.DELPBHEADER);
			mainApp.getAlert().setContentText(Config.DELPBCONTENT);
			Optional<ButtonType> result = mainApp.getAlert().showAndWait();
			if (result.get() == ButtonType.OK){
	    	mainApp.getCurrentPersonalBest().clear();
		    	for(Split split : mainApp.getTableData()) {
		    		split.personalBestProperty().setValue(null);
		    	}
			}
    	}
    }//handleDeletePB
    
    /*
     * Delete current game SOB in every split.
     */
    @FXML
    private void handleDeleteSOB(){
    	if(mainApp.getCurrentGame() != null) {
    		mainApp.getAlert().setTitle(Config.DELSOBTITLE);
			mainApp.getAlert().setHeaderText(Config.DELSOBHEADER);
			mainApp.getAlert().setContentText(Config.DELSOBCONTENT);
			Optional<ButtonType> result = mainApp.getAlert().showAndWait();
			if (result.get() == ButtonType.OK){
				mainApp.getCurrentSumOfBest().replaceAll( t -> !(Objects.isNull(t))? null : t );
		    	for(Split split : mainApp.getTableData()) {
		    		split.sumOfBestProperty().setValue(null);
		    	}
			}
    	}
    }//handleDeleteSOB
    
    /**
     * Opens the project Github page in default web browser.
     */
    @FXML
    private void handleGithub() {
    	mainApp.getHostServices().showDocument(Config.GITHUB);
    	return;
    }//handleGithub
    
    /*
     * Converts accelerators map to a save-able map.
     */
    private HashMap<String, KeyCombination> convertKeyMap(HashMap<String, Runnable> keybinds, HashMap<KeyCombination, Runnable> accelerators){
    	HashMap<String, KeyCombination> newMap = new HashMap<String, KeyCombination>();
    	for(Map.Entry<KeyCombination, Runnable> value : accelerators.entrySet()) {
    		int indexVal = Arrays.asList(keybinds.values().toArray()).indexOf(value.getValue());
    		newMap.put((String) Arrays.asList(keybinds.keySet().toArray()).get(indexVal), value.getKey());
    	}
    	return newMap;
    }//convertKeyMap

}