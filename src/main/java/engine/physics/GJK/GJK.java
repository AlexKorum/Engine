package engine.physics.GJK;

import engine.scene.objects.Object;
import engine.scene.objects.components.Collider;
import engine.scene.objects.components.Transform;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utilities.math.MatrixMath;

import java.util.Random;


public class GJK {
    private final static Simplex points = new Simplex();
    private final static Random random = new Random(System.nanoTime());

    public static boolean detectCollision(Object objectA, Object objectB) {
        // Получаем данные от объектов
        Transform transformA = (Transform) objectA.getComponent(ComponentsList.TRANSFORM);
        Transform transformB = (Transform) objectB.getComponent(ComponentsList.TRANSFORM);
        Collider colliderA = (Collider) objectA.getComponent(ComponentsList.COLLIDER);
        Collider colliderB = (Collider) objectB.getComponent(ComponentsList.COLLIDER);
        float[] vertexesA = createTransformVertexes(transformA, colliderA);
        float[] vertexesB = createTransformVertexes(transformB, colliderB);

        // Вводим начальный вектор направления поиска
        Vector3f subPositionObject = Vector3f.sub(transformA.getPosition(), transformB.getPosition(), null);
        Vector3f direction = subPositionObject.equals(new Vector3f(0, 0, 0)) ? new Vector3f(0, 1, 0) : subPositionObject;

        // Получаем точку опоры в направлении Direction
        Vector3f support = support(vertexesA, vertexesB, direction);

        // Создаем симплекс. Симплекс - это, набор точек окружающих начало координат, максимальное количество точек в 3д пространстве 4 (Тетраэдр)
        Simplex simplex = new Simplex();
        simplex.pushToFront(support);

        // Direction устанавливаем в направлении начала координат
        direction.set(-support.getX(), -support.getY(), -support.getZ());

        // Ищем новые точки
        while (true) {
            // Находим опорную точку в новом направлении
            support = support(vertexesA, vertexesB, direction);

            // Проверяем, прошла ли точка начало координат
            if (Vector3f.dot(support, direction) <= 0) {
                return false;
            }

            // Добавляем точку в симплекс
            simplex.pushToFront(support);

            // Проверяем находится ли точка внутри симплекса
            if (nextSimplex(simplex, direction)) {
                points.set(simplex.getPoints());
                return true;
            }
        }
    }

    private static boolean nextSimplex(Simplex simplex, Vector3f direction) {
        switch (simplex.getSize()) {
            case 2:
                return line(simplex, direction);
            case 3:
                return triangle(simplex, direction);
            case 4:
                return tetrahedron(simplex, direction);
        }

        // Никогда не должны доходить до этого
        return false;
    }

    private static boolean line(Simplex simplex, Vector3f direction) {
        Vector3f a = simplex.getPoint(0);
        Vector3f b = simplex.getPoint(1);

        Vector3f ab = Vector3f.sub(b, a, null);
        Vector3f ao = Vector3f.sub(new Vector3f(0, 0, 0), a, null);


        if (sameDirection(ab, ao)) {
            // TODO Порядок умножений
            Vector3f.cross(
                    Vector3f.cross(ab, ao, null),
                    ab,
                    direction
            );
        } else {
            simplex.set(new Vector3f[]{a});
            direction.set(ao);
        }
        return false;
    }

    private static boolean triangle(Simplex simplex, Vector3f direction) {
        Vector3f a = simplex.getPoint(0);
        Vector3f b = simplex.getPoint(1);
        Vector3f c = simplex.getPoint(2);

        Vector3f ab = Vector3f.sub(b, a, null);
        Vector3f ac = Vector3f.sub(c, a, null);
        Vector3f ao = Vector3f.sub(new Vector3f(0, 0, 0), a, null);

        Vector3f abc = Vector3f.cross(ab, ac, null);

        if (sameDirection(Vector3f.cross(abc, ac, null), ao)) {
            if (sameDirection(ac, ao)) {
                simplex.set(new Vector3f[]{a, c});
                // TODO проверить порядок умножения
                Vector3f.cross(
                        Vector3f.cross(ac, ao, null),
                        ac,
                        direction
                );
            } else {
                simplex.set(new Vector3f[]{a, b});
                return line(simplex, direction);
            }
        } else {
            if (sameDirection(Vector3f.cross(ab, abc, null), ao)) {
                simplex.set(new Vector3f[]{a, b});
                return line(simplex, direction);
            } else {
                if (sameDirection(abc, ao)) {
                    direction.set(abc);
                } else {
                    // TODO Порядок {a,c,b} или {a,b,c}
                    simplex.set(new Vector3f[]{a, b, c});
                    direction.set(abc);
                    direction.negate();
                }
            }
        }
        return false;
    }

    private static boolean tetrahedron(Simplex simplex, Vector3f direction) {
        Vector3f a = simplex.getPoint(0);
        Vector3f b = simplex.getPoint(1);
        Vector3f c = simplex.getPoint(2);
        Vector3f d = simplex.getPoint(3);

        Vector3f ab = Vector3f.sub(b, a, null);
        Vector3f ac = Vector3f.sub(c, a, null);
        Vector3f ad = Vector3f.sub(d, a, null);
        Vector3f ao = Vector3f.sub(new Vector3f(0, 0, 0), a, null);

        Vector3f abc = Vector3f.cross(ab, ac, null);
        Vector3f acd = Vector3f.cross(ac, ad, null);
        Vector3f adb = Vector3f.cross(ad, ab, null);

        if (sameDirection(abc, ao)) {
            simplex.set(new Vector3f[]{a, b, c});
            return triangle(simplex, direction);
        }
        if (sameDirection(acd, ao)) {
            simplex.set(new Vector3f[]{a, c, d});
            return triangle(simplex, direction);
        }
        if (sameDirection(adb, ao)) {
            simplex.set(new Vector3f[]{a, d, b});
            return triangle(simplex, direction);
        }
        return true;
    }

    private static boolean sameDirection(Vector3f direction, Vector3f ao) {
        return Vector3f.dot(direction, ao) > 0;
    }

    public static Vector3f support(float[] vertexA, float[] vertexB, Vector3f direction) {
        Vector3f nextPointA = findFurthestPoint(vertexA, direction);
        Vector3f directionNeg = new Vector3f(-direction.getX(), -direction.getY(), -direction.getZ());
        Vector3f nextPointB = findFurthestPoint(vertexB, directionNeg);
        return Vector3f.sub(nextPointA, nextPointB, null);
    }

    private static Vector3f findFurthestPoint(float[] vertexes, Vector3f direction) {
        Vector3f point = new Vector3f();
        Vector3f maxPoint = new Vector3f();
        float maxDistance = -Float.MAX_VALUE;
        float distance;

        for (int i = 0; i < vertexes.length; i += 3) {
            point.set(vertexes[i], vertexes[i + 1], vertexes[i + 2]);
            distance = Vector3f.dot(direction, point);
            // TODO Уточнить нужно ли равенство
            if (distance >= maxDistance) {
                maxPoint.set(point);
                maxDistance = distance;
            }
        }
        return maxPoint;
    }

    public static float[] createTransformVertexes(Transform transform, Collider collider) {
        float[] vertexes = collider.getVertexes();
        float[] newVertexes = new float[vertexes.length];
        Vector4f point = new Vector4f();
        Transform transformMod = new Transform(transform.getPosition(), transform.getRotation(), transform.getScale());
        float s = 1000;
        transformMod.getPosition().translate((random.nextFloat() - 0.5f) / s, (random.nextFloat() - 0.5f) / s, (random.nextFloat() - 0.5f) / s);

        Matrix4f transformMatrix = MatrixMath.createTransformationMatrix(transformMod);
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

    public static Simplex getSimplex() {
        return points;
    }
}
