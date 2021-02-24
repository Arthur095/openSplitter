package json;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import control.model.Split;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class JsonReadWrite {

	//private static JsonParser parser = new JsonParser();
	private String jsonPath;
	private String jsonTestPath = "D:\\java_workspace\\SpeedrunTimer\\resources\\json\\test.jon";
	private HashMap<String, ArrayList<Split>> gameData = new HashMap<String, ArrayList<Split>>();
	
	public JsonReadWrite(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
	// Extraction des segments pour un jeu choisi par l’utilisateur
	public ObservableList<Split> fromJson (String gameName) {
		ObservableList<Split> game = FXCollections.observableArrayList();
		try
		{ 
			//Object jsonobject = parser.parse(new FileReader(jsonPath));
            JsonObject runSet = (JsonObject) Jsoner.deserialize(new FileReader(jsonPath));            
            JsonArray splits = (JsonArray) runSet.get(gameName);
            for (Object split : splits.toArray()) {
            	JsonObject split_features = (JsonObject) split;
            	String sob;
            	String pb;
            	if (split_features.get("sob").equals("null")) {
            		sob = null;
            	}
            	else {
            		sob = (String) split_features.get("sob");
            	}
            	if (split_features.get("pb").equals("null")) {
            		pb = null;
            	}
            	else {
            		pb = (String) split_features.get("sob");
            	}
            	//String bloup = null;
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
	
	
	// Stocker un json dans un HashMap
	public static HashMap<String,ArrayList<Split>> toHashMap(String gameName, ArrayList<Split>game) {
		HashMap<String,ArrayList<Split>> gameData = new HashMap<String,ArrayList<Split>>();
		gameData.put(gameName, game);
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
