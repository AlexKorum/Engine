package engine.render;

import engine.interfaces.EngineInterfaces;
import engine.render.window.Window;

public class RenderEngine implements EngineInterfaces {

    private Window window;

    public RenderEngine() {
        window = new Window();
    }

    @Override
    public void init() {
        window.create();
    }

    @Override
    public void update() {
        window.update();
    }

    public boolean isShouldClose(){
        return window.isShouldClose();
    }

    public void close(){
        window.close();
    }
}
