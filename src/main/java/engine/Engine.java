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
import org.lwjgl.util.vector.Vector3f;
import utilities.JSON.ManagerJSON;
import utilities.Loaders.LoaderOBJ;

import java.util.stream.Stream;

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


        // Создание сцены
        // ------------------------------------------------------------------------------------------------------------
        Scene scene = Scene.getInstance();
        Scene.getInstance().fromJSON(ManagerJSON.load("src\\main\\resources\\Assets\\Scenes\\SceneTest.json"));

        // Инициализация движка физики
        physicsEngine.init();
        // Инициализация движка ввода
        inputEngine.init();

    }

    private void update() {
        while (!renderEngine.isShouldClose()) {
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
