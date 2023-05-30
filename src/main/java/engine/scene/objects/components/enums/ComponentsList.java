package engine.scene.objects.components.enums;

import engine.scene.objects.components.*;
import engine.scene.objects.components.Collider;

public enum ComponentsList {
    TRANSFORM, MESH, MATERIAL, COLLIDER;

    public static Component getComponent(ComponentsList componentName) {
        Component component;
        switch (componentName) {
            case TRANSFORM -> component = new Transform();
            case MESH -> component = new Mesh();
            case MATERIAL -> component = new Material();
            case COLLIDER -> component = new Collider();
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
