package engine.input;

import engine.scene.objects.Object;
import engine.scene.objects.components.Transform;
import engine.scene.objects.components.enums.ComponentsList;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utilities.math.MatrixMath;

public class MoveController {
    private Object object;
    private float velocity;
    private float angleVelocity;

    private Vector4f forward;
    private Vector4f right;


    public MoveController(Object object) {
        this.object = object;
        velocity = 0.1f;
        angleVelocity = (float) (Math.PI / 2);

        forward = new Vector4f(0, 0, -1, 0);
        right = new Vector4f(1, 0, 0, 0);
    }

    public MoveController(Object object, float velocity, float angleVelocity) {
        this(object);
        this.velocity = velocity;
        this.angleVelocity = angleVelocity;
    }

    public void moveForward() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(velocity * forward.getX(),
                velocity * forward.getY(),
                velocity * forward.getZ());
    }

    public void moveBack() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(-velocity * forward.getX(),
                -velocity * forward.getY(),
                -velocity * forward.getZ());
    }

    public void moveLeft() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(-velocity * right.getX(),
                0,
                -velocity * right.getZ());
    }

    public void moveRight() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(velocity * right.getX(),
                0,
                velocity * right.getZ());
    }

    public void moveUp() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(0, velocity, 0);
    }

    public void moveDown() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getPosition().translate(0, -velocity, 0);
    }

    public void rotateRight() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation().translate(0, angleVelocity, 0);
        rotate();
    }

    public void rotateLeft() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation().translate(0, -angleVelocity, 0);
        rotate();
    }

    public void rotateUp() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation().translate(-angleVelocity, 0, 0);
        rotate();
    }

    public void rotateDown() {
        ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation().translate(angleVelocity, 0, 0);
        rotate();
    }

    private void rotate() {
        Vector3f rotateVector = ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation();
        if (rotateVector.getX() > 80) {
            ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation().set(80, rotateVector.getY(), rotateVector.getZ());
        }
        if (rotateVector.getX() < -80) {
            ((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation().set(-80, rotateVector.getY(), rotateVector.getZ());
        }

        Matrix4f rotate = MatrixMath.createRotateMatrix(((Transform) object.getComponent(ComponentsList.TRANSFORM)).getRotation());
        Matrix4f.transform(rotate, new Vector4f(0, 0, -1, 0), forward);
        Matrix4f.transform(rotate, new Vector4f(1, 0, 0, 0), right);

        forward.normalise();
        right.normalise();
    }

    public Vector4f getForward() {
        return new Vector4f(forward);
    }
}