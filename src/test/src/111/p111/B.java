package p111;

public class B {

    public Object m() {
        Intf i = B::new;
        return i.create();
    }
}
