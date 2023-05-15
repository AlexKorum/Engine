package engine.render;

import engine.interfaces.EngineInterfaces;
import engine.render.renderer.Renderer;
import engine.render.window.Window;

public class RenderEngine implements EngineInterfaces {

    private Window window;
    private Renderer renderer;

    public RenderEngine() {
        window = new Window();
        renderer = new Renderer();
    }

    @Override
    public void init() {
        window.create();
    }

    @Override
    public void update() {
        window.update();
    }

    public boolean isShouldClose() {
        return window.isShouldClose();
    }

    public void close() {
        window.close();
    }
}
