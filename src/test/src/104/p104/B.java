package p104;

public class B {

    public void m(Object obj) {
        if (obj instanceof String) {
            System.out.println(obj);
        }
        if (obj instanceof String) {
            System.out.println("str " + obj);
        }
    }
}
