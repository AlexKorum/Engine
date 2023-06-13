package engine.physics;

import engine.interfaces.EngineInterfaces;
import engine.physics.GJK.CollisionInformation;
import engine.physics.GJK.EPA;
import engine.physics.GJK.GJK;
import engine.scene.Scene;
import engine.scene.objects.Object;
import engine.scene.objects.components.Rigidbody;
import engine.scene.objects.components.Transform;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.util.vector.Vector3f;
import utilities.classes.GlobalTimer;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PhysicsEngine implements EngineInterfaces {
    private List<Object> objects;
    private final Vector3f gravity = new Vector3f(0, -1f, 0);
    private final float drag = 1.29f;
    private final float percent = 0.2f;
    private final float slop = 0.1f;

    @Override
    public void init() {
        objects = new ArrayList<>();
    }

    @Override
    public void update() {
        // 1. Гравитация
        addGravity();

        // 2. Разрешение коллизий
        collisionCheck();

        // 3. Шаг симуляций
        stepSimulation();
    }

    private void addGravity() {
        collectionObjectsWithComponent(ComponentsList.RIGIDBODY);

        Vector3f force = new Vector3f();
        Transform transform;
        Rigidbody rigidbody;
        for (int i = 0; i < objects.size(); i++) {
            transform = (Transform) objects.get(i).getComponent(ComponentsList.TRANSFORM);
            rigidbody = (Rigidbody) objects.get(i).getComponent(ComponentsList.RIGIDBODY);
            if (rigidbody.isUseGravity()) {
                force.set(gravity);

//                Vector3f.sub(new Vector3f(), transform.getPosition(), force);
//                force.normalise();
//                force.scale(gravity.length());

                force.scale(rigidbody.getMass());
                rigidbody.addForce(force);
            }
        }

    }

    private void collisionCheck() {
        collectionObjectsWithComponent(ComponentsList.COLLIDER);

        Rigidbody rigidbody1;
        Rigidbody rigidbody2;

        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                if (GJK.detectCollision(objects.get(i), objects.get(j))) {
                    CollisionInformation collisionInformation = EPA.getCollisionInformation(GJK.getSimplex(), objects.get(i), objects.get(j));
                    Vector3f normal = collisionInformation.getNormal();
                    float depth = collisionInformation.getPenetrationDepth();

                    physicsSimulation(objects.get(i), objects.get(j), normal, depth);
                }
            }
        }
    }

    private void physicsSimulation(Object object1, Object object2, Vector3f normal, float depth) {
        Transform transform1 = (Transform) object1.getComponent(ComponentsList.TRANSFORM);
        Transform transform2 = (Transform) object2.getComponent(ComponentsList.TRANSFORM);

        Rigidbody rigidbody1 = (Rigidbody) object1.getComponent(ComponentsList.RIGIDBODY);
        Rigidbody rigidbody2 = (Rigidbody) object2.getComponent(ComponentsList.RIGIDBODY);

        if (rigidbody1 == null && rigidbody2 == null) {
            return;
        }
        if (rigidbody1 == null) {
            rigidbody1 = new Rigidbody();
            rigidbody1.setMass(0);
        }
        if (rigidbody2 == null) {
            rigidbody2 = new Rigidbody();
            rigidbody2.setMass(0);
        }

        // Вычисляем относительную скорость
        Vector3f rv = Vector3f.sub(rigidbody2.getVelocity(), rigidbody1.getVelocity(), null);
        // Вычисляем скалярное произведение между относительной скоростью и нормали
        float velAlongNormal = Vector3f.dot(rv, normal);
        // Не выполняем вычисления если разрешения коллизии, если скорости разделены
        if (velAlongNormal > 0) {
            return;
        }

        // Вычисляем упругость
        float e = Math.min(rigidbody1.getElasticity(), rigidbody2.getElasticity());

        // Вычисляем скаляр импульса силы
        float j = -(1 + e) * velAlongNormal;
        j /= rigidbody1.getInvMass() + rigidbody2.getInvMass();

        // Прикладываем импульс силы
        Vector3f impulse1 = new Vector3f(normal);
        impulse1.scale(j * rigidbody1.getInvMass());
        Vector3f impulse2 = new Vector3f(normal);
        impulse2.scale(j * rigidbody2.getInvMass());

        // Модифицируем скорость
        Vector3f.sub(rigidbody1.getVelocity(), impulse1, rigidbody1.getVelocity());
        Vector3f.add(rigidbody2.getVelocity(), impulse2, rigidbody2.getVelocity());


        // Коррекция положения по глубине погружения
        Vector3f correction1 = new Vector3f(normal);
        correction1.scale(rigidbody1.getInvMass() * Math.max(depth - slop, 0.0f) / (rigidbody1.getInvMass() + rigidbody2.getInvMass()) * percent);
        Vector3f correction2 = new Vector3f(normal);
        correction2.scale(rigidbody2.getInvMass() * Math.max(depth - slop, 0.0f) / (rigidbody1.getInvMass() + rigidbody2.getInvMass()) * percent);

        Vector3f.sub(transform1.getPosition(), correction1, transform1.getPosition());
        Vector3f.add(transform2.getPosition(), correction2, transform2.getPosition());
    }


    private void stepSimulation() {
        collectionObjectsWithComponent(ComponentsList.RIGIDBODY);

        Transform transform;
        Rigidbody rigidbody;

        Vector3f velocityNegDir = new Vector3f();

        for (int i = 0; i < objects.size(); i++) {
            transform = (Transform) objects.get(i).getComponent(ComponentsList.TRANSFORM);
            rigidbody = (Rigidbody) objects.get(i).getComponent(ComponentsList.RIGIDBODY);

            if (rigidbody.getVelocity().length() > 0.1f) {
                velocityNegDir.set(rigidbody.getVelocity());
                velocityNegDir.negate();
                velocityNegDir.normalise();

                velocityNegDir.scale(drag * velocityNegDir.length() / 2);
                rigidbody.addForce(velocityNegDir);
            }

            rigidbody.updateVelocity();
            if (rigidbody.getVelocity().length() > 0.1f)
                transform.getPosition().translate(rigidbody.getVelocity().getX(), rigidbody.getVelocity().getY(), rigidbody.getVelocity().getZ());
        }
    }

    private void collectionObjectsWithComponent(ComponentsList component) {
        this.objects.clear();
        List<Object> objects = Scene.getInstance().getObjects();
        Object object;
        for (int i = 0; i < objects.size(); i++) {
            object = objects.get(i);
            if (object.getComponent(component) != null) {
                this.objects.add(object);
            }
        }
    }

}
