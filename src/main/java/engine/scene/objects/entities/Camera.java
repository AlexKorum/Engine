package engine.scene.objects.entities;

import engine.scene.objects.Object;
import engine.scene.objects.components.Component;
import engine.scene.objects.components.ComponentsList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Camera extends Object {
    private final float FOV = 70;
    private final float NEAR_PLANE = 0.1f;
    private final float FAR_PLANE = 1000f;

    public Camera() {
        super("Camera");
    }

    public Camera(String name) {
        super(name);
    }

    public float getFOV() {
        return FOV;
    }

    public float getNEAR_PLANE() {
        return NEAR_PLANE;
    }

    public float getFAR_PLANE() {
        return FAR_PLANE;
    }

    // Конвертирование
    @Override
    public JSONObject toJSON() {
        JSONObject objectJSON = new JSONObject();
        objectJSON.put("name", name);
        objectJSON.put("type", "Camera");

        JSONArray componentsArray = new JSONArray();
        for (Component component : components.values()) {
            componentsArray.add(component.toJSON());
        }
        objectJSON.put("components", componentsArray);

        return objectJSON;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (json.get("type").equals("Camera")) {
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
}
