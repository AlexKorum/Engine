package engine;

import engine.input.InputEngine;
import engine.physics.PhysicsEngine;
import engine.render.RenderEngine;
import engine.scene.Scene;
import engine.scene.objects.Object;
import engine.scene.objects.components.ComponentsList;
import engine.scene.objects.components.Material;
import engine.scene.objects.components.Mesh;
import engine.scene.objects.components.Transform;
import engine.scene.objects.entities.Camera;
import utilities.JSON.ManagerJSON;
import utilities.Loaders.LoaderOBJ;

public class Engine {
    private InputEngine inputEngine;
    private PhysicsEngine physicsEngine;
    private RenderEngine renderEngine;

    public Engine() {
        // Создание объектов
        renderEngine = new RenderEngine();
        physicsEngine = new PhysicsEngine();
        inputEngine = new InputEngine();
    }

    // Запуск движка
    public void run() {
        // Запуск инициализаций
        this.init();
        // Запуск цикла обновления
        this.update();
    }

    // Инициализация данных движков
    private void init() {
        // Инициализация движка рендера
        renderEngine.init();
        // Инициализация движка физики
        physicsEngine.init();
        // Инициализация движка ввода
        inputEngine.init();

        // Создание сцены
        // ------------------------------------------------------------------------------------------------------------
        Scene scene = Scene.getInstance();
        Object object = new Object();

        Material material = (Material) object.addComponent(ComponentsList.MATERIAL);
        Mesh mesh = (Mesh) object.addComponent(ComponentsList.MESH);
        LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Sphere.obj", mesh);

        Scene.getInstance().addObject(object);

        // TODO Убрать потом
        ManagerJSON.save("src\\main\\resources\\Assets\\Scenes\\SceneTest.json", scene.toJSON());
        Scene.getInstance().fromJSON(ManagerJSON.load("src\\main\\resources\\Assets\\Scenes\\SceneTest.json"));

    }

    private void update() {
        Object object = Scene.getInstance().getObject("GameObject");
        // Бесконечный цикл обновления (завершается если пришла команда на закрытие окна)
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(0f, 0f, -3f);
        while (!renderEngine.isShouldClose()) {
            ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation().translate(0.5f, 0.5f, 0f);
            // Обновление движка ввода (прием сообщений от пользователя)
            inputEngine.update();
            // Обновление движка физики (изменение позиций объектов, обработка столкновений и тд)
            physicsEngine.update();
            // Обновление движка рендера (вывод изображения, обновление экрана)
            renderEngine.update();
        }
        // Остановка работы приложения (закрытие окна и освобождения памяти)
        renderEngine.close();
    }
}
