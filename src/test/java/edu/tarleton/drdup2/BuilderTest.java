package edu.tarleton.drdup2;

import java.lang.reflect.Method;
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
public class BuilderTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws Exception {
    }

    private String builderName(Properties conf) throws Exception {
        Engine eng = Engine.instance(conf);
        Class<?> cls = eng.getClass();
        Method meth = cls.getDeclaredMethod("createBuilder");
        meth.setAccessible(true);
        Object builder = meth.invoke(eng);
        return builder.getClass().getSimpleName();
    }

    @Test
    public void testFullIndexBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        String name = builderName(conf);
        assertEquals("FullIndexBuilder", name);
    }

    @Test
    public void testFullIndexStmtBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("level", "statement");
        String name = builderName(conf);
        assertEquals("FullIndexBuilder", name);
    }

    @Test
    public void testFullIndexBuilderNaive() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("algorithm", "naive");
        String name = builderName(conf);
        assertEquals("FullIndexBuilderNaive", name);
    }

    @Test
    public void testSimplifiedIndexBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        String name = builderName(conf);
        assertEquals("SimplifiedIndexBuilder", name);
    }

    @Test
    public void testSimplifiedIndexStmtBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("level", "statement");
        String name = builderName(conf);
        assertEquals("SimplifiedIndexStmtBuilder", name);
    }

    @Test
    public void testSimplifiedIndexBuilderNaive() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("algorithm", "naive");
        String name = builderName(conf);
        assertEquals("SimplifiedIndexBuilderNaive", name);
    }

    @Test
    public void testFullCompressedIndexBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        String name = builderName(conf);
        assertEquals("FullCompressedIndexBuilder", name);
    }

    @Test
    public void testFullCompressedIndexStmtBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("level", "statement");
        String name = builderName(conf);
        assertEquals("FullCompressedIndexBuilder", name);
    }

    @Test
    public void testFullCompressedIndexBuilderNaive() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("algorithm", "naive");
        String name = builderName(conf);
        assertEquals("FullCompressedIndexBuilderNaive", name);
    }

    @Test
    public void testSimplifiedCompressedIndexBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        String name = builderName(conf);
        assertEquals("SimplifiedCompressedIndexBuilder", name);
    }

    @Test
    public void testSimplifiedCompressedIndexStmtBuilder() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("level", "statement");
        String name = builderName(conf);
        assertEquals("SimplifiedCompressedIndexStmtBuilder", name);
    }

    @Test
    public void testSimplifiedCompressedIndexBuilderNaive() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        conf.setProperty("sourceDir", "sourceDir");
        conf.setProperty("algorithm", "naive");
        String name = builderName(conf);
        assertEquals("SimplifiedCompressedIndexBuilderNaive", name);
    }
}
