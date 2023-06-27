package engine.input;

import engine.interfaces.EngineInterfaces;
import engine.render.window.Window;
import engine.scene.Scene;
import engine.scene.objects.Object;
import engine.scene.objects.components.*;
import engine.scene.objects.components.enums.ColliderList;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utilities.JSON.ManagerJSON;
import utilities.Loaders.LoaderOBJ;
import utilities.classes.GlobalTimer;

import java.util.Random;

public class InputEngine implements EngineInterfaces {
    private Scene scene;
    private MoveController controller;
    private float oldDeltaTime;

    @Override
    public void init() {
        this.scene = Scene.getInstance();
        this.controller = new MoveController(scene.getMainCamera());
        this.oldDeltaTime = GlobalTimer.getDeltaTimeSeconds();
    }

    @Override
    public void update() {
        GLFW.glfwPollEvents();

        // Перемещения
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            controller.moveForward();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            controller.moveBack();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            controller.moveLeft();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            controller.moveRight();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            controller.moveUp();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            controller.moveDown();
        }

        // Повороты
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            controller.rotateRight();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            controller.rotateLeft();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            controller.rotateUp();
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            controller.rotateDown();
        }

        // Генерация объекта
        for (int i = GLFW.GLFW_KEY_1; i <= GLFW.GLFW_KEY_7; i++) {
            if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), i) == GLFW.GLFW_PRESS) {
                generationObject();
            }
        }
        // Сохранение и загрузка
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_F5) == GLFW.GLFW_PRESS) {
            ManagerJSON.save("src\\main\\resources\\Assets\\Scenes\\SceneTest.json", Scene.getInstance().toJSON());
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_F6) == GLFW.GLFW_PRESS) {
            Scene.getInstance().fromJSON(ManagerJSON.load("src\\main\\resources\\Assets\\Scenes\\SceneTest.json"));
        }
    }

    private void generationObject() {
        if (oldDeltaTime == GlobalTimer.getDeltaTimeSeconds()) {
            return;
        }
        oldDeltaTime = GlobalTimer.getDeltaTimeSeconds();
        Object object = new Object();

        Transform transform = (Transform) object.addComponent(ComponentsList.TRANSFORM);
        transform.setPosition(((Transform) scene.getMainCamera().getComponent(ComponentsList.TRANSFORM)).getPosition());
        Vector4f forward = controller.getForward();
        forward.scale(2f);
        transform.getPosition().translate(forward.getX(), forward.getY(), forward.getZ());

        Random random = new Random(System.nanoTime());
        Material material = (Material) object.addComponent(ComponentsList.MATERIAL);
        material.setColor(random.nextFloat(), random.nextFloat(), random.nextFloat());

        Mesh mesh = (Mesh) object.addComponent(ComponentsList.MESH);
        Collider collider = (Collider) object.addComponent(ComponentsList.COLLIDER);
        collider.setVertexes(LoaderOBJ.getColliderVertex("src\\main\\resources\\Assets\\Prefabs\\Colliders\\OBJ\\CubeCollider.obj"));

        Rigidbody rigidbody = (Rigidbody) object.addComponent(ComponentsList.RIGIDBODY);
//        forward.normalise();
//        forward.scale(50f);
//        rigidbody.addForce(new Vector3f(forward));

        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_1) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Cube.obj", mesh);
//            collider.setType(ColliderList.CUBE);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_2) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Sphere.obj", mesh);
            collider.setType(ColliderList.SPHERE);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_3) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Plane.obj", mesh);
//            collider.setType(ColliderList.PLANE);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_4) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Cylinder.obj", mesh);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_5) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Concuss.obj", mesh);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_6) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Torus.obj", mesh);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_7) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\dragon.obj", mesh);
        }


        scene.addObject(object);
    }
}
