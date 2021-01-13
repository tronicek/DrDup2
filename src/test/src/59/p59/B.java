package p59;

public class B {

    public Object run() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {
        }
        return cl;
    }
}
