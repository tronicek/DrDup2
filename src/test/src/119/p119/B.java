package p119;

public class B {

    @Source("ZT")
    public void m() {
        Runnable r = new Runnable() {
            @Override @Source2("ZT")
            public void run() {
                System.out.println("hi");
            }
        };
        r.run();
    }
}
