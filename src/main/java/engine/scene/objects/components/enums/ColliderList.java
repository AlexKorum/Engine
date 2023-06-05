package engine.scene.objects.components.enums;

import utilities.Loaders.LoaderOBJ;

public enum ColliderList {
    CUBE, SPHERE, PLANE;

    public static float[] getColliderVertexes(ColliderList collider) {
        float[] vertexes;
        switch (collider) {
            case SPHERE -> vertexes = getSphereCollider();
            case PLANE -> vertexes = getPlaneCollider();
            default -> vertexes = getCubeCollider();
        }
        return vertexes;
    }

    public static ColliderList getColliderType(String nameColliderType) {
        nameColliderType = nameColliderType.toUpperCase();
        ColliderList colliderType = ColliderList.valueOf(nameColliderType);
        return colliderType;
    }

    private static float[] getCubeCollider() {
        return LoaderOBJ.getColliderVertex("src\\main\\resources\\Assets\\Prefabs\\Colliders\\OBJ\\CubeCollider.obj");
    }

    private static float[] getSphereCollider() {
        return LoaderOBJ.getColliderVertex("src\\main\\resources\\Assets\\Prefabs\\Colliders\\OBJ\\SphereCollider.obj");
    }

    public static float[] getPlaneCollider() {
        return new float[]{
                -100.000000f, 0.000000f, 100.000000f,
                100.000000f, 0.000000f, 100.000000f,
                -100.000000f, 0.000000f, -100.000000f,
                100.000000f, 0.000000f, -100.000000f
        };
    }
}
