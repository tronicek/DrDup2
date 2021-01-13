package edu.tarleton.drdup2;

import java.io.FileReader;
import java.util.Properties;

/**
 * The entry class.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("expected argument: properties file (e.g. JavaAPI.properties)");
            System.exit(0);
        }
        Properties conf = new Properties();
        try (FileReader in = new FileReader(args[0])) {
            conf.load(in);
        }
        Engine eng = Engine.instance(conf);
        String command = conf.getProperty("command");
        switch (command) {
            case "findClones":
                eng.findClones();
                break;
            case "printTrie":
                eng.printTrie();
                break;
            default:
                System.err.println("unknown command: " + command);
        }
    }
}
