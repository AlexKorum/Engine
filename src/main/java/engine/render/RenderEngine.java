package engine.render;

import engine.interfaces.EngineInterfaces;
import engine.render.renderer.Renderer;
import engine.render.window.Window;

public class RenderEngine implements EngineInterfaces {

    private Window window;
    private Renderer renderer;

    public RenderEngine() {
        window = Window.getInstance();
        renderer = new Renderer();
    }

    @Override
    public void init() {
        window.create();
        renderer.init();
    }

    @Override
    public void update() {
        renderer.update();
        window.update();
    }

    public boolean isShouldClose() {
        return window.isShouldClose();
    }

    public void close() {
        renderer.close();
        window.close();
    }
}
