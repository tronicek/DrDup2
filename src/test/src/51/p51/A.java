package p51;

public class A {

    Runnable r = new Runnable() {
        @Override
        public void run() {
            System.out.println("42");
        }
    };

    public void run() {
        System.out.println("42");
    }
}
