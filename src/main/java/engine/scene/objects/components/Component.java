package engine.scene.objects.components;

import engine.interfaces.ConvertClassToJSON;
import engine.interfaces.ConvertJSONToClass;
import engine.interfaces.LoadFromJSON;
import engine.interfaces.SaveToJSON;

public abstract class Component implements ConvertClassToJSON, ConvertJSONToClass, LoadFromJSON, SaveToJSON {
    protected ComponentsList type;

    public Component(ComponentsList type) {
        this.type = type;
    }
}
