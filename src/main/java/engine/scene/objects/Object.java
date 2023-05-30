package engine.scene.objects;

import engine.interfaces.ConvertClassToJSON;
import engine.interfaces.ConvertJSONToClass;
import engine.scene.Scene;
import engine.scene.objects.components.Component;
import engine.scene.objects.components.enums.ComponentsList;
import engine.scene.objects.components.Transform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.EnumMap;
import java.util.List;

public class Object implements ConvertClassToJSON, ConvertJSONToClass {
    protected String name;
    protected EnumMap<ComponentsList, Component> components;

    public Object() {
        this.init();
        name = "GameObject";
    }

    public Object(String name) {
        this.init();
        this.name = name;
    }

    public Object(JSONObject json) {
        this.init();
        this.fromJSON(json);
    }

    protected void init() {
        components = new EnumMap<>(ComponentsList.class);
        components.put(ComponentsList.TRANSFORM, new Transform());
    }


    public void setName(String name) {
        if (Scene.getInstance().getObject(name) == null) {
            this.name = name;
        } else {
            if (this.equals(Scene.getInstance().getObject(name))) {
                Scene.getInstance().removeObject(name);
                this.name = name;
                Scene.getInstance().addObject(this);
            } else {
                System.out.println("Нельзя использовать имя, которое уже используется другим объектом");
            }
        }

    }

    public String getName() {
        return name;
    }

    public Component addComponent(ComponentsList type) {
        if (type.equals(ComponentsList.TRANSFORM)) return components.get(ComponentsList.TRANSFORM);
        Component component = ComponentsList.getComponent(type);
        components.put(type, component);
        return component;
    }

    public Component getComponent(ComponentsList type) {
        return components.get(type);
    }

    public void removeComponent(ComponentsList type) {
        if (type == ComponentsList.TRANSFORM) return;
        components.remove(type);
    }

    public List<Component> getComponents() {
        return components.values().stream().toList();
    }

    // Конвертирование
    @Override
    public JSONObject toJSON() {
        JSONObject objectJSON = new JSONObject();
        objectJSON.put("name", name);
        objectJSON.put("type", "Object");

        JSONArray componentsArray = new JSONArray();
        for (Component component : components.values()) {
            componentsArray.add(component.toJSON());
        }
        objectJSON.put("components", componentsArray);

        return objectJSON;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (json.get("type").equals("Object")) {
            this.name = (String) json.get("name");

            JSONArray componentsArray = (JSONArray) json.get("components");
            Component component;
            for (int i = 0; i < componentsArray.size(); i++) {
                component = ComponentsList.getComponent(((JSONObject) componentsArray.get(i)).get("tag").toString());
                component.fromJSON((JSONObject) componentsArray.get(i));
                components.put(component.getTag(), component);
            }
        } else {
            throw new IllegalStateException("В переданном JSON хранится не Object.\n" + json);
        }
    }


    @Override
    public String toString() {
        String objectString = "Object: " + name + "\n";

        for (Component component : components.values()) {
            objectString += component.toString() + "\n";
        }

        return objectString;
    }
}
