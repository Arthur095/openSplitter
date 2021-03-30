package json;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import control.Config;
import control.model.Split;
public class JacksonTest {
	
	
	   public static void main (String[] args){
		  
		   try {
			    // create object mapper instance
			    ObjectMapper mapper = new ObjectMapper();

			    // convert JSON file to map
			    Map<String, ArrayList<Split>> map = mapper.readValue(Paths.get(Config.FILEPATH).toFile(), Map.class);

			    // print map entries
			    for (Map.Entry<?, ?> entry : map.entrySet()) {
			    	System.out.println(entry.getKey());
			        
			        System.out.println(entry.getValue());
			        
			    }
			    System.out.println(map.get("Super Mario Odyssey - Any%").get(1));
			    Map<String, String> map2 = mapper.readValue(map.get("Super Mario Odyssey - Any%").get(1).toString(), Map.class);
			} catch (Exception ex) {
			    ex.printStackTrace();
			}
		   
	   }
}
