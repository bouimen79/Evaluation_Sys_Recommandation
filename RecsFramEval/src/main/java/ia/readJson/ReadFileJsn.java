package ia.readJson;
import ia.EstimateTime;
import org.json.JSONArray;
import org.json.JSONObject;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ReadFileJsn {

public static HashMap<Long, String> readJson(String filename)  {
    HashMap<Long, String> hash_map = new HashMap<Long, String>();
    // read in string
    try{
    String data = new String(Files.readAllBytes(Paths.get(filename)));
    //read as json array
    JSONArray jsonArray = new JSONArray(data);
    for(int i = 0; i < jsonArray.length(); i++) {
        String str = jsonArray.get(i).toString();
        JSONObject object1 = new JSONObject(str);
        long idItem = object1.getLong("_id");
        String fullText = object1.getString("fulltext");
        hash_map.put(idItem, fullText);
    }
    }catch (Exception e){
        e.printStackTrace();
    }
    return hash_map;
}

public static String getfullText(String filename,long idItem){
    HashMap<Long, String> hash_Map=readJson(filename);
    return hash_Map.get(idItem);
}

}
