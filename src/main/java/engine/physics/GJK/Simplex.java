package engine.physics.GJK;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

public class Simplex {

    private Vector3f[] points;
    private int size;

    public Simplex() {
        this.points = new Vector3f[4];
        size = 0;
    }

    public void set(Vector3f[] points) {
        for (int i = 0; i < Math.min(points.length, 4); i++) {
            this.points[i] = points[i];
        }
        size = Math.min(points.length, 4);
    }

    public void set(ArrayList<Vector3f> points) {
        for (int i = 0; i < Math.min(points.size(), 4); i++) {
            this.points[i] = points.get(i);
        }
        size = Math.min(points.size(), 4);
    }

    public void pushToFront(Vector3f point) {
        for (int i = points.length - 1; i > 0; i--) {
            points[i] = points[i - 1];
        }
        points[0] = point;
        size = Math.min(size + 1, 4);
    }

    public int getSize() {
        return size;
    }

    public Vector3f getPoint(int i) {
        return points[i];
    }

    public ArrayList<Vector3f> getPoints() {
        ArrayList<Vector3f> pointsList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            pointsList.add(points[i]);
        }
        return pointsList;
    }

    @Override
    public String toString() {
        String pointsString = "\n";
        for (Vector3f vector : points) {
            if (vector != null)
                pointsString += vector.toString() + "\n";
        }
        return "Simplex{" +
                "points=" + pointsString +
                "size=" + size +
                '}';
    }
}
