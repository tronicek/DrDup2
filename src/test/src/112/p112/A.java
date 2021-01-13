package p112;

public class A {

    public A(String s) {
    }
    
    public String m(String s) {
        Intf<String> i = A::new;
        return i.create(s);
    }
}
