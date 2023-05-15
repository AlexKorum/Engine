package engine.scene.objects.components;

import org.json.simple.JSONObject;
import org.lwjgl.util.vector.Vector3f;

public class Transform extends Component {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform() {
        super(ComponentsList.TRANSFORM);
        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1, 1, 1);
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        super(ComponentsList.TRANSFORM);
        this.position = new Vector3f(position);
        this.rotation = new Vector3f(rotation);
        this.scale = new Vector3f(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale.set(scale);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject componentJSON = new JSONObject();
        componentJSON.put("tag", tag.toString());

        JSONObject positionJSON = new JSONObject();
        positionJSON.put("x", position.getX());
        positionJSON.put("y", position.getY());
        positionJSON.put("z", position.getZ());

        JSONObject rotationJSON = new JSONObject();
        rotationJSON.put("x", rotation.getX());
        rotationJSON.put("y", rotation.getY());
        rotationJSON.put("z", rotation.getZ());

        JSONObject scaleJSON = new JSONObject();
        scaleJSON.put("x", scale.getX());
        scaleJSON.put("y", scale.getY());
        scaleJSON.put("z", scale.getZ());

        componentJSON.put("position", positionJSON);
        componentJSON.put("rotation", rotationJSON);
        componentJSON.put("scale", scaleJSON);

        return componentJSON;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (!json.get("tag").equals("TRANSFORM")) throw new IllegalStateException("Попытка загрузить не компонент TRANSFORM");
        JSONObject positionJSON = (JSONObject) json.get("position");
        position.set(Float.parseFloat(positionJSON.get("x").toString()),
                Float.parseFloat(positionJSON.get("y").toString()),
                Float.parseFloat(positionJSON.get("z").toString()));

        JSONObject rotationJSON = (JSONObject) json.get("rotation");
        rotation.set(Float.parseFloat(rotationJSON.get("x").toString()),
                Float.parseFloat(rotationJSON.get("y").toString()),
                Float.parseFloat(rotationJSON.get("z").toString()));

        JSONObject scaleJSON = (JSONObject) json.get("scale");
        scale.set(Float.parseFloat(scaleJSON.get("x").toString()),
                Float.parseFloat(scaleJSON.get("y").toString()),
                Float.parseFloat(scaleJSON.get("z").toString()));
    }

    @Override
    public String toString() {
        return "Transform{" +
                "position=" + position +
                ", rotation=" + rotation +
                ", scale=" + scale +
                '}';
    }
}
