package p22;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class A {

    public abstract void openFile(String mode) throws IOException;

    public abstract void closeFile() throws IOException;

    void m() {
        try (InputStream is = new FileInputStream("x.txt")) {
            openFile("r");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    void m2() {
        try {
            closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
