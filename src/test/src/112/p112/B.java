package p112;

public class B {

    public Object m(String s) {
        Intf<Object> i = B::new;
        return i.create(s);
    }
}
