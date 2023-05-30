package engine.scene.objects.components;

import engine.scene.objects.components.enums.ColliderList;
import engine.scene.objects.components.enums.ComponentsList;
import org.json.simple.JSONObject;

public class Collider extends Component {

    private float[] vertexes;
    private ColliderList colliderType;

    public Collider() {
        super(ComponentsList.COLLIDER);
        vertexes = ColliderList.getColliderVertexes(ColliderList.CUBE);
        colliderType = ColliderList.CUBE;
    }


    public void setType(ColliderList collider) {
        vertexes = ColliderList.getColliderVertexes(collider);
        colliderType = collider;
    }

    public float[] getVertexes() {
        return vertexes.clone();
    }

    public void setVertexes(float[] vertexes) {
        this.vertexes = vertexes;
    }


    @Override
    public JSONObject toJSON() {
        JSONObject componentJSON = new JSONObject();
        componentJSON.put("tag", tag.toString());
        componentJSON.put("typeCollider", colliderType.toString());

        return componentJSON;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (!json.get("tag").equals(ComponentsList.COLLIDER.toString()))
            throw new IllegalStateException("Попытка загрузить не компонент COLLIDER");
        colliderType = ColliderList.getColliderType(json.get("typeCollider").toString());
        vertexes = ColliderList.getColliderVertexes(colliderType);
    }
}
