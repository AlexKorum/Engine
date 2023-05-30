package engine.physics.GJK;

import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;

public class FaceNormals {
    private ArrayList<Vector4f> normals;
    private int minTriangle;

    public FaceNormals(ArrayList<Vector4f> normals, int minTriangle) {
        this.normals = normals;
        this.minTriangle = minTriangle;
    }

    public ArrayList<Vector4f> getNormals() {
        return normals;
    }

    public void setNormals(ArrayList<Vector4f> normals) {
        this.normals = normals;
    }

    public int getMinTriangle() {
        return minTriangle;
    }

    public void setMinTriangle(int minTriangle) {
        this.minTriangle = minTriangle;
    }
}
