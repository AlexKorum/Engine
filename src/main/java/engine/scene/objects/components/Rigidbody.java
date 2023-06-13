package engine.scene.objects.components;

import engine.scene.objects.components.enums.ComponentsList;
import org.json.simple.JSONObject;
import org.lwjgl.util.vector.Vector3f;
import utilities.classes.GlobalTimer;

public class Rigidbody extends Component {
    private float mass;
    private float elasticity;
    private boolean useGravity;
    private Vector3f velocity;
    private Vector3f force;

    public Rigidbody() {
        super(ComponentsList.RIGIDBODY);
        mass = 1;
        elasticity = 0.1f;
        useGravity = true;
        velocity = new Vector3f();
        force = new Vector3f();
    }

    public void addForce(Vector3f newForce) {
        Vector3f.add(force, newForce, force);
    }

    public void updateVelocity() {
        force.scale(1 / mass * GlobalTimer.getDeltaTimeSeconds());
        Vector3f.add(velocity, force, velocity);
        force.set(0, 0, 0);
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public Vector3f getForce() {
        return force;
    }

    public float getElasticity() {
        return elasticity;
    }

    public void setElasticity(float elasticity) {
        this.elasticity = elasticity;
    }

    public float getMass() {
        return mass;
    }

    public float getInvMass() {
        if (mass == 0) {
            return 0;
        } else {
            return mass;
        }
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public boolean isUseGravity() {
        return useGravity;
    }

    public void setUseGravity(boolean useGravity) {
        this.useGravity = useGravity;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("mass", mass);
        json.put("useGravity", useGravity ? 1 : 0);

        JSONObject velocityJson = new JSONObject();
        velocityJson.put("x", velocity.getX());
        velocityJson.put("y", velocity.getY());
        velocityJson.put("z", velocity.getZ());

        json.put("velocity", velocityJson);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json) {
        if (!json.get("tag").equals("RIGIDBODY"))
            throw new IllegalStateException("Попытка загрузить не компонент RIGIDBODY");
        mass = Float.parseFloat(json.get("mass").toString());
        useGravity = Integer.parseInt(json.get("useGravity").toString()) == 1;
        JSONObject velocityJson = (JSONObject) json.get("velocity");
        velocity.set(Float.parseFloat(velocityJson.get("x").toString()), Float.parseFloat(velocityJson.get("y").toString()), Float.parseFloat(velocityJson.get("z").toString()));
    }
}
