package utilities;


import org.jetbrains.annotations.NotNull;

public class Vector3f {
    /*
     * 1. Создание векора
     *   +1.1. Без инициализаций
     *   +1.2. С инициализацией в виде 3-х чисел
     *   +1.3. С инициализацией в виде вектора
     * 2. Установка кооринат
     *   +2.1. Установка координат поотдельности
     *   +2.2. Установка по 3-м числам
     *   +2.3. Установка по Входному вектору
     * 3. Математические операций над векторами
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

    // 1. Инициализация вектора
    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f(float s) {
        this.x = s;
        this.y = s;
        this.z = s;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector3f(@NotNull Vector3f src) {
        this.x = src.getX();
        this.y = src.getY();
        this.z = src.getZ();
    }


    // 2. Математические операций (работа над текущим объектом)
    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void add(@NotNull Vector3f src) {
        this.x += src.getX();
        this.y += src.getY();
        this.z += src.getZ();
    }

    public void sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public void sub(@NotNull Vector3f src) {
        this.x -= src.getX();
        this.y -= src.getY();
        this.z -= src.getZ();
    }

    public void multiplyOnScalar(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public void divisionOnScalar(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        this.z /= scalar;
    }

    public void negative() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public void scale(float sx, float sy, float sz) {
        this.x *= sx;
        this.y *= sy;
        this.z *= sz;
    }

    public void scale(@NotNull Vector3f src) {
        this.x *= src.getX();
        this.y *= src.getY();
        this.z *= src.getZ();
    }

    public void normalize() {
        this.divisionOnScalar(this.length());
    }

    // 3. Математические операций (с возвратом объектов)
    public Vector3f sumReturn(float x, float y, float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    public Vector3f sumReturn(@NotNull Vector3f src) {
        return new Vector3f(this.x + src.getX(), this.y + src.getY(), this.z + src.getZ());
    }

    public Vector3f subReturn(float x, float y, float z) {
        return new Vector3f(this.x - x, this.y - y, this.z - z);
    }

    public Vector3f subReturn(@NotNull Vector3f src) {
        return new Vector3f(this.x - src.getX(), this.y - src.getY(), this.z - src.getZ());
    }

    public Vector3f multiplyOnScalarReturn(float scalar) {
        return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3f divisionOnScalarReturn(float scalar) {
        return new Vector3f(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public Vector3f negativeReturn() {
        return new Vector3f(-this.x, -this.y, -this.z);
    }

    public Vector3f scaleReturn(float sx, float sy, float sz) {
        return new Vector3f(this.x * sx, this.y * sy, this.z * sz);
    }

    public Vector3f scaleReturn(@NotNull Vector3f src) {
        return new Vector3f(this.x * src.getX(), this.y * src.getY(), this.z * src.getZ());
    }

    public Vector3f normalizeReturn() {
        return this.divisionOnScalarReturn(this.length());
    }

    // 4. Возвращаемые значения
    public float lengthSquare() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquare());
    }

    public float distance(float x, float y, float z) {
        return this.subReturn(x, y, z).length();
    }

    public float distance(Vector3f src) {
        return this.subReturn(src).length();
    }

    public float scalarMultiplyOnVector(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public float scalarMultiplyOnVector(@NotNull Vector3f src) {
        return this.x * src.getX() + this.y * src.getY() + this.z * src.getZ();
    }

    public void vectorMultiplyOnVector(float x, float y, float z) {
        float xn = this.y * z - this.z * y;
        float yn = this.z * x - this.x * z;
        float zn = this.x * y - this.y * x;

        this.x = xn;
        this.y = yn;
        this.z = zn;
    }

    public void vectorMultiplyOnVector(Vector3f src) {
        vectorMultiplyOnVector(src.getX(), src.getY(), src.getZ());
    }

    public Vector3f vectorMultiplyOnVectorReturn(float x, float y, float z) {
        return new Vector3f(
                this.y * z - this.z * y,
                this.z * x - this.x * z,
                this.x * y - this.y * x
        );
    }

    public Vector3f vectorMultiplyOnVectorReturn(@NotNull Vector3f src) {
        return vectorMultiplyOnVectorReturn(src.getX(), src.getY(), src.getZ());
    }

    // Геттеры и сеттеры
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(@NotNull Vector3f vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
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

    @Override
    public String toString() {
        return x + " " + y + " " + z + "\n";
    }

    public boolean equals(@NotNull Vector3f vec3f) {
        return vec3f.getX() == this.x && vec3f.getY() == this.y && vec3f.getZ() == this.z;
    }

    @Override
    public Vector3f clone() {
        return new Vector3f(this.x, this.y, this.z);
    }
}
