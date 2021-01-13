package p10;

public class A {

    void print(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(i);
        }
        System.out.println("---");
    }
    
    void display(int n) {
        n++;
        for (int j = 0; j < n; j++) {
            System.out.println(j);
        }
    }
}
