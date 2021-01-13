package p22;

public class C extends A {

    int y;
    
    C(int x) {
        super(x);
        y = 42;
    }
    
    C(int x, int y) {
        this(x);
        this.y = y;
    }
}
