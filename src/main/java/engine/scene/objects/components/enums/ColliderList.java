package engine.scene.objects.components.enums;

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
        return new float[]{
                1.000000f, 1.000000f, -1.000000f,
                1.000000f, -1.000000f, -1.000000f,
                1.000000f, 1.000000f, 1.000000f,
                1.000000f, -1.000000f, 1.000000f,
                -1.000000f, 1.000000f, -1.000000f,
                -1.000000f, -1.000000f, -1.000000f,
                -1.000000f, 1.000000f, 1.000000f,
                -1.000000f, -1.000000f, 1.000000f
        };
    }

    private static float[] getSphereCollider() {
        return new float[]{
                0.000000f, -1.000000f, 0.000000f,
                0.723607f, -0.447220f, 0.525725f,
                -0.276388f, -0.447220f, 0.850649f,
                -0.894426f, -0.447216f, 0.000000f,
                -0.276388f, -0.447220f, -0.850649f,
                0.723607f, -0.447220f, -0.525725f,
                0.276388f, 0.447220f, 0.850649f,
                -0.723607f, 0.447220f, 0.525725f,
                -0.723607f, 0.447220f, -0.525725f,
                0.276388f, 0.447220f, -0.850649f,
                0.894426f, 0.447216f, 0.000000f,
                0.000000f, 1.000000f, 0.000000f,
                -0.162456f, -0.850654f, 0.499995f,
                0.425323f, -0.850654f, 0.309011f,
                0.262869f, -0.525738f, 0.809012f,
                0.850648f, -0.525736f, 0.000000f,
                0.425323f, -0.850654f, -0.309011f,
                -0.525730f, -0.850652f, 0.000000f,
                -0.688189f, -0.525736f, 0.499997f,
                -0.162456f, -0.850654f, -0.499995f,
                -0.688189f, -0.525736f, -0.499997f,
                0.262869f, -0.525738f, -0.809012f,
                0.951058f, 0.000000f, 0.309013f,
                0.951058f, 0.000000f, -0.309013f,
                0.000000f, 0.000000f, 1.000000f,
                0.587786f, 0.000000f, 0.809017f,
                -0.951058f, 0.000000f, 0.309013f,
                -0.587786f, 0.000000f, 0.809017f,
                -0.587786f, 0.000000f, -0.809017f,
                -0.951058f, 0.000000f, -0.309013f,
                0.587786f, 0.000000f, -0.809017f,
                0.000000f, 0.000000f, -1.000000f,
                0.688189f, 0.525736f, 0.499997f,
                -0.262869f, 0.525738f, 0.809012f,
                -0.850648f, 0.525736f, 0.000000f,
                -0.262869f, 0.525738f, -0.809012f,
                0.688189f, 0.525736f, -0.499997f,
                0.162456f, 0.850654f, 0.499995f,
                0.525730f, 0.850652f, 0.000000f,
                -0.425323f, 0.850654f, 0.309011f,
                -0.425323f, 0.850654f, -0.309011f,
                0.162456f, 0.850654f, -0.499995f
        };
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
