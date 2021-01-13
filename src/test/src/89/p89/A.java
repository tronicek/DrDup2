package p89;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class A {

    private ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("html-sheet-thread");
            return thread;
        }
    });
}
