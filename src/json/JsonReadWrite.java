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

import control.model.Chrono;
import control.model.Split;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class JsonReadWrite {

	//private static JsonParser parser = new JsonParser();
	private String jsonPath;
	private String jsonTestPath = "D:\\java_workspace\\SpeedrunTimer\\resources\\json\\test.json";
	private HashMap<String, ArrayList<Split>> gameData = new HashMap<String, ArrayList<Split>>();
	
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
            	if (!(split_features.get("sob").equals("null"))) {
            		sob = Chrono.formatTime(Double.parseDouble((String) split_features.get("sob")));
            	}
            	if (!(split_features.get("pb").equals("null"))) {
            		pb = Chrono.formatTime(Double.parseDouble((String) split_features.get("pb")));
            	}
            	Split Segment = new Split(split_features.get("name").toString(),split_features.get("logo").toString(), sob, pb);
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
            	if (!(split_features.get(row).equals("null"))) {
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
	public void toJson(HashMap<String, ArrayList<Split>> gameData) {
		
		JsonObject jsonGameName = new JsonObject();
		for (String key : gameData.keySet()) {
			JsonArray jsonKey = new JsonArray();
			for (Split val : gameData.get(key)) {
				JsonObject  jsonVal = new JsonObject();
				jsonVal.put("logo", val.logoProperty());
				jsonVal.put("name", val.splitNameProperty());
				jsonVal.put("sob", String.valueOf(val.sumOfBestProperty()));
				jsonVal.put("pb", String.valueOf(val.personalBestProperty()));
				jsonKey.add(jsonVal);
			}
			jsonGameName.put(key, jsonKey);
		}
		System.out.println(jsonGameName);

		// Écriture du json
		try (FileWriter file = new FileWriter(jsonTestPath)) {
			file.write(jsonGameName.toJson()); 
			file.flush();
			}
		catch (IOException e) {
        e.printStackTrace();
        }
	}


	// Section getter/setter
	public HashMap<String, ArrayList<Split>> getGameData() {
		return gameData;
	}

	public void setGameData(HashMap<String, ArrayList<Split>> gameData) {
		this.gameData = gameData;
	}
}
