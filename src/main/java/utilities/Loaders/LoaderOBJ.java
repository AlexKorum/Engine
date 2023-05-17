package utilities.Loaders;

import engine.scene.objects.components.Mesh;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LoaderOBJ {
    public static Mesh loadMeshFromOBJ(String path, Mesh mesh) {
        FileReader fr = null;

        try {
            fr = new FileReader(new File(path));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(fr);
        String line;
        List<Integer> indexes = new ArrayList<>();
        List<Vector3f> vertexes = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();

        float[] vertexesArray = null;
        float[] normalArray = null;
        int[] indexesArray = null;
        try {
            while (true) {
                line = br.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertexes.add(vertex);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    normalArray = new float[vertexes.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = br.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indexes, normals, normalArray);
                processVertex(vertex2, indexes, normals, normalArray);
                processVertex(vertex3, indexes, normals, normalArray);
                line = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        vertexesArray = new float[vertexes.size() * 3];
        indexesArray = new int[indexes.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertexes) {
            vertexesArray[vertexPointer++] = vertex.x/2;
            vertexesArray[vertexPointer++] = vertex.y/2;
            vertexesArray[vertexPointer++] = vertex.z/2;
        }

        for (int i = 0; i < indexes.size(); i++) {
            indexesArray[i] = indexes.get(i);
        }

        mesh.set(indexesArray, vertexesArray, normalArray);
        return mesh;
    }

    private static void processVertex(String[] vertexData, List<Integer> indexes, List<Vector3f> normals, float[] normalArrays) {
        int currentVertexPointer = Integer.parseInt(vertexData[0])-1;
        indexes.add(currentVertexPointer);

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalArrays[currentVertexPointer * 3] = currentNorm.x;
        normalArrays[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalArrays[currentVertexPointer * 3 + 2] = currentNorm.z;
    }
}
