package engine.render.shaders;

import engine.scene.objects.components.Material;
import engine.scene.objects.entities.Light;
import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src\\main\\resources\\Shaders\\VertexShader.glsl";
    private static final String FRAGMENT_FILE = "src\\main\\resources\\Shaders\\FragmentShader.glsl";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;

    private int materialColorLocation;
    private int materialReflectivityLocation;
    private int materialShineDamping;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
        lightPositionLocation = super.getUniformLocation("lightPosition");
        lightColorLocation = super.getUniformLocation("lightColor");

        materialColorLocation = super.getUniformLocation("materialColor");
        materialReflectivityLocation = super.getUniformLocation("reflectivity");
        materialShineDamping = super.getUniformLocation("shineDamping");
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

    public void loadLight(Light light) {
        super.loadVector3f(lightPositionLocation, light.getPosition());
        super.loadVector3f(lightColorLocation, light.getColor());
    }

    public void loadMaterial(Material material) {
        super.loadVector3f(materialColorLocation, material.getColor());
        super.loadFloat(materialReflectivityLocation, material.getReflectivity());
        super.loadFloat(materialShineDamping, material.getShineDamping());
    }
}
