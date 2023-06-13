package engine.physics.GJK;

import engine.scene.objects.Object;
import engine.scene.objects.components.Collider;
import engine.scene.objects.components.Transform;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.util.vector.Vector3f;
import utilities.classes.Pair;

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
        float[] vertexesA = GJK.createTransformVertexes(transformA, colliderA);
        float[] vertexesB = GJK.createTransformVertexes(transformB, colliderB);

        // Получаем симплекс
        ArrayList<Vector3f> vertexes = simplex.getPoints();
        ArrayList<Integer> faces = new ArrayList<>(List.of(facesPattern));

        float minDistance = Float.MAX_VALUE;
        Vector3f minNormal = null;

        while (minDistance == Float.MAX_VALUE) {
            // Находим минимальную нормаль
            Pair<Vector3f, Float> normalInfo = getFaceNormal(vertexes, faces);
            minNormal = normalInfo.getItem1();
            minDistance = normalInfo.getItem2();

            // Находим опорную точку в направлении нормали
            Vector3f support = GJK.support(vertexesA, vertexesB, minNormal);

            // Если точка находится ближе чем Eps, то продолжаем поиск
            if (Vector3f.dot(minNormal, support) - minDistance < 0.01f) {
                break;
            }

            // Устанавливаем minDistance на максимально возможное значение
            minDistance = Float.MAX_VALUE;

            // Дополняем симплекс новой точкой
            addNewPoint(support, vertexes, faces);
        }
        return new CollisionInformation(minNormal, minDistance+0.01f);
    }

    private static void addNewPoint(Vector3f support, ArrayList<Vector3f> vertexes, ArrayList<Integer> faces) {
        // Получаем направление на новую точку
        Vector3f supportDirection = new Vector3f(support);
        supportDirection.normalise();

        // Определяем итерационную нормаль
        Vector3f normal = new Vector3f();

        // Определяем список выбывших треугольников и список новых
        ArrayList<Integer> removeTriangle = new ArrayList<>();
        ArrayList<Integer> addTriangles = new ArrayList<>();

        // Собираем данные о выбывших треугольниках
        for (int i = 0; i < faces.size(); i += 3) {
            Vector3f a = vertexes.get(faces.get(i));
            Vector3f b = vertexes.get(faces.get(i + 1));
            Vector3f c = vertexes.get(faces.get(i + 2));

            Vector3f.cross(
                    Vector3f.sub(b, a, null),
                    Vector3f.sub(c, a, null),
                    normal
            );
            normal.normalise();
            if (Vector3f.dot(normal, a) < 0) {
                normal.negate();
            }

            if (Vector3f.dot(supportDirection, normal) > 0) {
                // Сохраняем индексы треугольника
                removeTriangle.add(faces.get(i));
                removeTriangle.add(faces.get(i + 1));
                removeTriangle.add(faces.get(i + 2));

                // Удаляем индексы треугольника из листа индексов
                faces.remove(i);
                faces.remove(i);
                faces.remove(i);
                i -= 3;
            }
        }

        int facesSize = faces.size();
        // Определяем новые треугольники
        for (int i = 0; i < removeTriangle.size(); i += 3) {
            for (int j = 0; j < facesSize; j += 3) {
                for (int l = 0; l < 3; l++) {
                    for (int k = 0; k < 3; k++) {
                        if (removeTriangle.get(i + l % 3) == faces.get(j + (k + 1) % 3) && removeTriangle.get(i + (l + 1) % 3) == faces.get(j + k % 3)) {
                            faces.add(removeTriangle.get(i + l % 3));
                            faces.add(removeTriangle.get(i + (l + 1) % 3));
                            faces.add(vertexes.size());
                        }
                    }
                }
            }
        }

        // Добавляем точку в симплекс
        vertexes.add(support);
    }

    private static Pair<Vector3f, Float> getFaceNormal(ArrayList<Vector3f> vertexes, ArrayList<Integer> faces) {
        // Переменные для итераций
        Vector3f normal = new Vector3f();
        float distance;

        // Данные для вывода
        Vector3f minNormal = new Vector3f();
        float minDistance = Float.MAX_VALUE;

        for (int i = 0; i < faces.size(); i += 3) {
            // Находим точки полигона
            Vector3f a = vertexes.get(faces.get(i));
            Vector3f b = vertexes.get(faces.get(i + 1));
            Vector3f c = vertexes.get(faces.get(i + 2));

            // Находим нормаль и дистанцию до полигона
            Vector3f.cross(
                    Vector3f.sub(b, a, null),
                    Vector3f.sub(c, a, null),
                    normal
            );
            normal.normalise();
            distance = Vector3f.dot(normal, a);

            // Если дистанция меньше 0, то разворачиваем нормаль и меняем знак дистанций
            if (distance < 0) {
                distance *= -1;
                normal.negate();
            }

            // Находим минимальную нормаль по дистанции
            if (distance < minDistance) {
                minDistance = distance;
                minNormal.set(normal);
            }
        }
        return new Pair<>(minNormal, minDistance);
    }
}