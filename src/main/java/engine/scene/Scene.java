package engine.scene;

import engine.scene.objects.Object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {
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

    private Scene(String name) {
        this.name = name;
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
    public void save(String path, String filename) {

    }

    public void save(String path) {
        save(path, name);
    }

    public void load(String path, String filename) {

    }
}
