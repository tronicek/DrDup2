package p119;

public class A {

    @Source("ZT")
    public void m() {
        Runnable r = new Runnable() {
            @Override @Source("ZT")
            public void run() {
                System.out.println("hi");
            }
        };
        r.run();
    }
}
