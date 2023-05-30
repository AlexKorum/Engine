package engine.scene.objects.components;

import engine.interfaces.ConvertClassToJSON;
import engine.interfaces.ConvertJSONToClass;
import engine.scene.objects.components.enums.ComponentsList;

public abstract class Component implements ConvertClassToJSON, ConvertJSONToClass {
    protected ComponentsList tag;

    public Component(ComponentsList tag) {
        this.tag = tag;
    }

    public ComponentsList getTag() {
        return tag;
    }
}
