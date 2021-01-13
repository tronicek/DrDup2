package p122;

public class A {

    public void m(String s) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException | NullPointerException e) {
            if (e instanceof InterruptedException) {
                e.printStackTrace();
            }
        }
    }
}
