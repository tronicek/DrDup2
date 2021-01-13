package p99;

public class B {

    public Object makeRunnable() {
        class C1<T> {
            Object m(T t) {
                return t;
            }
        }
        return new C1<>();
    }
}
