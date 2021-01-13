package p113;

public class B<T> {

    public B(T s) {
        System.out.println(s);
    }
    
    public Intf m(Object obj) {
        return B<String>::new;
    }
}
