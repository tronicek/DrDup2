package p121;

public class B {

    public class X implements Runnable {

        @Override
        public void run() {
            String s = B.this.toString();
            A p = new A();
        }
    }
}
