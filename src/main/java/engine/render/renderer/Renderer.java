package engine.render.renderer;

import engine.render.shaders.StaticShader;
import engine.render.window.Window;
import engine.scene.Scene;
import engine.scene.objects.Object;
import engine.scene.objects.components.ComponentsList;
import engine.scene.objects.components.Material;
import engine.scene.objects.components.Mesh;
import engine.scene.objects.components.Transform;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import utilities.Loaders.LoaderVAO;
import utilities.math.MatrixMath;


public class Renderer {
    private StaticShader shader;
    private Material defaultMaterial;

    public void init() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        this.setBackgroundColor(new Vector3f(97.0f, 163.0f, 192.0f));
        shader = new StaticShader();
        defaultMaterial = new Material();
    }

    public void update() {
        Mesh mesh;
        for (Object object : Scene.getInstance().getObjects()) {
            mesh = (Mesh) object.getComponent(ComponentsList.MESH);
            if (mesh != null) {
                shader.start();
                loadUniformVariables(object);
                render(mesh);
                shader.stop();
            }
        }
    }

    public void close() {
        LoaderVAO.getInstance().cleanUp();
        shader.cleanUp();
    }

    public void setBackgroundColor(Vector3f backgroundColor) {
        backgroundColor.normalise(backgroundColor);
        GL11.glClearColor(backgroundColor.getX(), backgroundColor.getY(), backgroundColor.getZ(), 1.0f);
    }

    private void render(Mesh mesh) {
        GL30.glBindVertexArray(mesh.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndexes().length, GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void loadUniformVariables(Object object) {
        if (Window.getInstance().isResize()) {
            shader.loadProjectionMatrix(MatrixMath.createProjectionMatrix(Scene.getInstance().getMainCamera()));
            Window.getInstance().setResize(false);
        }
        shader.loadViewMatrix(MatrixMath.createViewMatrix(Scene.getInstance().getMainCamera()));
        shader.loadLight(Scene.getInstance().getLight());

        // Загрузка данных объекта
        shader.loadTransformationMatrix(MatrixMath.createTransformationMatrix((Transform) object.getComponent(ComponentsList.TRANSFORM)));

        if (object.getComponent(ComponentsList.MATERIAL) != null)
            shader.loadMaterial((Material) object.getComponent(ComponentsList.MATERIAL));
        else shader.loadMaterial(defaultMaterial);
    }
}
