package p113;

public class A<T> {

    public A(T s) {
    }
    
    public Intf m(String s) {
        return A<String>::new;
    }
}
