package engine.physics.GJK;

import engine.scene.objects.Object;
import engine.scene.objects.components.Collider;
import engine.scene.objects.components.Transform;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utilities.classes.Pair;
import utilities.math.MatrixMath;

import java.util.ArrayList;
import java.util.List;

public class EPA {

    private final static Integer[] facesPattern = new Integer[]{0, 1, 2, 0, 3, 1, 0, 2, 3, 1, 3, 2};

    public static CollisionInformation getCollisionInformation(Simplex simplex, Object objectA, Object objectB) {
        // Получение данных модели
        Transform transformA = (Transform) objectA.getComponent(ComponentsList.TRANSFORM);
        Transform transformB = (Transform) objectB.getComponent(ComponentsList.TRANSFORM);
        Collider colliderA = (Collider) objectA.getComponent(ComponentsList.COLLIDER);
        Collider colliderB = (Collider) objectB.getComponent(ComponentsList.COLLIDER);
        float[] vertexesA = createTransformVertexes(transformA, colliderA);
        float[] vertexesB = createTransformVertexes(transformB, colliderB);

        // Получаем симплекс
        ArrayList<Vector3f> vertexes = simplex.getPoints();
        ArrayList<Integer> faces = new ArrayList<>(List.of(facesPattern));

        Pair<Pair<Vector3f, Float>, Integer> normalInfo;
        Vector3f normal = null;
        float minDistance = Float.MAX_VALUE;
        int i = 0;
        while (minDistance == Float.MAX_VALUE) {
            // Получаем информацию о минимальной нормали
            normalInfo = getNormalMinTriangle(vertexes, faces);
            normal = normalInfo.getItem1().getItem1();
            minDistance = normalInfo.getItem1().getItem2();

            // Находим новую точку в направлении нормали
            Vector3f support = GJK.support(vertexesA, vertexesB, normal);

            // Если новая точка лежит ближе к началу координат чем полигон, то она находится внутри него и поиск заканчивается
            if (Math.abs(Vector3f.dot(normal, support) - minDistance) <= 0.01f) {
                break;
            }
            minDistance = Float.MAX_VALUE;

            // Добавляем новую точку к многограннику
            addNewVertex(vertexes, faces, support);
            i++;
        }

        return new CollisionInformation(normal, minDistance + 0.01f);
    }

    private static void addNewVertex(ArrayList<Vector3f> vertexes, ArrayList<Integer> faces, Vector3f support) {
        // 1. Найти ребра которые видны из точки
        // 2. Удалить ребра относящиеся ко внутренним точкам
        // 3. Сформировать новые индексы с учетом новой точки

        // Получаем список индексов вершин "видных" из новой точки
        ArrayList<Pair<Integer, Integer>> boundaryEdges = getBoundaryFaces(vertexes, faces, support);
        for (Pair<Integer, Integer> edge : boundaryEdges) {
            faces.add(edge.getItem1());
            faces.add(edge.getItem2());
            faces.add(vertexes.size());
        }
        vertexes.add(support);
    }

    private static ArrayList<Pair<Integer, Integer>> getBoundaryFaces(ArrayList<Vector3f> vertexes, ArrayList<Integer> faces, Vector3f support) {
        // Объявляем список индексов точек
        ArrayList<Pair<Integer, Integer>> edge = new ArrayList<>();

        // Объявляем точки треугольника
        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        Vector3f c = new Vector3f();

        // Объявляем нормаль и дистанцию
        Vector3f normal = new Vector3f();
        float distance;

        for (int i = 0; i < faces.size(); i += 3) {
            // Получаем координаты точек треугольника
            a.set(vertexes.get(faces.get(i)));
            b.set(vertexes.get(faces.get(i + 1)));
            c.set(vertexes.get(faces.get(i + 2)));

            // Находим нормаль  расстояние
            Vector3f.cross(
                    Vector3f.sub(b, a, null),
                    Vector3f.sub(c, a, null),
                    normal
            );

            distance = Vector3f.dot(normal, a);

            if (distance < 0) {
                normal.negate();
                distance *= -1;
            }

            // Проверяем видно ли треугольник с точки
            if (Vector3f.dot(normal, support) <= distance) {
                continue;
            }


            // Добавляем треугольники которые видно из точки в список
            edge.add(new Pair<>(faces.get(i), faces.get(i + 1)));
            edge.add(new Pair<>(faces.get(i + 1), faces.get(i + 2)));
            edge.add(new Pair<>(faces.get(i + 2), faces.get(i)));

            faces.remove(i);
            faces.remove(i);
            faces.remove(i);
        }

        ArrayList<Pair<Integer, Integer>> boundaryEdges = new ArrayList<>();

        for (Pair pair : edge) {
            for (int i = 0; i < faces.size(); i += 3) {
                if ((pair.getItem2() == faces.get(i) && pair.getItem1() == faces.get(i + 1)) ||
                        (pair.getItem2() == faces.get(i + 1) && pair.getItem1() == faces.get(i + 2)) ||
                        (pair.getItem2() == faces.get(i + 2) && pair.getItem1() == faces.get(i))) {
                    boundaryEdges.add(pair);
                    break;
                }
            }
        }


        return boundaryEdges;
    }

    private static Pair<Pair<Vector3f, Float>, Integer> getNormalMinTriangle(ArrayList<Vector3f> vertexes, ArrayList<Integer> indexes) {
        Vector3f minNormal = new Vector3f();
        int minIndexVertexInTriangle = -1;

        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        Vector3f c = new Vector3f();

        Vector3f normal = new Vector3f();

        Vector3f p = new Vector3f();
        Vector3f p1 = new Vector3f();
        Vector3f p2 = new Vector3f();

        float distance;
        float minDistance = Float.MAX_VALUE;


        for (int i = 0; i < indexes.size(); i += 3) {
            // Сохраняем точки треугольника
            a.set(vertexes.get(indexes.get(i)));
            b.set(vertexes.get(indexes.get(i + 1)));
            c.set(vertexes.get(indexes.get(i + 2)));

            // Находим нормаль к поверхности
            Vector3f.cross(
                    Vector3f.sub(b, a, null),
                    Vector3f.sub(c, a, null),
                    normal
            );
            if (normal.getX() == 0 && normal.getY() == 0 && normal.getZ() == 0) continue;
            normal.normalise();

            // Определяем расстояние от начала координат до полигона
            distance = Vector3f.dot(normal, a);
            if (distance < 0) {
                normal.negate();
                distance *= -1;
            }

            // Находим проекций начала координат на плоскость
            Vector3f.cross(
                    Vector3f.sub(b, a, null),
                    normal,
                    p
            );
            Vector3f.cross(
                    Vector3f.sub(c, b, null),
                    normal,
                    p1
            );
            Vector3f.cross(
                    Vector3f.sub(a, c, null),
                    normal,
                    p2
            );

            if (Vector3f.dot(p, a) <= distance && Vector3f.dot(p, b) <= distance && Vector3f.dot(p, c) <= distance) {
                continue;
            }

            // Определяем минимальное расстояние и индекс точки начала полигона
            if (distance < minDistance) {
                minDistance = distance;
                minIndexVertexInTriangle = i / 3;
                minNormal.set(normal);
            }
        }
        return new Pair<>(new Pair<>(minNormal, minDistance), minIndexVertexInTriangle);
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
