package engine.physics.GJK;

import engine.scene.objects.Object;
import engine.scene.objects.components.Collider;
import engine.scene.objects.components.Transform;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utilities.math.MatrixMath;


public class GJK {
    private static Simplex points;

    public static boolean detectCollision(Object objectA, Object objectB) {
        // Получаем данные от объектов
        Transform transformA = (Transform) objectA.getComponent(ComponentsList.TRANSFORM);
        Transform transformB = (Transform) objectB.getComponent(ComponentsList.TRANSFORM);
        Collider colliderA = (Collider) objectA.getComponent(ComponentsList.COLLIDER);
        Collider colliderB = (Collider) objectB.getComponent(ComponentsList.COLLIDER);
        float[] vertexesA = createTransformVertexes(transformA, colliderA);
        float[] vertexesB = createTransformVertexes(transformB, colliderB);

        // Определяем первое направление
        Vector3f direction = new Vector3f();
        Vector3f.sub(transformB.getPosition(), transformA.getPosition(), direction);

        // Получаем точку опоры по направлению d
        Vector3f supportPoint = support(vertexesA, vertexesB, direction);
        // Объявляем симплекс
        points = new Simplex();
        points.pushToFront(supportPoint);

        // Разворачиваем вектор направления к началу координат
        direction.set(-supportPoint.getX(), -supportPoint.getY(), -supportPoint.getZ());

        while (true) {
            supportPoint = support(vertexesA, vertexesB, direction);
            if (Vector3f.dot(supportPoint, direction) <= 0) {
                return false; // Нет коллизии
            }

            points.pushToFront(supportPoint);

            if (nextSimplex(points, direction)) return true;
        }
    }

    public static Simplex getSimplex() {
        return points;
    }

    private static boolean nextSimplex(Simplex points, Vector3f direction) {
        switch (points.getSize()) {
            case 2:
                return Line(points, direction);
            case 3:
                return Triangle(points, direction);
            case 4:
                return Tetrahedron(points, direction);
        }
        // Никогда не будет использованною
        return false;
    }

    private static boolean Line(Simplex points, Vector3f direction) {
        Vector3f a = points.getPoint(0);
        Vector3f b = points.getPoint(1);

        Vector3f ab = Vector3f.sub(b, a, null);
        Vector3f ao = new Vector3f(-a.getX(), -a.getY(), -a.getZ());

        if (sameDirection(ab, ao)) {
            direction.set(Vector3f.cross(Vector3f.cross(ab, ao, null), ab, null));
        } else {
            points.set(new Vector3f[]{a});
            direction.set(ao);
        }
        return false;
    }

    private static boolean Triangle(Simplex points, Vector3f direction) {
        Vector3f a = points.getPoint(0);
        Vector3f b = points.getPoint(1);
        Vector3f c = points.getPoint(2);

        Vector3f ab = Vector3f.sub(b, a, null);
        Vector3f ac = Vector3f.sub(c, a, null);
        Vector3f ao = new Vector3f(-a.getX(), -a.getY(), -a.getZ());

        Vector3f abc = Vector3f.cross(ab, ac, null);

        if (sameDirection(Vector3f.cross(abc, ac, null), ao)) {
            if (sameDirection(ac, ao)) {
                points.set(new Vector3f[]{a, c});
                direction.set(Vector3f.cross(Vector3f.cross(ac, ao, null), ac, null));
            } else {
                points.set(new Vector3f[]{a, b});
                return Line(points, direction);
            }
        } else {
            if (sameDirection(Vector3f.cross(ab, abc, null), ao)) {
                points.set(new Vector3f[]{a, b});
                return Line(points, direction);
            } else {
                if (sameDirection(abc, ao)) {
                    direction.set(abc);
                } else {
                    points.set(new Vector3f[]{a, c, b});
                    direction.set(-abc.getX(), -abc.getY(), -abc.getZ());
                }
            }
        }
        return false;
    }

    private static boolean Tetrahedron(Simplex points, Vector3f direction) {
        Vector3f a = points.getPoint(0);
        Vector3f b = points.getPoint(1);
        Vector3f c = points.getPoint(2);
        Vector3f d = points.getPoint(3);

        Vector3f ab = Vector3f.sub(b, a, null);
        Vector3f ac = Vector3f.sub(c, a, null);
        Vector3f ad = Vector3f.sub(d, a, null);
        Vector3f ao = new Vector3f(-a.getX(), -a.getY(), -a.getZ());

        Vector3f abc = Vector3f.cross(ab, ac, null);
        Vector3f acd = Vector3f.cross(ac, ad, null);
        Vector3f adb = Vector3f.cross(ad, ab, null);

        if (sameDirection(abc, ao)) {
            points.set(new Vector3f[]{a, b, c});
            return Triangle(points, direction);
        }
        if (sameDirection(acd, ao)) {
            points.set(new Vector3f[]{a, c, d});
            return Triangle(points, direction);
        }
        if (sameDirection(adb, ao)) {
            points.set(new Vector3f[]{a, d, b});
            return Triangle(points, direction);
        }

        return true;
    }

    private static boolean sameDirection(Vector3f direction, Vector3f ao) {
        return Vector3f.dot(direction, ao) > 0;
    }

    private static Vector3f support(float[] vertexesA, float[] vertexesB, Vector3f direction) {
        Vector3f vectorA = findFurthestPoint(vertexesA, direction);
        Vector3f dNeg = new Vector3f(-direction.getX(), -direction.getY(), -direction.getZ());
        Vector3f vectorB = findFurthestPoint(vertexesB, dNeg);

        Vector3f result = new Vector3f();
        Vector3f.sub(vectorA, vectorB, result);
        return result;
    }

    private static Vector3f findFurthestPoint(float[] vertexes, Vector3f direction) {
        Vector3f maxPoint = new Vector3f();
        Vector3f stepPoint = new Vector3f();
        float maxDot = -Float.MAX_VALUE;
        float dot;
        for (int i = 0; i < vertexes.length; i += 3) {
            stepPoint.set(vertexes[i], vertexes[i + 1], vertexes[i + 2]);
            dot = Vector3f.dot(stepPoint, direction);
            if (dot > maxDot) {
                maxDot = dot;
                maxPoint.set(stepPoint);
            }
        }
        return maxPoint;
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
