package p49;

public class A {

    Thread.State newThread() {
        return Thread.State.NEW;
    }

    Thread.State blockedThread() {
        return Thread.State.BLOCKED;
    }
}
