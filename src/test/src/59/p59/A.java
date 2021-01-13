package p59;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class A {

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                ClassLoader classLoader = null;
                try {
                    classLoader = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException ex) {
                }
                return classLoader;
            }
        });
    }
}
