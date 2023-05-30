package engine.render;

import engine.interfaces.EngineInterfaces;
import engine.render.renderer.Renderer;
import engine.render.window.Window;

public class RenderEngine implements EngineInterfaces {

    private Window window;
    private Renderer renderer;
    private long startTime;
    private long endTime;

    public RenderEngine() {
        window = Window.getInstance();
        renderer = new Renderer();
    }

    @Override
    public void init() {
        window.create();
        renderer.init();

        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        renderer.update();
        window.update();

        setFPS();
    }

    public boolean isShouldClose() {
        return window.isShouldClose();
    }

    public void close() {
        renderer.close();
        window.close();
    }

    // Просчет fps
    private void setFPS() {
        endTime = System.currentTimeMillis();
        window.setWindowTitle("FPS: " + 1000 / (float) (endTime - startTime));
        startTime = endTime;
    }
}
