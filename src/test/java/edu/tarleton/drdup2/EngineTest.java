package edu.tarleton.drdup2;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * The unit tests.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class EngineTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFull() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("MemoryPlainEngine", cls);
    }

    @Test
    public void testSimplified() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("MemoryPlainEngine", cls);
    }

    @Test
    public void testFullCompressed() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("MemoryCompressedEngine", cls);
    }

    @Test
    public void testSimplifiedCompressed() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("MemoryCompressedEngine", cls);
    }

    @Test
    public void testFullPersistent() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "true");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("PlainPersistentEngine", cls);
    }

    @Test
    public void testSimplifiedPersistent() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "true");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("PlainPersistentEngine", cls);
    }

    @Test
    public void testFullCompressedPersistent() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "true");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("CPEngine", cls);
    }

    @Test
    public void testSimplifiedCompressedPersistent() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "true");
        Engine eng = Engine.instance(conf);
        String cls = eng.getClass().getSimpleName();
        assertEquals("CPEngine", cls);
    }
}
