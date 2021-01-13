package p114;

public class A {

    @Source(author = "ZT", version = "1.0")
    public void print() {
        @Source(author = "ZT", version = "1.0")
        int x = 10;
        System.out.println(x);
    }
}
