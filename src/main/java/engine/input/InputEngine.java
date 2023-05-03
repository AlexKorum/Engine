package engine.input;

import engine.interfaces.EngineInterfaces;
import org.lwjgl.glfw.GLFW;

public class InputEngine implements EngineInterfaces {
    @Override
    public void init() {

    }

    @Override
    public void update() {
        GLFW.glfwPollEvents();
    }
}
