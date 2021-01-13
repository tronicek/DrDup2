package p17;

import java.io.IOException;

public abstract class A {

    public abstract void openFile() throws IOException;

    public abstract void closeFile() throws IOException;

    void m() {
        try {
            openFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void m2() {
        m();
        try {
            closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
