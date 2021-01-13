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
public class MethodTest {

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
        conf.setProperty("level", "method");
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
        int c = perform("blind", "src/test/src/1");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/1");
        assertEquals(0, c2);
    }

    @Test
    public void test2() throws Exception {
        int c = perform("blind", "src/test/src/2");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/2");
        assertEquals(0, c2);
    }

    @Test
    public void test2x() throws Exception {
        Properties conf = new Properties();
        conf.put("ignoreParentheses", "true");
        int c = performWith("blind", "src/test/src/2", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/2", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test3() throws Exception {
        int c = perform("blind", "src/test/src/3");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/3");
        assertEquals(0, c2);
    }

    @Test
    public void test3x() throws Exception {
        Properties conf = new Properties();
        conf.put("ignoreParentheses", "true");
        int c = performWith("blind", "src/test/src/3", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/3", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test4() throws Exception {
        int c = perform("blind", "src/test/src/4");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/4");
        assertEquals(0, c2);
    }

    @Test
    public void test4x() throws Exception {
        Properties conf = new Properties();
        conf.put("addBlocks", "true");
        int c = performWith("blind", "src/test/src/4", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/4", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test5() throws Exception {
        int c = perform("blind", "src/test/src/5");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/5");
        assertEquals(0, c2);
    }

    @Test
    public void test6() throws Exception {
        int c = perform("blind", "src/test/src/6");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/6");
        assertEquals(0, c2);
    }

    @Test
    public void test6x() throws Exception {
        Properties conf = new Properties();
        conf.put("insertSuperCall", "true");
        int c = performWith("blind", "src/test/src/6", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/6", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test7() throws Exception {
        int c = perform("blind", "src/test/src/7");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/7");
        assertEquals(0, c2);
    }

    @Test
    public void test8() throws Exception {
        int c = perform("blind", "src/test/src/8");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/8");
        assertEquals(1, c2);
    }

    @Test
    public void test9() throws Exception {
        int c = perform("blind", "src/test/src/9");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/9");
        assertEquals(0, c2);
    }

    @Test
    public void test10() throws Exception {
        int c = perform("blind", "src/test/src/10");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/10");
        assertEquals(0, c2);
    }

    @Test
    public void test11() throws Exception {
        int c = perform("blind", "src/test/src/11");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/11");
        assertEquals(0, c2);
    }

    @Test
    public void test12() throws Exception {
        int c = perform("blind", "src/test/src/12");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/12");
        assertEquals(0, c2);
    }

    @Test
    public void test13() throws Exception {
        int c = perform("blind", "src/test/src/13");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/13");
        assertEquals(0, c2);
    }

    @Test
    public void test14() throws Exception {
        int c = perform("blind", "src/test/src/14");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/14");
        assertEquals(0, c2);
    }

    @Test
    public void test15() throws Exception {
        int c = perform("blind", "src/test/src/15");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/15");
        assertEquals(1, c2);
    }

    @Test
    public void test16() throws Exception {
        int c = perform("blind", "src/test/src/16");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/16");
        assertEquals(0, c2);
    }

    @Test
    public void test17() throws Exception {
        int c = perform("blind", "src/test/src/17");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/17");
        assertEquals(0, c2);
    }

    @Test
    public void test18() throws Exception {
        int c = perform("blind", "src/test/src/18");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/18");
        assertEquals(0, c2);
    }

    @Test
    public void test19() throws Exception {
        int c = perform("blind", "src/test/src/19");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/19");
        assertEquals(0, c2);
    }

    @Test
    public void test20() throws Exception {
        int c = perform("blind", "src/test/src/20");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/20");
        assertEquals(0, c2);
    }

    @Test
    public void test21() throws Exception {
        int c = perform("blind", "src/test/src/21");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/21");
        assertEquals(0, c2);
    }

    @Test
    public void test22() throws Exception {
        int c = perform("blind", "src/test/src/22");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/22");
        assertEquals(0, c2);
    }

    @Test
    public void test23() throws Exception {
        int c = perform("blind", "src/test/src/23");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/23");
        assertEquals(1, c2);
    }

    @Test
    public void test24() throws Exception {
        int c = perform("blind", "src/test/src/24");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/24");
        assertEquals(0, c2);
    }

    @Test
    public void test25() throws Exception {
        int c = perform("blind", "src/test/src/25");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/25");
        assertEquals(1, c2);
    }

    @Test
    public void test26() throws Exception {
        int c = perform("blind", "src/test/src/26");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/26");
        assertEquals(1, c2);
    }

    @Test
    public void test27() throws Exception {
        int c = perform("blind", "src/test/src/27");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/27");
        assertEquals(0, c2);
    }

    @Test
    public void test28() throws Exception {
        int c = perform("blind", "src/test/src/28");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/28");
        assertEquals(0, c2);
    }

    @Test
    public void test29() throws Exception {
        int c = perform("blind", "src/test/src/29");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/29");
        assertEquals(1, c2);
    }

    @Test
    public void test30() throws Exception {
        int c = perform("blind", "src/test/src/30");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/30");
        assertEquals(0, c2);
    }

    @Test
    public void test31() throws Exception {
        int c = perform("blind", "src/test/src/31");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/31");
        assertEquals(0, c2);
    }

    @Test
    public void test32() throws Exception {
        int c = perform("blind", "src/test/src/32");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/32");
        assertEquals(0, c2);
    }

    @Test
    public void test33() throws Exception {
        int c = perform("blind", "src/test/src/33");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/33");
        assertEquals(0, c2);
    }

    @Test
    public void test34() throws Exception {
        int c = perform("blind", "src/test/src/34");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/34");
        assertEquals(0, c2);
    }

    @Test
    public void test35() throws Exception {
        int c = perform("blind", "src/test/src/35");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/35");
        assertEquals(0, c2);
    }

    @Test
    public void test36() throws Exception {
        int c = perform("blind", "src/test/src/36");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/36");
        assertEquals(1, c2);
    }

    @Test
    public void test37() throws Exception {
        int c = perform("blind", "src/test/src/37");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/37");
        assertEquals(1, c2);
    }

    @Test
    public void test38() throws Exception {
        int c = perform("blind", "src/test/src/38");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/38");
        assertEquals(0, c2);
    }

    @Test
    public void test39() throws Exception {
        int c = perform("blind", "src/test/src/39");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/39");
        assertEquals(0, c2);
    }

    @Test
    public void test40() throws Exception {
        int c = perform("blind", "src/test/src/40");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/40");
        assertEquals(1, c2);
    }

    @Test
    public void test41() throws Exception {
        int c = perform("blind", "src/test/src/41");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/41");
        assertEquals(1, c2);
    }

    @Test
    public void test42() throws Exception {
        int c = perform("blind", "src/test/src/42");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/42");
        assertEquals(0, c2);
    }

    @Test
    public void test43() throws Exception {
        int c = perform("blind", "src/test/src/43");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/43");
        assertEquals(0, c2);
    }

    @Test
    public void test44() throws Exception {
        int c = perform("blind", "src/test/src/44");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/44");
        assertEquals(0, c2);
    }

    @Test
    public void test45() throws Exception {
        int c = perform("blind", "src/test/src/45");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/45");
        assertEquals(0, c2);
    }

    @Test
    public void test46() throws Exception {
        int c = perform("blind", "src/test/src/46");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/46");
        assertEquals(0, c2);
    }

    @Test
    public void test47() throws Exception {
        int c = perform("blind", "src/test/src/47");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/47");
        assertEquals(0, c2);
    }

    @Test
    public void test48() throws Exception {
        int c = perform("blind", "src/test/src/48");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/48");
        assertEquals(0, c2);
    }

    @Test
    public void test49() throws Exception {
        int c = perform("blind", "src/test/src/49");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/49");
        assertEquals(1, c2);
    }

    @Test
    public void test50() throws Exception {
        int c = perform("blind", "src/test/src/50");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/50");
        assertEquals(1, c2);
    }

    @Test
    public void test51() throws Exception {
        int c = perform("blind", "src/test/src/51");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/51");
        assertEquals(0, c2);
    }

    @Test
    public void test51x() throws Exception {
        Properties conf = new Properties();
        conf.put("ignoreAnnotations", "true");
        int c = performWith("blind", "src/test/src/51", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/51", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test52() throws Exception {
        int c = perform("blind", "src/test/src/52");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/52");
        assertEquals(1, c2);
    }

    @Test
    public void test53() throws Exception {
        int c = perform("blind", "src/test/src/53");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/53");
        assertEquals(1, c2);
    }

    @Test
    public void test54() throws Exception {
        int c = perform("blind", "src/test/src/54");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/54");
        assertEquals(1, c2);
    }

    @Test
    public void test55() throws Exception {
        int c = perform("blind", "src/test/src/55");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/55");
        assertEquals(0, c2);
    }

    @Test
    public void test56() throws Exception {
        int c = perform("blind", "src/test/src/56");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/56");
        assertEquals(0, c2);
    }

    @Test
    public void test57() throws Exception {
        int c = perform("blind", "src/test/src/57");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/57");
        assertEquals(0, c2);
    }

    @Test
    public void test58() throws Exception {
        int c = perform("blind", "src/test/src/58");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/58");
        assertEquals(0, c2);
    }

    @Test
    public void test59() throws Exception {
        int c = perform("blind", "src/test/src/59");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/59");
        assertEquals(1, c2);
    }

    @Test
    public void test60() throws Exception {
        int c = perform("blind", "src/test/src/60");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/60");
        assertEquals(1, c2);
    }

    @Test
    public void test61() throws Exception {
        int c = perform("blind", "src/test/src/61");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/61");
        assertEquals(0, c2);
    }

    @Test
    public void test62() throws Exception {
        int c = perform("blind", "src/test/src/62");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/62");
        assertEquals(0, c2);
    }

    @Test
    public void test63() throws Exception {
        int c = perform("blind", "src/test/src/63");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/63");
        assertEquals(0, c2);
    }

    @Test
    public void test63x() throws Exception {
        Properties conf = new Properties();
        conf.put("addBlocks", "true");
        int c = performWith("blind", "src/test/src/63", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/63", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test64() throws Exception {
        int c = perform("blind", "src/test/src/64");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/64");
        assertEquals(0, c2);
    }

    @Test
    public void test64x() throws Exception {
        Properties conf = new Properties();
        conf.put("addBlocks", "true");
        int c = performWith("blind", "src/test/src/64", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/64", conf);
        assertEquals(0, c2);
    }

    @Test
    public void test65() throws Exception {
        int c = perform("blind", "src/test/src/65");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/65");
        assertEquals(0, c2);
    }

    @Test
    public void test66() throws Exception {
        int c = perform("blind", "src/test/src/66");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/66");
        assertEquals(0, c2);
    }

    @Test
    public void test67() throws Exception {
        int c = perform("blind", "src/test/src/67");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/67");
        assertEquals(0, c2);
    }

    @Test
    public void test68() throws Exception {
        int c = perform("blind", "src/test/src/68");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/68");
        assertEquals(1, c2);
    }

    @Test
    public void test69() throws Exception {
        int c = perform("blind", "src/test/src/69");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/69");
        assertEquals(0, c2);
    }

    @Test
    public void test69x() throws Exception {
        Properties conf = new Properties();
        conf.put("concatenateStrings", "true");
        int c = performWith("blind", "src/test/src/69", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/69", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test70() throws Exception {
        int c = perform("blind", "src/test/src/70");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/70");
        assertEquals(0, c2);
    }

    @Test
    public void test70x() throws Exception {
        Properties conf = new Properties();
        conf.put("concatenateStrings", "true");
        int c = performWith("blind", "src/test/src/70", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/70", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test71() throws Exception {
        int c = perform("blind", "src/test/src/71");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/71");
        assertEquals(0, c2);
    }

    @Test
    public void test72() throws Exception {
        int c = perform("blind", "src/test/src/72");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/72");
        assertEquals(0, c2);
    }

    @Test
    public void test73() throws Exception {
        int c = perform("blind", "src/test/src/73");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/73");
        assertEquals(0, c2);
    }

    @Test
    public void test74() throws Exception {
        int c = perform("blind", "src/test/src/74");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/74");
        assertEquals(0, c2);
    }

    @Test
    public void test75() throws Exception {
        int c = perform("blind", "src/test/src/75");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/75");
        assertEquals(0, c2);
    }

    @Test
    public void test75x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatVoidAsType", "true");
        int c = performWith("blind", "src/test/src/75", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/75", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test76() throws Exception {
        int c = perform("blind", "src/test/src/76");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/76");
        assertEquals(0, c2);
    }

    @Test
    public void test76x() throws Exception {
        Properties conf = new Properties();
        conf.put("concatenateStrings", "true");
        int c = performWith("blind", "src/test/src/76", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/76", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test77() throws Exception {
        int c = perform("blind", "src/test/src/77");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/77");
        assertEquals(0, c2);
    }

    @Test
    public void test77x() throws Exception {
        Properties conf = new Properties();
        conf.put("concatenateStrings", "true");
        int c = performWith("blind", "src/test/src/77", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/77", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test78() throws Exception {
        int c = perform("blind", "src/test/src/78");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/78");
        assertEquals(0, c2);
    }

    @Test
    public void test78x() throws Exception {
        Properties conf = new Properties();
        conf.put("ignoreParentheses", "true");
        int c = performWith("blind", "src/test/src/78", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/78", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test79() throws Exception {
        int c = perform("blind", "src/test/src/79");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/79");
        assertEquals(0, c2);
    }

    @Test
    public void test79x() throws Exception {
        Properties conf = new Properties();
        conf.put("ignoreParentheses", "true");
        int c = performWith("blind", "src/test/src/79", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/79", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test80() throws Exception {
        int c = perform("blind", "src/test/src/80");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/80");
        assertEquals(1, c2);
    }

    @Test
    public void test81() throws Exception {
        int c = perform("blind", "src/test/src/81");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/81");
        assertEquals(0, c2);
    }

    @Test
    public void test82() throws Exception {
        int c = perform("blind", "src/test/src/82");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/82");
        assertEquals(0, c2);
    }

    @Test
    public void test82x() throws Exception {
        Properties conf = new Properties();
        conf.put("ignoreParentheses", "true");
        int c = performWith("blind", "src/test/src/82", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/82", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test83() throws Exception {
        int c = perform("blind", "src/test/src/83");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/83");
        assertEquals(0, c2);
    }

    @Test
    public void test84() throws Exception {
        int c = perform("blind", "src/test/src/84");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/84");
        assertEquals(0, c2);
    }

    @Test
    public void test84x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatArrayDeclEqual", "true");
        int c = performWith("blind", "src/test/src/84", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/84", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test85() throws Exception {
        int c = perform("blind", "src/test/src/85");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/85");
        assertEquals(0, c2);
    }

    @Test
    public void test85x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatNullAsLiteral", "true");
        int c = performWith("blind", "src/test/src/85", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/85", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test86() throws Exception {
        int c = perform("blind", "src/test/src/86");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/86");
        assertEquals(0, c2);
    }

    @Test
    public void test86x() throws Exception {
        Properties conf = new Properties();
        conf.put("ignoreAnnotations", "true");
        int c = performWith("blind", "src/test/src/86", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/86", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test87() throws Exception {
        int c = perform("blind", "src/test/src/87");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/87");
        assertEquals(1, c2);
    }

    @Test
    public void test88() throws Exception {
        int c = perform("blind", "src/test/src/88");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/88");
        assertEquals(1, c2);
    }

    @Test
    public void test89() throws Exception {
        int c = perform("blind", "src/test/src/89");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/89");
        assertEquals(1, c2);
    }

    @Test
    public void test90() throws Exception {
        int c = perform("blind", "src/test/src/90");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src/90");
        assertEquals(2, c2);
    }

    @Test
    public void test91() throws Exception {
        int c = perform("blind", "src/test/src/91");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src/91");
        assertEquals(2, c2);
    }

    @Test
    public void test92() throws Exception {
        int c = perform("blind", "src/test/src/92");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/92");
        assertEquals(1, c2);
    }

    @Test
    public void test93() throws Exception {
        int c = perform("blind", "src/test/src/93");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/93");
        assertEquals(0, c2);
    }

    @Test
    public void test94() throws Exception {
        int c = perform("blind", "src/test/src/94");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/94");
        assertEquals(0, c2);
    }

    @Test
    public void test95() throws Exception {
        int c = perform("blind", "src/test/src/95");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/95");
        assertEquals(0, c2);
    }

    @Test
    public void test96() throws Exception {
        int c = perform("blind", "src/test/src/96");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/96");
        assertEquals(0, c2);
    }

    @Test
    public void test97() throws Exception {
        int c = perform("blind", "src/test/src/97");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/97");
        assertEquals(0, c2);
    }

    @Test
    public void test98() throws Exception {
        int c = perform("blind", "src/test/src/98");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/98");
        assertEquals(0, c2);
    }

    @Test
    public void test99() throws Exception {
        int c = perform("blind", "src/test/src/99");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src/99");
        assertEquals(0, c2);
    }

    @Test
    public void test100() throws Exception {
        int c = perform("blind", "src/test/src/100");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/100");
        assertEquals(0, c2);
    }

    @Test
    public void test101() throws Exception {
        int c = perform("blind", "src/test/src/101");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/101");
        assertEquals(0, c2);
    }

    @Test
    public void test102() throws Exception {
        int c = perform("blind", "src/test/src/102");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/102");
        assertEquals(0, c2);
    }

    @Test
    public void test103() throws Exception {
        int c = perform("blind", "src/test/src/103");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/103");
        assertEquals(0, c2);
    }

    @Test
    public void test104() throws Exception {
        int c = perform("blind", "src/test/src/104");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/104");
        assertEquals(0, c2);
    }

    @Test
    public void test105() throws Exception {
        int c = perform("blind", "src/test/src/105");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/105");
        assertEquals(0, c2);
    }

    @Test
    public void test106() throws Exception {
        int c = perform("blind", "src/test/src/106");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/106");
        assertEquals(0, c2);
    }

    @Test
    public void test107() throws Exception {
        int c = perform("blind", "src/test/src/107");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/107");
        assertEquals(0, c2);
    }

    @Test
    public void test108() throws Exception {
        int c = perform("blind", "src/test/src/108");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/108");
        assertEquals(0, c2);
    }

    @Test
    public void test109() throws Exception {
        int c = perform("blind", "src/test/src/109");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/109");
        assertEquals(0, c2);
    }

    @Test
    public void test110() throws Exception {
        int c = perform("blind", "src/test/src/110");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/110");
        assertEquals(0, c2);
    }

    @Test
    public void test111() throws Exception {
        int c = perform("blind", "src/test/src/111");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/111");
        assertEquals(0, c2);
    }

    @Test
    public void test112() throws Exception {
        int c = perform("blind", "src/test/src/112");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/112");
        assertEquals(0, c2);
    }

    @Test
    public void test113() throws Exception {
        int c = perform("blind", "src/test/src/113");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/113");
        assertEquals(0, c2);
    }

    @Test
    public void test114() throws Exception {
        int c = perform("blind", "src/test/src/114");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/114");
        assertEquals(0, c2);
    }

    @Test
    public void test115() throws Exception {
        int c = perform("blind", "src/test/src/115");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/115");
        assertEquals(0, c2);
    }

    @Test
    public void test116() throws Exception {
        int c = perform("blind", "src/test/src/116");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/116");
        assertEquals(0, c2);
    }

    @Test
    public void test117() throws Exception {
        int c = perform("blind", "src/test/src/117");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/117");
        assertEquals(0, c2);
    }

    @Test
    public void test118() throws Exception {
        int c = perform("blind", "src/test/src/118");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/118");
        assertEquals(0, c2);
    }

    @Test
    public void test119() throws Exception {
        int c = perform("blind", "src/test/src/119");
        assertEquals(2, c);
        int c2 = perform("consistent", "src/test/src/119");
        assertEquals(1, c2);
    }

    @Test
    public void test120() throws Exception {
        int c = perform("blind", "src/test/src/120");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/120");
        assertEquals(0, c2);
    }

    @Test
    public void test121() throws Exception {
        int c = perform("blind", "src/test/src/121");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/121");
        assertEquals(0, c2);
    }

    @Test
    public void test122() throws Exception {
        int c = perform("blind", "src/test/src/122");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/122");
        assertEquals(0, c2);
    }

    @Test
    public void test123() throws Exception {
        int c = perform("blind", "src/test/src/123");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/123");
        assertEquals(0, c2);
    }

    @Test
    public void test124() throws Exception {
        int c = perform("blind", "src/test/src/124");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/124");
        assertEquals(0, c2);
    }

    @Test
    public void test125() throws Exception {
        int c = perform("blind", "src/test/src/125");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/125");
        assertEquals(0, c2);
    }

    @Test
    public void test126() throws Exception {
        int c = perform("blind", "src/test/src/126");
        assertEquals(1, c);
        int c2 = perform("consistent", "src/test/src/126");
        assertEquals(0, c2);
    }

    @Test
    public void test127() throws Exception {
        int c = perform("blind", "src/test/src/127");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/127");
        assertEquals(0, c2);
    }

    @Test
    public void test127x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatSuperThisAsIdentifier", "true");
        int c = performWith("blind", "src/test/src/127", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/127", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test128() throws Exception {
        int c = perform("blind", "src/test/src/128");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/128");
        assertEquals(0, c2);
    }

    @Test
    public void test128x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatSuperThisAsIdentifier", "true");
        int c = performWith("blind", "src/test/src/128", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/128", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test129() throws Exception {
        int c = perform("blind", "src/test/src/129");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/129");
        assertEquals(0, c2);
    }

    @Test
    public void test129x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatSuperThisAsIdentifier", "true");
        int c = performWith("blind", "src/test/src/129", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/129", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test130() throws Exception {
        int c = perform("blind", "src/test/src/130");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/130");
        assertEquals(0, c2);
    }

    @Test
    public void test130x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatSuperThisAsIdentifier", "true");
        int c = performWith("blind", "src/test/src/130", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/130", conf);
        assertEquals(1, c2);
    }

    @Test
    public void test131() throws Exception {
        int c = perform("blind", "src/test/src/131");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/131");
        assertEquals(0, c2);
    }

    @Test
    public void test131x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatSuperThisAsIdentifier", "true");
        int c = performWith("blind", "src/test/src/131", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/131", conf);
        assertEquals(0, c2);
    }

    @Test
    public void test132() throws Exception {
        int c = perform("blind", "src/test/src/132");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/132");
        assertEquals(0, c2);
    }

    @Test
    public void test132x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatSuperThisAsIdentifier", "true");
        int c = performWith("blind", "src/test/src/132", conf);
        assertEquals(0, c);
        int c2 = performWith("consistent", "src/test/src/132", conf);
        assertEquals(0, c2);
    }

    @Test
    public void test133() throws Exception {
        int c = perform("blind", "src/test/src/133");
        assertEquals(0, c);
        int c2 = perform("consistent", "src/test/src/133");
        assertEquals(0, c2);
    }

    @Test
    public void test133x() throws Exception {
        Properties conf = new Properties();
        conf.put("treatSuperThisAsIdentifier", "true");
        int c = performWith("blind", "src/test/src/133", conf);
        assertEquals(1, c);
        int c2 = performWith("consistent", "src/test/src/133", conf);
        assertEquals(1, c2);
    }
}
