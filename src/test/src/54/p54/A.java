package p54;

public class A {

    void m() {
        try {
            System.out.println("hi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("hi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void m2() {
        try {
            System.out.println("hi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("hi");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
