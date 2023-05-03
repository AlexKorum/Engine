package utilities;

import org.jetbrains.annotations.NotNull;

public class Vector4f {
    /*
     * 1. Создание векора
     *   +1.1. Без инициализаций
     *   +1.2. С инициализацией в виде 3-х чисел
     *   +1.3. С инициализацией в виде вектора
     * 2. Установка кооринат
     *   +2.1. Установка координат поотдельности
     *   +2.2. Установка по 3-м числам
     *   +2.3. Установка по Входному вектору
     * 3. Математической операций над векторами
     *   +3.1. Сложение (с возвратом вектора)
     *   +3.2. Вычитание (с возвратом вектора)
     *   +3.3. Умножение вектора на число (с возвратом вектора)
     *   +3.4. Умножение векторов друг на друга (скалярное произведение)
     *   +3.5. Умножение векторов друг нв другв (векторное произведение)
     *   +3.6. Отражение вектора относительно знака (*-1)
     * 4. Свойства вектора
     *   +4.1. Длина вектора в квадрате
     *   +4.2. Рельная длина вектора
     *   +4.3. Нахождение растояния между текущим ветором и пришедшим
     * 5. Операций над самим объектом
     *   +5.1. Перемещение (сложение исходного вектора с полученным)
     *   +5.2. Маштабирование ветора целиком и почленно
     *   +5.3. Нормальзация вектора
     *    5.4. Повороты ветора относительно начала кординат или произвольной точки
     * */

    private float x;
    private float y;
    private float z;

    private float w;

    // 1. Инициализация вектора
    public Vector4f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public Vector4f(float s) {
        this.x = s;
        this.y = s;
        this.z = s;
        this.w = s;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    public Vector4f(@NotNull Vector4f src) {
        this.x = src.getX();
        this.y = src.getY();
        this.z = src.getZ();
        this.w = src.getW();
    }


    // 2. Математические операций (работа над текущим объектом)
    public void add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
    }

    public void add(@NotNull Vector4f src) {
        this.x += src.getX();
        this.y += src.getY();
        this.z += src.getZ();
        this.w += src.getW();
    }

    public void sub(float x, float y, float z, float w) {
        this.x -= x;
        this.y -= y;
        this.y -= z;
        this.w -= w;
    }

    public void sub(@NotNull Vector4f src) {
        this.x -= src.getX();
        this.y -= src.getY();
        this.z -= src.getZ();
        this.w -= src.getW();
    }

    public void multiplyOnScalar(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
    }

    public void divisionOnScalar(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
        this.w /= scalar;
    }

    public void negative() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }

    public void scale(float sx, float sy, float sz, float sw) {
        this.x *= sx;
        this.y *= sy;
        this.z *= sz;
        this.w *= sw;
    }

    public void scale(@NotNull Vector4f src) {
        this.x *= src.getX();
        this.y *= src.getY();
        this.z *= src.getZ();
        this.w *= src.getW();
    }

    public void normalize() {
        this.divisionOnScalar(this.length());
    }

    // 3. Математические операций (с возвратом объектов)
    public Vector4f sumReturn(float x, float y, float z, float w) {
        return new Vector4f(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Vector4f sumReturn(@NotNull Vector4f src) {
        return new Vector4f(this.x + src.getX(), this.y + src.getY(), this.z + src.getZ(), this.w + src.getW());
    }

    public Vector4f subReturn(float x, float y, float z, float w) {
        return new Vector4f(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    public Vector4f subReturn(@NotNull Vector4f src) {
        return new Vector4f(this.x - src.getX(), this.y - src.getY(), this.z - src.getZ(), this.w - src.getW());
    }

    public Vector4f multiplyOnScalarReturn(float scalar) {
        return new Vector4f(this.x * scalar, this.y * scalar, this.z * scalar, this.w * scalar);
    }

    public Vector4f divisionOnScalarReturn(float scalar) {
        return new Vector4f(this.x / scalar, this.y / scalar, this.z / scalar, this.w / scalar);
    }

    public Vector4f negativeReturn() {
        return new Vector4f(-this.x, -this.y, -this.z, -this.w);
    }

    public Vector4f scaleReturn(float sx, float sy, float sz, float sw) {
        return new Vector4f(this.x * sx, this.y * sy, this.z * sz, this.w * sw);
    }

    public Vector4f scaleReturn(@NotNull Vector4f src) {
        return new Vector4f(this.x * src.getX(), this.y * src.getY(), this.z * src.getZ(), this.w * src.getW());
    }

    public Vector4f normalizeReturn() {
        return this.divisionOnScalarReturn(this.length());
    }

    // 4. Возвращаемые значения
    public float lengthSquare() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquare());
    }

    public float distance(float x, float y, float z, float w) {
        return this.subReturn(x, y, z, w).length();
    }

    public float distance(@NotNull Vector4f src) {
        return this.subReturn(src).length();
    }

    public float scalarMultiplyOnVector(float x, float y, float z, float w) {
        return this.x * x + this.y * y + this.z * z + this.w * w;
    }

    public float scalarMultiplyOnVector(@NotNull Vector4f src) {
        return this.x * src.getX() + this.y * src.getY() + this.z * src.getZ() + this.w * src.getW();
    }


    // Геттеры и сеттеры
    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(Vector4f vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
        this.w = vector.getW();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z + " " + w + "\n";
    }

    @Override
    public Vector4f clone() {
        return new Vector4f(this.x, this.y, this.z, this.w);
    }
}
