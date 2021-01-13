package p55;

public class B {

    A p = new A();

    void setSize(int width, int height) {
        p.rect.width = width;
        p.rect.height = height;
    }

    void setSize2(int width, int height) {
        p.rect.width = width;
        p.rect.width = height;
    }
}
