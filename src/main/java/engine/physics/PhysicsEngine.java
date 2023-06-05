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

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine implements EngineInterfaces {
    private List<Object> objects;
    //    private final Vector3f gravity = new Vector3f(0, -9.81f, 0);
    private final Vector3f gravity = new Vector3f(0, -0.05f, 0);

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
        Rigidbody rigidbody;
        for (int i = 0; i < objects.size(); i++) {
            rigidbody = (Rigidbody) objects.get(i).getComponent(ComponentsList.RIGIDBODY);
            if (rigidbody.isUseGravity()) {
                force.set(gravity);
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
                    rigidbody1 = (Rigidbody) objects.get(i).getComponent(ComponentsList.RIGIDBODY);
                    rigidbody2 = (Rigidbody) objects.get(j).getComponent(ComponentsList.RIGIDBODY);

                    Vector3f normal = collisionInformation.getNormal();
                    System.out.println(normal);


                }
            }
        }
    }


    private void stepSimulation() {
        collectionObjectsWithComponent(ComponentsList.RIGIDBODY);

        Transform transform;
        Rigidbody rigidbody;

        for (int i = 0; i < objects.size(); i++) {
            transform = (Transform) objects.get(i).getComponent(ComponentsList.TRANSFORM);
            rigidbody = (Rigidbody) objects.get(i).getComponent(ComponentsList.RIGIDBODY);

            rigidbody.updateVelocity();
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
