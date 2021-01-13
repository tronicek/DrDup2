package p57;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class A {

    void write(ObjectOutputStream oos) {
        try {
            oos.defaultWriteObject();
        } catch (IOException e) {
            try {
                oos.close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

    void write2(ObjectOutputStream oos) {
        try {
            oos.defaultWriteObject();
        } catch (IOException e) {
            try {
                oos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
