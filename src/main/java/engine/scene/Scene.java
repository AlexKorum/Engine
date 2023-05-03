package engine.scene;

import engine.interfaces.LoadFromJSON;
import engine.interfaces.SaveToJSON;
import engine.scene.objects.Object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene implements SaveToJSON, LoadFromJSON {
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

    public List<Object> getObjects() {
        return objects.values().stream().toList();
    }

    // Добавление и удаление объектов со сцены
    public void addObject(String name, Object object) {

    }

    public void addObjectFromPrefabs(String pathPrefabs, String filename) {

    }

    public void removeObject(String name) {

    }

    // Сохранение и загрузка сцены
    @Override
    public void saveToJSON(String path, String filename) {

    }

    public void save(String path) {
        saveToJSON(path, name);
    }

    @Override
    public void loadFromJSON(String path, String filename) {

    }

}
