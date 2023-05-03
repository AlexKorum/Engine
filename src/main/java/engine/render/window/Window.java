package engine.render.window;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.io.FileReader;
import java.io.IOException;

public class Window {
    private final String url = "src\\main\\resources\\Settings\\WindowSettings.json";
    private long windowId;
    private String title;
    private int width;
    private int height;

    public Window() {
        JSONObject windowSettings;
        try {
            windowSettings = (JSONObject) new JSONParser().parse(new FileReader(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        this.title = windowSettings.get("Title").toString();
        this.width = Integer.parseInt(windowSettings.get("Width").toString());
        this.height = Integer.parseInt(windowSettings.get("Height").toString());
    }

    public void create() {
        // Инициализация модуля LWJGL
        boolean initState = GLFW.glfwInit();

        if (!initState)
            throw new IllegalStateException("Could not create GLFW!");

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        windowId = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (windowId == MemoryUtil.NULL)
            throw new IllegalStateException("Can't make window!");

        // Калбек на изменение размера окна (меняет область отображения на полный размер окна)
        GLFW.glfwSetWindowSizeCallback(windowId, (windowId, width, height) -> {
            GL11.glViewport(0, 0, width, height);
        });

        GLFW.glfwMakeContextCurrent(windowId);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowId);
        GL.createCapabilities();
    }

    public void update() {
        GLFW.glfwSwapBuffers(windowId);
        GLFW.glfwSwapInterval(1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void close() {
        GLFW.glfwDestroyWindow(windowId);
        GLFW.glfwTerminate();
    }

    public boolean isShouldClose() {
        return GLFW.glfwWindowShouldClose(windowId);
    }

    public void setWindowTitle(String title) {
        this.title = title;
        GLFW.glfwSetWindowTitle(this.windowId, title);
    }

}
