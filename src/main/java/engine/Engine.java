package engine;

import engine.input.InputEngine;
import engine.physics.PhysicsEngine;
import engine.render.RenderEngine;
import engine.scene.Scene;
import engine.scene.objects.Object;
import engine.scene.objects.components.*;
import engine.scene.objects.components.enums.ColliderList;
import engine.scene.objects.components.enums.ComponentsList;
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


        // Создание сцены
        // ------------------------------------------------------------------------------------------------------------
        Scene scene = Scene.getInstance();
//        Scene.getInstance().fromJSON(ManagerJSON.load("src\\main\\resources\\Assets\\Scenes\\SceneTest.json"));

        Object plane = new Object();
        plane.setName("Plane");
        Mesh meshPlane = (Mesh) plane.addComponent(ComponentsList.MESH);
        LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Sphere.obj", meshPlane);
        Collider colliderPlane = (Collider) plane.addComponent(ComponentsList.COLLIDER);
//        colliderPlane.setType(ColliderList.PLANE);
        colliderPlane.setVertexes(meshPlane.getVertexes());
        Material materialPlane = (Material) plane.addComponent(ComponentsList.MATERIAL);


        Object cube = new Object();
        cube.setName("Cube");
        Transform cubeTransform = (Transform) cube.getComponent(ComponentsList.TRANSFORM);
        cubeTransform.getPosition().translate(0, 2, 0);
        cubeTransform.getScale().set(4, 1, 1);
        Mesh cubeMesh = (Mesh) cube.addComponent(ComponentsList.MESH);
        LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Cube.obj", cubeMesh);
        Collider colliderCube = (Collider) cube.addComponent(ComponentsList.COLLIDER);
        colliderCube.setVertexes(meshPlane.getVertexes());
        Material materialCube = (Material) cube.addComponent(ComponentsList.MATERIAL);

        scene.addObject(plane);
        scene.addObject(cube);
        // ------------------------------------------------------------------------------------------------------------

        // Инициализация движка физики
        physicsEngine.init();
        // Инициализация движка ввода
        inputEngine.init();

    }


    private void update() {
        while (!renderEngine.isShouldClose()) {
            ((Transform) Scene.getInstance().getObject("Cube").getComponent(ComponentsList.TRANSFORM))
                    .getRotation().translate(0, 0, 0.5f);
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
