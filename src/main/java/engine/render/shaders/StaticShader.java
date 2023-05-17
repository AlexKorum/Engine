package engine.render.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src\\main\\resources\\Shaders\\VertexShader.glsl";
    private static final String FRAGMENT_FILE = "src\\main\\resources\\Shaders\\FragmentShader.glsl";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "normals");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix4f(transformationMatrixLocation, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix4f(projectionMatrixLocation, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix4f(viewMatrixLocation, matrix);
    }
}
