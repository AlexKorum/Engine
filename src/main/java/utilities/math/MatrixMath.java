package utilities.math;

import engine.render.window.Window;
import engine.scene.objects.components.ComponentsList;
import engine.scene.objects.components.Transform;
import engine.scene.objects.entities.Camera;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import utilities.JSON.ManagerJSON;

public class MatrixMath {
    public static Matrix4f createTransformationMatrix(Transform transform) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(transform.getPosition(), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(transform.getRotation().getX()), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(transform.getRotation().getY()), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(transform.getRotation().getZ()), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(transform.getScale(), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createProjectionMatrix(Camera camera) {
        float aspectRatio = (float) Window.getInstance().getWidth() / (float) Window.getInstance().getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(camera.getFOV() / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = camera.getFAR_PLANE() - camera.getNEAR_PLANE();

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((camera.getFAR_PLANE() + camera.getNEAR_PLANE()) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * camera.getNEAR_PLANE() * camera.getFAR_PLANE()) / frustum_length);
        projectionMatrix.m33 = 0;

        return projectionMatrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Transform cameraTransform = (Transform) camera.getComponent(ComponentsList.TRANSFORM);
        Matrix4f.rotate((float) Math.toRadians(cameraTransform.getRotation().getX()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(cameraTransform.getRotation().getY()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f negateCameraPos = new Vector3f(-cameraTransform.getPosition().getX(),
                -cameraTransform.getPosition().getY(),
                -cameraTransform.getPosition().getZ());
        Matrix4f.translate(negateCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
}
