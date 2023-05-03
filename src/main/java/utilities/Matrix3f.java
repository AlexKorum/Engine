package utilities;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix3f {
    /*
    1. Сложение и вычитание матриц
    2. Умножение на скаляр
    3. Умножение на вектор
    4. Произведение 2-х матриц
    5. Транспонирование?
    */

    private Vector3f[] matrix;

    public Matrix3f() {
        this.matrix = new Vector3f[]{
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0),
                new Vector3f(0, 0, 1)
        };
    }

    public Matrix3f(float[][] matrix) {
        this.matrix = new Vector3f[]{
                new Vector3f(matrix[0][0], matrix[0][1], matrix[0][2]),
                new Vector3f(matrix[1][0], matrix[1][1], matrix[1][2]),
                new Vector3f(matrix[2][0], matrix[2][1], matrix[2][2])
        };
    }

    public void createMatrix(float xa, float ya, float za) {
        matrix[0].set((float) (cos(ya) * cos(za)), (float) (-sin(za) * cos(ya)), (float) sin(ya));
        matrix[1].set((float) (sin(xa) * sin(ya) * cos(za) + sin(za) * cos(xa)), (float) (-sin(xa) * sin(ya) * sin(za) + cos(xa) * cos(za)), (float) (-sin(xa) * cos(ya)));
        matrix[2].set((float) (sin(xa) * sin(za) - sin(ya) * cos(xa) * cos(za)), (float) (sin(xa) * cos(za) + sin(ya) * sin(za) * cos(xa)), (float) (cos(xa) * cos(ya)));
    }

    // 1. Изменение самого объекта

    // Сложение матриц
    public void add(float[][] matrix) {
        this.matrix[0].add(matrix[0][0], matrix[0][1], matrix[0][2]);
        this.matrix[1].add(matrix[1][0], matrix[1][1], matrix[1][2]);
        this.matrix[2].add(matrix[2][0], matrix[2][1], matrix[2][2]);
    }

    public void add(@NotNull Matrix3f matrix) {
        this.matrix[0].add(matrix.getLine(0));
        this.matrix[1].add(matrix.getLine(1));
        this.matrix[2].add(matrix.getLine(2));
    }

    // Умножение на скаляр
    public void multiplyOnScalar(float scalar) {
        this.matrix[0].multiplyOnScalar(scalar);
        this.matrix[1].multiplyOnScalar(scalar);
        this.matrix[2].multiplyOnScalar(scalar);
    }

    // Умножение на вектор
    public Vector3f multiplyOnVector(@NotNull Vector3f vector) {
        return new Vector3f(
                this.matrix[0].scalarMultiplyOnVector(vector),
                this.matrix[1].scalarMultiplyOnVector(vector),
                this.matrix[2].scalarMultiplyOnVector(vector)
        );
    }

    public void multiplyOnVectorReturn(float x, float y, float z, Vector3f vector) {
        vector.set(
                this.matrix[0].scalarMultiplyOnVector(x, y, z),
                this.matrix[1].scalarMultiplyOnVector(x, y, z),
                this.matrix[2].scalarMultiplyOnVector(x, y, z)
        );
    }

    // Умножение матрицы на матрицу
    public void multiplyOnMatrix(@NotNull Matrix3f matrix) {
        Matrix3f newMatrix = new Matrix3f();
        Vector3f dop = new Vector3f();

        dop.set(matrix.getLine(0).getX(), matrix.getLine(1).getX(), matrix.getLine(2).getX());
        newMatrix.setElementAij(0, 0, this.matrix[0].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(1, 0, this.matrix[1].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(2, 0, this.matrix[2].scalarMultiplyOnVector(dop));

        dop.set(matrix.getLine(0).getY(), matrix.getLine(1).getY(), matrix.getLine(2).getY());
        newMatrix.setElementAij(0, 1, this.matrix[0].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(1, 1, this.matrix[1].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(2, 1, this.matrix[2].scalarMultiplyOnVector(dop));

        dop.set(matrix.getLine(0).getZ(), matrix.getLine(1).getZ(), matrix.getLine(2).getZ());
        newMatrix.setElementAij(0, 2, this.matrix[0].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(1, 2, this.matrix[1].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(2, 2, this.matrix[2].scalarMultiplyOnVector(dop));

        this.matrix[0].set(matrix.getLine(0));
        this.matrix[1].set(matrix.getLine(1));
        this.matrix[2].set(matrix.getLine(2));
    }

    // 2. Вывод данных

    // 3. Геттеры и сеттеры
    public void setElementAij(int i, int j, float el) {
        if (i > 2 || j > 2 || i < 0 || j < 0) throw new IllegalStateException("Выход за пределы матрицы");
        if (j == 0) matrix[i].setX(el);
        if (j == 1) matrix[i].setY(el);
        if (j == 2) matrix[i].setZ(el);
    }

    public float getElementAij(int i, int j) {
        if (i > 2 || j > 2 || i < 0 || j < 0) throw new IllegalStateException("Выход за пределы матрицы");
        if (j == 0) return matrix[i].getX();
        if (j == 1) return matrix[i].getY();
        if (j == 2) return matrix[i].getZ();
        return MemoryUtil.NULL;
    }

    public Vector3f getLine(int i) {
        return this.matrix[i];
    }

    @Override
    public String toString() {
        return this.matrix[0].toString() +
                this.matrix[1].toString() +
                this.matrix[2].toString();
    }
}
