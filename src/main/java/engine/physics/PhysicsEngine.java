package engine.physics;

import engine.interfaces.EngineInterfaces;
import engine.physics.GJK.CollisionInformation;
import engine.physics.GJK.EPA;
import engine.physics.GJK.GJK;
import engine.scene.Scene;
import engine.scene.objects.Object;
import engine.scene.objects.components.Material;
import engine.scene.objects.components.enums.ComponentsList;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine implements EngineInterfaces {
    private ArrayList<Object> objects;

    @Override
    public void init() {
        objects = new ArrayList<>();
        collectionObjectsWithCollider();
    }

    @Override
    public void update() {
        collectionObjectsWithCollider();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                if (GJK.detectCollision(objects.get(i), objects.get(j))) {
                    ((Material) objects.get(i).getComponent(ComponentsList.MATERIAL)).setColor(1, 0, 0);
                    ((Material) objects.get(j).getComponent(ComponentsList.MATERIAL)).setColor(1, 0, 0);

                    CollisionInformation collisionInformation = EPA.getCollisionInformation(GJK.getSimplex(), objects.get(i), objects.get(j));
                    System.out.println(collisionInformation);

                } else {
                    ((Material) objects.get(i).getComponent(ComponentsList.MATERIAL)).setColor(178f / 255f, 180f / 255f, 180f / 255f);
                    ((Material) objects.get(j).getComponent(ComponentsList.MATERIAL)).setColor(178f / 255f, 180f / 255f, 180f / 255f);
                }
            }
        }


    }

    private void collectionObjectsWithCollider() {
        objects.clear();
        List<Object> objectsWithScene = Scene.getInstance().getObjects();
        for (Object object : objectsWithScene) {
            if (object.getComponent(ComponentsList.COLLIDER) != null) {
                objects.add(object);
            }
        }
    }
}
