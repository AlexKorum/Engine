package engine.physics.GJK;

import org.lwjgl.util.vector.Vector3f;

public class CollisionInformation {
    private Vector3f normal;
    private float penetrationDepth;

    public CollisionInformation() {
        normal = new Vector3f();
        penetrationDepth = 0;
    }

    public void set(Vector3f normal, float penetrationDepth) {
        this.normal.set(normal);
        this.penetrationDepth = penetrationDepth;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public float getPenetrationDepth() {
        return penetrationDepth;
    }

    @Override
    public String toString() {
        return "CollisionInformation{\n" +
                "normal=" + normal.toString() +
                ",\npenetrationDepth=" + penetrationDepth +
                "\n}\n";
    }
}
