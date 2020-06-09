package controlador;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Esteban Guzmán R.
 */
public class FileManager {

    /**
     * Write a file with json format
     *
     * @param list Arraylist to convert
     * @param fileName filename to create
     * @param keyValue { keyValue : list }
     */
    public static void writteJsonFile(ArrayList<?> list, String fileName, 
            String keyValue) {
        
        JSONArray jsonList = new JSONArray();
        JSONParser parser = new JSONParser();
        Gson gson = new Gson();
        for (int i = 0; i < list.size(); i++) {
            
            try {
                
                JSONObject jsonItem = new JSONObject();
                jsonItem = (JSONObject) parser.parse(gson.toJson(list.get(i)));
                jsonList.add(jsonItem);
                
            } catch (ParseException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        JSONObject json = new JSONObject();
        json.put(keyValue, jsonList);
        System.out.println(json.toJSONString());
        try (FileWriter file = new FileWriter(fileName)) {

            file.write(json.toJSONString());
            file.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Read a file with json format
     *
     * @param fileName name of file with json format
     * @return { keyValue : List }
     */
    public static JSONObject readFileJson(String fileName) {
        
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            
            Object obj = jsonParser.parse(reader);
            JSONObject jsonFromFile = (JSONObject) obj;
            return jsonFromFile;
            
        } catch (ParseException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
        
    }
    
}
