package engine.render;

import engine.interfaces.EngineInterfaces;
import engine.render.renderer.Renderer;
import engine.render.window.Window;
import utilities.classes.GlobalTimer;

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

        // Установка FPS в заголовок окна
        window.setWindowTitle("FPS: " + 1000 / GlobalTimer.getDeltaTimeMillis());
    }

    public boolean isShouldClose() {
        return window.isShouldClose();
    }

    public void close() {
        renderer.close();
        window.close();
    }
}
