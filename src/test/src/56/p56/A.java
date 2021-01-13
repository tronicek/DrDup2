package p56;

public class A {

    Size[] create() {
        return new Size[]{Size.S, Size.M, Size.L, Size.XL};
    }

    Size[] create2() {
        return new Size[]{Size.S, Size.M, Size.L, Size.S};
    }
}
