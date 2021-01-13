package p122;

public class B {

    public void m(String s) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException | NullPointerException e) {
            if (e instanceof NullPointerException) {
                e.printStackTrace();
            }
        }
    }
}
