package engine.scene.objects.components;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Mesh extends Component {
    private int[] indexes;
    private float[] vertexes;
    private float[] normals;

    public Mesh() {
        super(ComponentsList.MESH);
    }

    public Mesh(int[] indexes, float[] vertexes, float[] normals) {
        this();
        set(indexes, vertexes, normals);
    }

    public void set(int[] indexes, float[] vertexes, float[] normals) {
        this.indexes = indexes.clone();
        this.vertexes = vertexes.clone();
        this.normals = normals.clone();
    }

    public int[] getIndexes() {
        return indexes.clone();
    }

    public float[] getVertexes() {
        return vertexes.clone();
    }

    public float[] getNormals() {
        return normals.clone();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject meshJSON = new JSONObject();
        meshJSON.put("tag", tag.toString());

        JSONArray indexesJSON = new JSONArray();
        JSONArray vertexesJSON = new JSONArray();
        JSONArray normalsJSON = new JSONArray();
        if (indexes != null && vertexes != null && normals != null) {
            for (int i = 0; i < indexes.length; i++) {
                indexesJSON.add(indexes[i]);
            }
            for (int i = 0; i < vertexes.length; i++) {
                vertexesJSON.add(vertexes[i]);
            }
            for (int i = 0; i < normals.length; i++) {
                normalsJSON.add(normals[i]);
            }
        }
        meshJSON.put("indexes", indexesJSON);
        meshJSON.put("vertexes", vertexesJSON);
        meshJSON.put("normals", normalsJSON);

        return meshJSON;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (!json.get("tag").equals("MESH")) throw new IllegalStateException("Попытка загрузить не компонент MESH");
        JSONArray indexesJSON = (JSONArray) json.get("indexes");
        JSONArray vertexesJSON = (JSONArray) json.get("vertexes");
        JSONArray normalsJSON = (JSONArray) json.get("normals");


        indexes = new int[indexesJSON.size()];
        vertexes = new float[vertexesJSON.size()];
        normals = new float[normalsJSON.size()];

        for (int i = 0; i < vertexes.length; i++) {
            indexes[i] = Integer.parseInt(indexesJSON.get(i).toString());
            vertexes[i] = Float.parseFloat(vertexesJSON.get(i).toString());
            normals[i] = Float.parseFloat(normalsJSON.get(i).toString());
        }
    }
}
