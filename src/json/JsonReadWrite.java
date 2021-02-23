package readWriteJson;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class JsonReadWrite {

	private static JSONParser parser = new JSONParser();
	private String jsonPath;
	private String jsonTestPath = "/home/martin/eclipseJavaWorkspace/SplitData/test.json";
	private HashMap<String, ArrayList<Split>> gameData = new HashMap<String, ArrayList<Split>>();
	
	public JsonReadWrite(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
	// Extraction des segments pour un jeu choisi par l’utilisateur
	public ArrayList<Split> fromJson (String gameName) {
		ArrayList<Split> game = new ArrayList<Split>();
		try
		{ 
			Object object = parser.parse(new FileReader(jsonPath));
            JSONObject runSet = (JSONObject)object;            
            JSONArray splits = (JSONArray) runSet.get(gameName);
            for (Object split : splits.toArray()) {
            	JSONObject split_features = (JSONObject) split;
            	Double sob;
            	Double pb;
            	if (split_features.get("sob").equals("null")) {
            		sob = null;
            	}
            	else {
            		sob = (Double) split_features.get("sob");
            	}
            	if (split_features.get("pb").equals("null")) {
            		pb = null;
            	}
            	else {
            		pb = (Double) split_features.get("sob");
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
	public ArrayList<String> GameList() {
		Object object;
		ArrayList<String> allGames = new ArrayList<String>();
		try {
			object = parser.parse(new FileReader(jsonPath));
			JSONObject runSet = (JSONObject)object;
			for (Object game : runSet.keySet().toArray()) {
				allGames.add(game.toString());
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
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
		
		JSONObject jsonGameName = new JSONObject();
		for (String key : gameData.keySet()) {
			JSONArray jsonKey = new JSONArray();
			for (Split val : gameData.get(key)) {
				JSONObject  jsonVal = new JSONObject();
				jsonVal.put("logo", val.logoPath);
				jsonVal.put("name", val.name);
				jsonVal.put("sob", String.valueOf(val.sob));
				jsonVal.put("pb", String.valueOf(val.pb));
				jsonKey.add(jsonVal);
			}
			jsonGameName.put(key, jsonKey);
		}
		System.out.println(jsonGameName);

		// Écriture du json
		try (FileWriter file = new FileWriter(jsonTestPath)) {
			file.write(jsonGameName.toJSONString()); 
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
