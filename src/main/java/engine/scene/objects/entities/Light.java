package engine.scene.objects.entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f color;

    public Light() {
        position = new Vector3f(-200, 500, 200);
        color = new Vector3f(1, 1, 1);
    }

    public Light(Vector3f position, Vector3f color) {
        this();
        this.position.set(position);
        this.color.set(color);
        this.color.normalise();
    }

    public Vector3f getPosition() {
        Vector3f position = new Vector3f(this.position);
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public Vector3f getColor() {
        Vector3f color = new Vector3f(this.color);
        return color;
    }

    public void setColor(Vector3f color) {
        this.color.set(color).normalise();
    }
}
