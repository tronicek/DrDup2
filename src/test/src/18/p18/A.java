package p18;

public class A {

    int x;

    void increment() {
        x++;
    }

    void decrement() {
        x--;
    }

    void execute() {
        increment();
        decrement();
    }

    void execute2() {
        increment();
        increment();
    }
}
