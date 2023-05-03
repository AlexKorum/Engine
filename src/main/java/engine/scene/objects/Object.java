package engine.scene.objects;

import engine.scene.objects.components.Component;
import engine.scene.objects.components.ComponentsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Object {
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
    }

    public List<Component> getComponents() {
        return components.values().stream().toList();
    }

}
