package p104;

public class A {

    public void m(Object obj) {
        if (obj instanceof String) {
            System.out.println(obj);
        }
        if (obj instanceof Integer) {
            System.out.println("int " + obj);
        }
    }
}
