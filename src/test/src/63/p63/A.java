package p63;

public class A {

    int x;

    void inc() {
        x++;
    }

    void dec() {
        x--;
    }

    void print(int a) {
        inc();
        if (a > 0) {
            inc();
        } else {
            dec();
        }
    }
    
    void write(int a) {
        inc();
        if (a > 0) inc();
        else dec();
    }
}
