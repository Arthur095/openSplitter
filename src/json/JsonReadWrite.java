package json;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import control.Config;
import control.model.Chrono;
import control.model.Split;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;


public class JsonReadWrite {
	
	private String jsonPath;
	
	public JsonReadWrite(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
	// Extraction des segments pour un jeu choisi par l’utilisateur pour remplir le tableview.
	public ObservableList<Split> fromJson (String gameName) {
		ObservableList<Split> game = FXCollections.observableArrayList();
		try
		{ 
            JsonObject runSet = (JsonObject) Jsoner.deserialize(new FileReader(jsonPath));            
            JsonArray splits = (JsonArray) runSet.get(gameName);
            for (Object split : splits.toArray()) {
            	JsonObject split_features = (JsonObject) split;
            	String sob = null;
            	String pb = null;
            	if (!(split_features.get(Config.SOB).equals(Config.NULL))) {
            		sob = Chrono.formatTime(Double.parseDouble((String) split_features.get(Config.SOB)));
            	}
            	if (!(split_features.get(Config.PB).equals(Config.NULL))) {
            		pb = Chrono.formatTime(Double.parseDouble((String) split_features.get(Config.PB)));
            	}
            	Split Segment = new Split(split_features.get(Config.NAME).toString(),split_features.get(Config.LOGO).toString(), pb, sob);
            	game.add(Segment);
            	}
		}
		catch(FileNotFoundException fe)
        {
            fe.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		return game;
    }
	
	public ArrayList<Double> getGameTimes(String gameName, String row){
		ArrayList<Double> times = new ArrayList<Double>();
		try
		{ 
            JsonObject runSet = (JsonObject) Jsoner.deserialize(new FileReader(jsonPath));            
            JsonArray splits = (JsonArray) runSet.get(gameName);
            for (Object split : splits.toArray()) {
            	JsonObject split_features = (JsonObject) split;
            	Double time = null;
            	if (!(split_features.get(row).equals(Config.NULL))) {
            		times.add(Double.parseDouble((String) split_features.get(row)));
            		
            	}
            	else {
            		times.add(time);
            	}
            }
            
		}
		catch(FileNotFoundException fe)
        {
            fe.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		return times;
	}
	
	// Fournit la liste des jeux stockés dans le json
	public ArrayList<String> gameList() throws IOException, JsonException {
		ArrayList<String> allGames = new ArrayList<String>();
		try {
			JsonObject runSet = (JsonObject) Jsoner.deserialize(new FileReader(jsonPath)); 
			for (Object game : runSet.keySet().toArray()) {
				allGames.add(game.toString());
			}
		} catch (IOException | JsonException e) {
			((Throwable) e).printStackTrace();
		}
		return allGames;
		}
	
	
	// Stocker le json dans un HashMap
	public HashMap<String,ArrayList<Split>> toHashMap() {
		HashMap<String,ArrayList<Split>> gameData = new HashMap<String,ArrayList<Split>>();
		try {
			for(String gameName : gameList()) {
				ArrayList<Split> game = new ArrayList<Split>();
				game.addAll(fromJson(gameName));
				gameData.put(gameName, game);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonException e) {
			e.printStackTrace();
		}
		return gameData;
	}
	

	// Écrire un nouveau jeu avec ses segments dans le json
	@SuppressWarnings("unchecked")
	public void toJson(Map<String, ArrayList<Split>> gameData) {
		
		JsonObject jsonGameName = new JsonObject();
		for (String key : gameData.keySet()) {
			//System.out.println(key);
			JsonArray jsonKey = new JsonArray();
			for (Split val : gameData.get(key)) {
				JsonObject  jsonVal = new JsonObject();	
				
				try {
					jsonVal.put(Config.PB, Chrono.reverseFormatTime(val.personalBestProperty().getValueSafe()));
				}
				catch(Exception e) {
					jsonVal.put(Config.PB, Config.NULL);
				}
				try {
					jsonVal.put(Config.SOB, Chrono.reverseFormatTime(val.sumOfBestProperty().getValueSafe()));
				}
				catch(Exception e) {
					jsonVal.put(Config.SOB, Config.NULL);
				}
				
				jsonVal.put(Config.NAME, val.splitNameProperty().getValueSafe());
				jsonVal.put(Config.LOGO, val.getLogoPath());
				
				jsonKey.add(jsonVal);
			}
			jsonGameName.put(key, jsonKey);
		}
		
		// Écriture du json
		try (FileWriter file = new FileWriter(jsonPath)) {
			file.write(Jsoner.prettyPrint(Jsoner.serialize(jsonGameName)));
			}
		catch (IOException e) {
        e.printStackTrace();
        }

	}
	
	/*
	 * Save
	 */
	public void saveKeybinds(HashMap<String, KeyCombination> keybinds) {
		
		JsonObject combo = new JsonObject();
		for(String key : keybinds.keySet()) {
			combo.put(key, keybinds.get(key).getName());
		}
		// Écriture du json
		try (FileWriter file = new FileWriter(jsonPath)) {
			file.write(Jsoner.prettyPrint(Jsoner.serialize(combo)));
			}
		catch (IOException e) {
        e.printStackTrace();
        }
	}
	
	/**
	 * Load
	 * @return HashMap<String, String>
	 */
	public HashMap<KeyCombination, Runnable> loadKeybinds(HashMap<String, Runnable> keybinds) {
		HashMap<KeyCombination, Runnable> accelerators = new HashMap<KeyCombination, Runnable>();
		try
		{ 
            JsonObject runSet = (JsonObject) Jsoner.deserialize(new FileReader(jsonPath)); 
            for(String key : runSet.keySet()) {
            	Runnable function = keybinds.get(key);
            	accelerators.put(KeyCombination.keyCombination(runSet.get(key).toString()), function);
            }
		}
		catch(FileNotFoundException fe)
        {
            fe.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		return accelerators;
	}
}
