package engine.scene.objects.components;

public enum ComponentsList {
    TRANSFORM, MESH, MATERIAL;

    public static Component getComponent(ComponentsList componentName) {
        Component component;
        switch (componentName) {
            case TRANSFORM -> component = new Transform();
            case MESH -> component = new Mesh();
            case MATERIAL -> component = new Material();
            default -> component = null;
        }
        return component;
    }

    public static Component getComponent(String nameComponent) {
        nameComponent = nameComponent.toUpperCase();
        ComponentsList component = ComponentsList.valueOf(nameComponent);
        return getComponent(component);
    }

}
