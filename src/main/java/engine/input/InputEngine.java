package engine.input;

import engine.interfaces.EngineInterfaces;
import engine.render.window.Window;
import engine.scene.Scene;
import engine.scene.objects.Object;
import engine.scene.objects.components.ComponentsList;
import engine.scene.objects.components.Material;
import engine.scene.objects.components.Mesh;
import engine.scene.objects.components.Transform;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utilities.JSON.ManagerJSON;
import utilities.Loaders.LoaderOBJ;

public class InputEngine implements EngineInterfaces {
    private Scene scene;
    private MoveController controller;

    @Override
    public void init() {
        this.scene = Scene.getInstance();
        this.controller = new MoveController(scene.getMainCamera());
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
        for (int i = GLFW.GLFW_KEY_1; i <= GLFW.GLFW_KEY_5; i++) {
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
        Object object = new Object();

        Transform transform = (Transform) object.addComponent(ComponentsList.TRANSFORM);
        transform.setPosition(((Transform) scene.getMainCamera().getComponent(ComponentsList.TRANSFORM)).getPosition());
        Vector4f forward = controller.getForward();
        forward.scale(2f);
        transform.getPosition().translate(forward.getX(), forward.getY(), forward.getZ());

        Material material = (Material) object.addComponent(ComponentsList.MATERIAL);

        Mesh mesh = (Mesh) object.addComponent(ComponentsList.MESH);
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_1) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Cube.obj", mesh);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_2) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Sphere.obj", mesh);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_3) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Torus.obj", mesh);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_4) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Cylinder.obj", mesh);
        }
        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_5) == GLFW.GLFW_PRESS) {
            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Concuss.obj", mesh);
        }
//        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_6) == GLFW.GLFW_PRESS) {
//            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\Monkey.obj", mesh);
//        }
//        if (GLFW.glfwGetKey(Window.getInstance().getWindowId(), GLFW.GLFW_KEY_7) == GLFW.GLFW_PRESS) {
//            LoaderOBJ.loadMeshFromOBJ("src\\main\\resources\\Assets\\Prefabs\\Models\\OBJ\\dragon.obj", mesh);
//        }


        scene.addObject(object);
    }
}
