package json;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.File;
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

/**
 * 
 * Handles reading and writing of the file given to the constructor, on json format using Simple.JSON library.
 *
 */
public class JsonReadWrite {
	
	/*Attributes*/
	private String jsonPath;
	
	/**
	 * Constructor
	 * @param jsonPath, the file path.
	 */
	public JsonReadWrite(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
	/**
	 * Add to splits data from key given in parameter, then add split in a list which is returned at end.
	 * @param String gameName
	 * @return ObservableList<Split> 
	 */
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
            	if (!(split_features.get(Config.SOB) == null)) {
            		sob = Chrono.formatTime(Double.parseDouble((String) split_features.get(Config.SOB)));
            	}
            	if (!(split_features.get(Config.PB) == null)) {
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
    }//fromJson
	
	/**
	 * Put in an array of double the data read from file according to the keys name.
	 * @param String gameName, the game as key of json.
	 * @param String row, the sob or pb line in key's data.
	 * @return ArrayList<Double>
	 */
	public ArrayList<Double> getGameTimes(String gameName, String row){
		ArrayList<Double> times = new ArrayList<Double>();
		try
		{ 
            JsonObject runSet = (JsonObject) Jsoner.deserialize(new FileReader(jsonPath));            
            JsonArray splits = (JsonArray) runSet.get(gameName);
            for (Object split : splits.toArray()) {
            	JsonObject split_features = (JsonObject) split;
            	Double time = null;
            	if (!(split_features.get(row) == null)) {
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
	}//getGameTimes
	
	/**
	 * Get all main keys of json, games, and put it in a list.
	 * @return ArrayList<String>
	 * @throws IOException
	 * @throws JsonException
	 */
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
		}//gameList
	
	
	/**
	 * Converts the whole json file read into a map.
	 * @return HashMap<String,ArrayList<Split>>
	 */
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
	}//toHashMap
	

	/**
	 * Writes the given map into the json file.
	 * @param Map<String, ArrayList<Split>> gameData 
	 */
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
					jsonVal.put(Config.PB, null);
				}
				try {
					jsonVal.put(Config.SOB, Chrono.reverseFormatTime(val.sumOfBestProperty().getValueSafe()));
				}
				catch(Exception e) {
					jsonVal.put(Config.SOB, null);
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
			File file = new File(Config.FILEPATH);
			try {
				file.createNewFile();
				toJson(gameData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
        }

	}//toJson
	
	/**
	 * Saves into the json file the given map in parameter.
	 * @param HashMap<String, KeyCombination> keybinds
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
			File file = new File(Config.CONFIGPATH);
			try {
				file.createNewFile();
				saveKeybinds(keybinds);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
	}//saveKeybinds
	
	/**
	 * Converts the json file to the javafx keybinds map format according to the matching value of map keys given in parameter.
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
	}//loadKeybinds
}
