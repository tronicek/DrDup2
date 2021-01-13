package p16;

public class A {

    int x, y;

    void compute() {
    }

    void perform() {
        while (x < 0) {
            compute();
        }
        y++;
    }

    void execute() {
        while (x < 0) {
            compute();
            y++;
        }
    }
}
