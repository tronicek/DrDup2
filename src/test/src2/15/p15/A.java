package p15;

public class A {

    private int counter;

    int increment() {
        synchronized (this) {
            counter++;
        }
        return counter;
    }

    void addOne() {
        synchronized (this) {
            counter++;
        }
        System.out.println(counter);
    }
}
