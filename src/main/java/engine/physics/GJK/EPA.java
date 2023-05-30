package engine.physics.GJK;

import engine.scene.objects.Object;
import engine.scene.objects.components.Collider;
import engine.scene.objects.components.Transform;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utilities.math.MatrixMath;

import java.util.ArrayList;
import java.util.List;

public class EPA {
    public static CollisionInformation collisionInformation = new CollisionInformation();

    private final static Integer[] facesPattern = new Integer[]{
            0, 1, 2,
            0, 3, 1,
            0, 2, 3,
            1, 3, 2
    };

    public static CollisionInformation getCollisionInformation(Simplex simplex, Object objectA, Object objectB) {
        Transform transformA = (Transform) objectA.getComponent(ComponentsList.TRANSFORM);
        Transform transformB = (Transform) objectB.getComponent(ComponentsList.TRANSFORM);
        Collider colliderA = (Collider) objectA.getComponent(ComponentsList.COLLIDER);
        Collider colliderB = (Collider) objectB.getComponent(ComponentsList.COLLIDER);
        float[] vertexesA = createTransformVertexes(transformA, colliderA);
        float[] vertexesB = createTransformVertexes(transformB, colliderB);

        ArrayList<Vector3f> polytope = simplex.getPoints();
        ArrayList<Integer> faces = new ArrayList<>(List.of(facesPattern));

        FaceNormals faceNormals = getFaceNormals(polytope, faces);
        ArrayList<Vector4f> normals = faceNormals.getNormals();
        int minFace = faceNormals.getMinTriangle();

        Vector3f minNormal = new Vector3f();
        float minDistance = Float.MAX_VALUE;

        while (minFace == Float.MAX_VALUE) {
            // TODO Закончить
        }

        collisionInformation.set(minNormal, minDistance + 0.001f);

        return collisionInformation;
    }

    private static FaceNormals getFaceNormals(ArrayList<Vector3f> polytope, ArrayList<Integer> faces) {
        ArrayList<Vector4f> normals = new ArrayList<>();
        int minTriangle = 0;
        float minDistance = Float.MAX_VALUE;

        for (int i = 0; i < faces.size(); i += 3) {
            Vector3f a = polytope.get(faces.get(i));
            Vector3f b = polytope.get(faces.get(i + 1));
            Vector3f c = polytope.get(faces.get(i + 2));

            Vector3f normal = new Vector3f();
            Vector3f.cross(Vector3f.sub(b, a, null), Vector3f.sub(c, a, null), null).normalise(normal);
            float distance = Vector3f.dot(normal, a);

            if (distance < 0) {
                normal.negate();
                distance *= -1;
            }

            normals.add(new Vector4f(normal.getX(), normal.getY(), normal.getZ(), distance));

            if (distance < minDistance) {
                minTriangle = i / 3;
                minDistance = distance;
            }
        }
        return new FaceNormals(normals, minTriangle);
    }

    private static float[] createTransformVertexes(Transform transform, Collider collider) {
        float[] vertexes = collider.getVertexes();
        float[] newVertexes = new float[vertexes.length];
        Vector4f point = new Vector4f();

        Matrix4f transformMatrix = MatrixMath.createTransformationMatrix(transform);
        Vector4f newPoint = new Vector4f();

        for (int i = 0; i < vertexes.length; i += 3) {
            point.set(vertexes[i], vertexes[i + 1], vertexes[i + 2], 1.0f);
            Matrix4f.transform(transformMatrix, point, newPoint);
            newVertexes[i] = newPoint.getX();
            newVertexes[i + 1] = newPoint.getY();
            newVertexes[i + 2] = newPoint.getZ();
        }
        return newVertexes;
    }
}
