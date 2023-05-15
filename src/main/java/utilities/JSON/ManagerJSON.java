package utilities.JSON;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ManagerJSON {
    public static JSONObject load(String path)  {
        JSONObject object;

        try {
            object = (JSONObject) new JSONParser().parse(new FileReader(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return object;
    }

    public static void save(String path, JSONObject object) {
        try {
            Files.write(Paths.get(path), object.toJSONString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
