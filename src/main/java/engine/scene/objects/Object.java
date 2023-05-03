package engine.scene.objects;

import engine.interfaces.ConvertClassToJSON;
import engine.interfaces.ConvertJSONToClass;
import engine.interfaces.LoadFromJSON;
import engine.interfaces.SaveToJSON;
import engine.scene.objects.components.Component;
import engine.scene.objects.components.ComponentsList;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Object implements ConvertClassToJSON, ConvertJSONToClass, LoadFromJSON, SaveToJSON {
    protected String name;
    protected Map<ComponentsList, Component> components;

    public Object() {
        name = "GameObject";
        components = new HashMap<>();
    }

    public Object(String name) {
        this.name = name;
        components = new HashMap<>();
    }

    public Object(String path, String filename) {
        components = new HashMap<>();
        this.loadFromJSON(path, filename);
    }

    public Object(JSONObject json) {
        components = new HashMap<>();
        this.fromJSON(json);
    }

    public void addComponent(ComponentsList type) {

    }

    public Component getComponent(ComponentsList type) {
        return components.get(type);
    }

    public void removeComponent(ComponentsList type) {
        if (type == ComponentsList.Transform) return;
    }

    public List<Component> getComponents() {
        return components.values().stream().toList();
    }

    @Override
    public void saveToJSON(String path, String filename) {

    }

    public void saveToJSON(String path) {
        saveToJSON(path, name);
    }

    @Override
    public void loadFromJSON(String path, String filename) {

    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public void fromJSON(JSONObject json) {

    }
}
