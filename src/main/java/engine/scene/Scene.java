package engine.scene;

import engine.interfaces.ConvertClassToJSON;
import engine.interfaces.ConvertJSONToClass;
import engine.scene.objects.Object;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene implements ConvertClassToJSON, ConvertJSONToClass {
    // Паттерн Singleton
    private static Scene instance;

    public static Scene getInstance() {
        if (instance == null) {
            instance = new Scene();
        }
        return instance;
    }


    // Переменные
    private String name;
    private Map<String, Object> objects;

    // Конструкторы
    private Scene() {
        name = "Default Scene";
        objects = new HashMap<>();
    }


    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject(String name) {
        return objects.get(name);
    }

    public List<Object> getObjects() {
        return objects.values().stream().toList();
    }

    // Добавление и удаление объектов со сцены
    public void addObject(Object object) {
        if (!objects.containsKey(object.getName())) {
            objects.put(object.getName(), object);
        } else {
            String newName = null;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                newName = object.getName() + i;
                if (!objects.containsKey(newName)) break;
            }
            object.setName(newName);
            objects.put(object.getName(), object);
        }
    }

    public void addObject(JSONObject objectJSON) {
        Object object = new Object(objectJSON);
        this.addObject(object);
    }

    public void removeObject(String name) {
        objects.remove(name);
    }

    // Конвертирование
    @Override
    public JSONObject toJSON() {
        JSONObject sceneJSON = new JSONObject();
        sceneJSON.put("name", name);
        sceneJSON.put("type", "Scene");

        JSONArray objectsArray = new JSONArray();
        for (Object object : objects.values()) {
            objectsArray.add(object.toJSON());
        }

        sceneJSON.put("objects", objectsArray);

        return sceneJSON;
    }


    @Override
    public void fromJSON(JSONObject json) {
        if (json.get("type").equals("Scene")) {
            objects.clear();
            this.name = json.get("name").toString();

            JSONArray objectsArray = (JSONArray) json.get("objects");
            for (int i = 0; i < objectsArray.size(); i++) {
                addObject((JSONObject) objectsArray.get(i));
            }
        } else {
            throw new IllegalStateException("В переданном JSON хранится не Scene.\n" + json);
        }
    }

    @Override
    public String toString() {
        String sceneString = "Scene: " + name + "\n\n";

        for (Object object : objects.values()) {
            sceneString += object.toString() + "\n";
        }

        return sceneString;
    }
}
