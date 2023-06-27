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
import utilities.classes.GlobalTimer;

import java.util.Random;

public class Engine {
    private InputEngine inputEngine;
    private PhysicsEngine physicsEngine;
    private RenderEngine renderEngine;

    private GlobalTimer globalTimer;

    public Engine() {
        // Создание объектов
        renderEngine = new RenderEngine();
        physicsEngine = new PhysicsEngine();
        inputEngine = new InputEngine();
        globalTimer = new GlobalTimer();
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
        this.generateScene();
        // ------------------------------------------------------------------------------------------------------------

        // Инициализация движка физики
        physicsEngine.init();
        // Инициализация движка ввода
        inputEngine.init();
        // Инициализация глобального таймера
        globalTimer.init();
    }

    private void generateScene() {
        Scene scene = Scene.getInstance();
        int type = 0;
        switch (type) {
            case 0:
                Object object;
                for (int i = 0; i < 0; i++) {
                    object = new Object();
                    Transform transform = (Transform) object.addComponent(ComponentsList.TRANSFORM);
                    Material material = (Material) object.addComponent(ComponentsList.MATERIAL);
                    Mesh mesh = (Mesh) object.addComponent(ComponentsList.MESH);
                    Collider collider = (Collider) object.addComponent(ComponentsList.COLLIDER);
                    Rigidbody rigidbody = (Rigidbody) object.addComponent(ComponentsList.RIGIDBODY);

                    transform.getPosition().set(0, i + 4 + ((float) i) / 10, i);
//            transform.getPosition().set(0, 2f, 0);
                    LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Sphere.obj", mesh);
                    collider.setVertexes(LoaderOBJ.getColliderVertex("src\\main\\resources\\Assets\\Prefabs\\Colliders\\OBJ\\CubeCollider.obj"));

                    material.setColor(0, 1, 0);

                    scene.addObject(object);
                }

                Object cube = new Object();
                cube.setName("Cube");
                Transform cubeTransform = (Transform) cube.getComponent(ComponentsList.TRANSFORM);
                cubeTransform.getPosition().translate(0, 0, 0);
                cubeTransform.getRotation().set(0, 0, 0);
                cubeTransform.getScale().set(100f, 1.01f, 100f);
                Mesh cubeMesh = (Mesh) cube.addComponent(ComponentsList.MESH);
                LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Cube.obj", cubeMesh);
                Collider colliderCube = (Collider) cube.addComponent(ComponentsList.COLLIDER);
                colliderCube.setVertexes(LoaderOBJ.getColliderVertex("src\\main\\resources\\Assets\\Prefabs\\Colliders\\OBJ\\CubeCollider.obj"));
                Material materialCube = (Material) cube.addComponent(ComponentsList.MATERIAL);

                scene.addObject(cube);
                break;
            case 1:
                Object object1 = new Object();
                Transform transform = (Transform) object1.getComponent(ComponentsList.TRANSFORM);
                object1.addComponent(ComponentsList.MATERIAL);
                LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\dragon.obj", (Mesh) object1.addComponent(ComponentsList.MESH));
                scene.addObject(object1);
                break;
            case 2:
                Object plane = new Object();
                ((Transform) plane.getComponent(ComponentsList.TRANSFORM)).getScale().set(100, 1, 100);

                LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Cube.obj", (Mesh) plane.addComponent(ComponentsList.MESH));
                plane.addComponent(ComponentsList.MATERIAL);
                Collider collider = (Collider) plane.addComponent(ComponentsList.COLLIDER);
                collider.setVertexes(LoaderOBJ.getColliderVertex("src\\main\\resources\\Assets\\Prefabs\\Colliders\\OBJ\\CubeCollider.obj"));

                Random random = new Random(System.nanoTime());
                int n = 2;
                Object randomObj;
                Collider randomCollider;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        for (int k = 0; k < n; k++) {
                            randomObj = new Object();
                            ((Transform) randomObj.getComponent(ComponentsList.TRANSFORM)).getPosition().set(i + ((float) i) / 10 + random.nextFloat() * 10,
                                    j + ((float) j) / 10 + random.nextFloat() * 10 +5,
                                    k + ((float) k) / 10 + random.nextFloat() * 10);

                            ((Transform) randomObj.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(random.nextFloat(), random.nextFloat(), random.nextFloat());
                            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Sphere.obj", (Mesh) randomObj.addComponent(ComponentsList.MESH));
//                            LoaderOBJ.loadRandomMesh((Mesh) randomObj.addComponent(ComponentsList.MESH));
                            ((Material) randomObj.addComponent(ComponentsList.MATERIAL)).setColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
                            randomCollider = (Collider) randomObj.addComponent(ComponentsList.COLLIDER);
                            randomCollider.setVertexes(LoaderOBJ.getColliderVertex("src\\main\\resources\\Assets\\Prefabs\\Colliders\\OBJ\\SphereCollider.obj"));
                            randomObj.addComponent(ComponentsList.RIGIDBODY);

                            scene.addObject(randomObj);
                        }
                    }
                }

                scene.addObject(plane);
                break;
        }
    }

    private void update() {
        while (!renderEngine.isShouldClose()) {
            Scene.getInstance().update();

            // Обновление глобального таймера
            globalTimer.update();

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
