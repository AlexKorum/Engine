package utilities.classes;

public class Pair<T1, T2> {
    private T1 item1;
    private T2 item2;

    public Pair(T1 a, T2 b) {
        this.item1 = a;
        this.item2 = b;
    }

    public T1 getItem1() {
        return item1;
    }

    public T2 getItem2() {
        return item2;
    }

    public void set(T1 item1, T2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public boolean equals(Pair pair) {
        return item1.equals(pair.getItem1()) && item2.equals(pair.getItem2());
    }

    @Override
    public String toString() {
        return "Pair{" +
                "item1 = " + item1 +
                ", item2 = " + item2 +
                "}";
    }
}
