package engine.scene.objects.components;

import engine.scene.objects.components.enums.ComponentsList;
import org.json.simple.JSONObject;
import org.lwjgl.util.vector.Vector3f;

public class Material extends Component {
    private Vector3f color;
    private float reflectivity;
    private float shineDamping;

    public Material() {
        super(ComponentsList.MATERIAL);
        color = new Vector3f(178f / 255f, 180f / 255f, 180f / 255f);
        reflectivity = 1f;
        shineDamping = 10f;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color.set(color);
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public float getShineDamping() {
        return shineDamping;
    }

    public void setShineDamping(float shineDamping) {
        this.shineDamping = shineDamping;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject componentJSON = new JSONObject();
        componentJSON.put("tag", tag.toString());

        JSONObject colorJSON = new JSONObject();
        colorJSON.put("r", color.getX());
        colorJSON.put("g", color.getY());
        colorJSON.put("b", color.getZ());
        componentJSON.put("color", colorJSON);

        componentJSON.put("reflectivity", reflectivity);
        componentJSON.put("shineDamping", shineDamping);

        return componentJSON;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (!json.get("tag").equals("MATERIAL")) throw new IllegalStateException("Попытка загрузить не компонент MATERIAL");
        JSONObject colorJSON = (JSONObject) json.get("color");
        color.set(Float.parseFloat(colorJSON.get("r").toString()),
                Float.parseFloat(colorJSON.get("g").toString()),
                Float.parseFloat(colorJSON.get("b").toString()));

        reflectivity = Float.parseFloat(json.get("reflectivity").toString());
        shineDamping = Float.parseFloat(json.get("shineDamping").toString());
    }
}
