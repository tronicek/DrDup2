package edu.tarleton.drdup2;

import edu.tarleton.drdup2.clones.Clone;
import edu.tarleton.drdup2.clones.CloneSet;
import edu.tarleton.drdup2.index.plain.persistent.PEdge;
import edu.tarleton.drdup2.index.plain.persistent.PNode;
import edu.tarleton.drdup2.index.plain.persistent.PPos;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * The unit tests.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class StmtTest {

    private final Random rand = new Random();
    private final Set<String> names = new HashSet<>();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAll() throws Exception {
        // GC must collect the mapped buffers
        Thread.sleep(500);
        System.gc();
        Thread.sleep(500);
    }

    private int perform(String rename, String srcDir) throws Exception {
        Properties conf = new Properties();
        return performWith(rename, srcDir, conf);
    }

    private int performWith(String rename, String srcDir, Properties conf) throws Exception {
        conf.setProperty("command", "findClones");
        conf.setProperty("level", "statement");
        conf.setProperty("rename", rename);
        conf.setProperty("sourceDir", srcDir);
        if (rename.equals("consistent")) {
            conf.setProperty("algorithm", "naive");
        }
        // full
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        int size = test(conf);
        // simplified
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "false");
        int size2 = test(conf);
        assertEquals(size, size2);
        // full compressed
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        int size3 = test(conf);
        assertEquals(size, size3);
        // simplified compressed
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "false");
        int size4 = test(conf);
        assertEquals(size, size4);
        // full persistent
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "true");
        int size5 = testPersistent(conf);
        assertEquals(size, size5);
        // simplified persistent
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "false");
        conf.setProperty("persistent", "true");
        int size6 = testPersistent(conf);
        assertEquals(size, size6);
        // full compressed persistent
        conf.setProperty("index", "full");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "true");
        int size7 = testPersistent(conf);
        assertEquals(size, size7);
        // simplified compressed persistent
        conf.setProperty("index", "simplified");
        conf.setProperty("compressed", "true");
        conf.setProperty("persistent", "true");
        int size8 = testPersistent(conf);
        assertEquals(size, size8);
        if (rename.equals("blind")) {
            conf.setProperty("algorithm", "naive");
            // full naive
            conf.setProperty("index", "full");
            conf.setProperty("compressed", "false");
            conf.setProperty("persistent", "false");
            int size9 = test(conf);
            assertEquals(size, size9);
            // simplified naive
            conf.setProperty("index", "simplified");
            conf.setProperty("compressed", "false");
            conf.setProperty("persistent", "false");
            int size10 = test(conf);
            assertEquals(size, size10);
            // full compressed naive
            conf.setProperty("index", "full");
            conf.setProperty("compressed", "true");
            conf.setProperty("persistent", "false");
            int size11 = test(conf);
            assertEquals(size, size11);
            // simplified compressed naive
            conf.setProperty("index", "simplified");
            conf.setProperty("compressed", "true");
            conf.setProperty("persistent", "false");
            int size12 = test(conf);
            assertEquals(size, size12);
            conf.remove("algorithm");
        }
        return size;
    }

    private int test(Properties conf) throws Exception {
        Engine eng = Engine.instance(conf);
        eng.findClones();
        CloneSet set = eng.getCloneSet();
        List<Clone> cc = set.getClones();
        return cc.size();
    }

    private int testPersistent(Properties conf) throws Exception {
        String nodeFileName = generateFileName("data", "nodes");
        String edgeFileName = generateFileName("data", "edges");
        String posFileName = generateFileName("data", "positions");
        String pathFileName = generateFileName("data", "paths");
        String labelFileName = generateFileName("data", "labels");
        String linearizationFileName = generateFileName("data", "linearizations");
        String nextStmtMapFileName = generateFileName("data", "nextStmtMap");
        conf.setProperty("nodeFile", nodeFileName);
        conf.setProperty("nodeFilePageSize", Integer.toString(PNode.LENGTH * 1024 * 64));
        conf.setProperty("edgeFile", edgeFileName);
        conf.setProperty("edgeFilePageSize", Integer.toString(PEdge.LENGTH * 1024 * 64));
        conf.setProperty("posFile", posFileName);
        conf.setProperty("posFilePageSize", Integer.toString(PPos.LENGTH * 1024 * 64));
        conf.setProperty("pathFile", pathFileName);
        conf.setProperty("labelFile", labelFileName);
        conf.setProperty("linearizationFile", linearizationFileName);
        conf.setProperty("nextStmtMapFile", nextStmtMapFileName);
        return test(conf);
    }

    private String generateFileName(String dir, String prefix) {
        String fn;
        do {
            fn = dir + "/" + prefix + randomString(8);
        } while (names.contains(fn));
        names.add(fn);
        File file = new File(fn);
        file.deleteOnExit();
        return fn;
    }

    private String randomString(int len) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
        String str = "";
        for (; len > 0; len--) {
            int i = Math.abs(rand.nextInt() % chars.length());
            str += chars.charAt(i);
        }
        return str;
    }

    @Test
    public void test1() throws Exception {
        int c = perform("blind", "src/test/src2/1");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/1");
        assertEquals(1, c2);
    }

    @Test
    public void test2() throws Exception {
        int c = perform("blind", "src/test/src2/2");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/2");
        assertEquals(1, c2);
    }

    @Test
    public void test3() throws Exception {
        int c = perform("blind", "src/test/src2/3");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/3");
        assertEquals(1, c2);
    }

    @Test
    public void test4() throws Exception {
        int c = perform("blind", "src/test/src2/4");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/4");
        assertEquals(2, c2);
    }

    @Test
    public void test5() throws Exception {
        int c = perform("blind", "src/test/src2/5");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/5");
        assertEquals(1, c2);
    }

    @Test
    public void test6() throws Exception {
        int c = perform("blind", "src/test/src2/6");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/6");
        assertEquals(1, c2);
    }

    @Test
    public void test7() throws Exception {
        int c = perform("blind", "src/test/src2/7");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/7");
        assertEquals(1, c2);
    }

    @Test
    public void test8() throws Exception {
        int c = perform("blind", "src/test/src2/8");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/8");
        assertEquals(1, c2);
    }

    @Test
    public void test9() throws Exception {
        int c = perform("blind", "src/test/src2/9");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/9");
        assertEquals(2, c2);
    }

    @Test
    public void test10() throws Exception {
        int c = perform("blind", "src/test/src2/10");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/10");
        assertEquals(2, c2);
    }

    @Test
    public void test11() throws Exception {
        int c = perform("blind", "src/test/src2/11");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/11");
        assertEquals(2, c2);
    }

    @Test
    public void test12() throws Exception {
        int c = perform("blind", "src/test/src2/12");
        assertEquals(3, c);
        int c2 = perform("consistent", "src/test/src2/12");
        assertEquals(3, c2);
    }

    @Test
    public void test13() throws Exception {
        int c = perform("blind", "src/test/src2/13");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/13");
        assertEquals(1, c2);
    }

    @Test
    public void test14() throws Exception {
        int c = perform("blind", "src/test/src2/14");
        assertEquals(3, c);
        int c2 = perform("consistent", "src/test/src2/14");
        assertEquals(3, c2);
    }

    @Test
    public void test15() throws Exception {
        int c = perform("blind", "src/test/src2/15");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/15");
        assertEquals(2, c2);
    }

    @Test
    public void test16() throws Exception {
        int c = perform("blind", "src/test/src2/16");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/16");
        assertEquals(1, c2);
    }

    @Test
    public void test17() throws Exception {
        int c = perform("blind", "src/test/src2/17");
        assertEquals(3, c);
        int c2 = perform("consistent", "src/test/src2/17");
        assertEquals(3, c2);
    }

    @Test
    public void test18() throws Exception {
        int c = perform("blind", "src/test/src2/18");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/18");
        assertEquals(2, c2);
    }

    @Test
    public void test19() throws Exception {
        int c = perform("blind", "src/test/src2/19");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/19");
        assertEquals(2, c2);
    }

    @Test
    public void test20() throws Exception {
        int c = perform("blind", "src/test/src2/20");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src2/20");
        assertEquals(0, c2);
    }

    @Test
    public void test21() throws Exception {
        int c = perform("blind", "src/test/src2/21");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src2/21");
        assertEquals(0, c2);
    }

    @Test
    public void test22() throws Exception {
        int c = perform("blind", "src/test/src2/22");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src2/22");
        assertEquals(0, c2);
    }

    @Test
    public void test23() throws Exception {
        int c = perform("blind", "src/test/src2/23");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src2/23");
        assertEquals(1, c2);
    }

    @Test
    public void test24() throws Exception {
        int c = perform("blind", "src/test/src2/24");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/24");
        assertEquals(0, c2);
    }

    @Test
    public void test25() throws Exception {
        int c = perform("blind", "src/test/src2/25");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/25");
        assertEquals(0, c2);
    }

    @Test
    public void test26() throws Exception {
        int c = perform("blind", "src/test/src2/26");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/26");
        assertEquals(0, c2);
    }

    @Test
    public void test27() throws Exception {
        int c = perform("blind", "src/test/src2/27");
        assertEquals(3, c);
        int c2 = perform("consistent", "src/test/src2/27");
        assertEquals(1, c2);
    }

    @Test
    public void test28() throws Exception {
        int c = perform("blind", "src/test/src2/28");
        assertEquals(3, c);
        int c2 = perform("consistent", "src/test/src2/28");
        assertEquals(1, c2);
    }

    @Test
    public void test29() throws Exception {
        Properties conf = new Properties();
        conf.put("addBlocks", "true");
        int c = performWith("blind", "src/test/src2/29", conf);
        assertEquals(2, c);
        int c2 = performWith("consistent", "src/test/src2/29", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test30() throws Exception {
        int c = perform("blind", "src/test/src2/30");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src2/30");
        assertEquals(0, c2);
    }
}
