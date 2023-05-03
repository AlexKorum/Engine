package utilities;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

public class Matrix4f {
    /*
    1. Сложение и вычитание матриц
    2. Умножение на скаляр
    3. Умножение на вектор
    4. Произведение 2-х матриц
    5. Транспонирование?
    */

    private Vector4f[] matrix;

    public Matrix4f() {
        this.matrix = new Vector4f[]{
                new Vector4f(1, 0, 0, 0),
                new Vector4f(0, 1, 0, 0),
                new Vector4f(0, 0, 1, 0),
                new Vector4f(0, 0, 0, 1)
        };
    }

    public Matrix4f(float[][] matrix) {
        this.matrix = new Vector4f[]{
                new Vector4f(matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3]),
                new Vector4f(matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3]),
                new Vector4f(matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3]),
                new Vector4f(matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3])

        };
    }

    // 1. Изменение объекта
    public void add(float[][] matrix) {
        this.matrix[0].add(matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3]);
        this.matrix[1].add(matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3]);
        this.matrix[2].add(matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3]);
        this.matrix[3].add(matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3]);
    }

    public void add(@NotNull Matrix4f matrix) {
        this.matrix[0].add(matrix.getLine(0));
        this.matrix[1].add(matrix.getLine(1));
        this.matrix[2].add(matrix.getLine(2));
        this.matrix[3].add(matrix.getLine(3));
    }

    // Умножение на скаляр
    public void multiplyOnScalar(float scalar) {
        this.matrix[0].multiplyOnScalar(scalar);
        this.matrix[1].multiplyOnScalar(scalar);
        this.matrix[2].multiplyOnScalar(scalar);
        this.matrix[3].multiplyOnScalar(scalar);
    }

    // Умножение на вектор
    public Vector4f multiplyOnVector(@NotNull Vector4f vector) {
        return new Vector4f(
                this.matrix[0].scalarMultiplyOnVector(vector),
                this.matrix[1].scalarMultiplyOnVector(vector),
                this.matrix[2].scalarMultiplyOnVector(vector),
                this.matrix[3].scalarMultiplyOnVector(vector)
        );
    }

    // Умножение матрицы на матрицу
    public void multiplyOnMatrix(@NotNull Matrix4f matrix) {
        Matrix4f newMatrix = new Matrix4f();
        Vector4f dop = new Vector4f();

        dop.set(matrix.getLine(0).getX(), matrix.getLine(1).getX(), matrix.getLine(2).getX(), matrix.getLine(3).getX());
        newMatrix.setElementAij(0, 0, this.matrix[0].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(1, 0, this.matrix[1].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(2, 0, this.matrix[2].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(3, 0, this.matrix[3].scalarMultiplyOnVector(dop));

        dop.set(matrix.getLine(0).getY(), matrix.getLine(1).getY(), matrix.getLine(2).getY(), matrix.getLine(3).getY());
        newMatrix.setElementAij(0, 1, this.matrix[0].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(1, 1, this.matrix[1].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(2, 1, this.matrix[2].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(3, 1, this.matrix[3].scalarMultiplyOnVector(dop));

        dop.set(matrix.getLine(0).getZ(), matrix.getLine(1).getZ(), matrix.getLine(2).getZ(), matrix.getLine(3).getZ());
        newMatrix.setElementAij(0, 2, this.matrix[0].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(1, 2, this.matrix[1].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(2, 2, this.matrix[2].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(3, 2, this.matrix[3].scalarMultiplyOnVector(dop));

        dop.set(matrix.getLine(0).getW(), matrix.getLine(1).getW(), matrix.getLine(2).getW(), matrix.getLine(3).getW());
        newMatrix.setElementAij(0, 3, this.matrix[0].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(1, 3, this.matrix[1].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(2, 3, this.matrix[2].scalarMultiplyOnVector(dop));
        newMatrix.setElementAij(3, 3, this.matrix[3].scalarMultiplyOnVector(dop));

        this.matrix[0].set(matrix.getLine(0));
        this.matrix[1].set(matrix.getLine(1));
        this.matrix[2].set(matrix.getLine(2));
        this.matrix[3].set(matrix.getLine(3));
    }


    // 3. Геттеры и сеттеры
    public void setElementAij(int i, int j, float el) {
        if (i > 3 || j > 3 || i < 0 || j < 0) throw new IllegalStateException("Выход за пределы матрицы");
        if (j == 0) matrix[i].setX(el);
        if (j == 1) matrix[i].setY(el);
        if (j == 2) matrix[i].setZ(el);
        if (j == 3) matrix[i].setW(el);
    }

    public float getElementAij(int i, int j) {
        if (i > 3 || j > 3 || i < 0 || j < 0) throw new IllegalStateException("Выход за пределы матрицы");
        if (j == 0) return matrix[i].getX();
        if (j == 1) return matrix[i].getY();
        if (j == 2) return matrix[i].getZ();
        if (j == 3) return matrix[i].getW();
        return MemoryUtil.NULL;
    }

    public Vector4f getLine(int i) {
        return this.matrix[i];
    }

    @Override
    public String toString() {
        return this.matrix[0].toString() +
                this.matrix[1].toString() +
                this.matrix[2].toString() +
                this.matrix[3].toString();
    }
}
