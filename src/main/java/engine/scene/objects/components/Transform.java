package engine.scene.objects.components;

import org.json.simple.JSONObject;

public class Transform extends Component {


    public Transform() {
        super(ComponentsList.Transform);
    }

    @Override
    public void loadFromJSON(String path, String filename) {

    }

    @Override
    public void saveToJSON(String path, String filename) {

    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public void fromJSON(JSONObject json) {

    }
}
