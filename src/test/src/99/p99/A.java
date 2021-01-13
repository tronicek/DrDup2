package p99;

public class A {

    public Object makeRunnable() {
        class C1<T> {
            T m(T t) {
                return t;
            }
        }
        return new C1<>();
    }
}
