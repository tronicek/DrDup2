package edu.tarleton.drdup2.clones;

import com.github.javaparser.Position;
import java.util.Comparator;

/**
 * The comparator for positions.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PosComparator implements Comparator<Pos> {

    @Override
    public int compare(Pos p1, Pos p2) {
        String file1 = p1.getFile();
        String file2 = p2.getFile();
        int c = file1.compareTo(file2);
        if (c != 0) {
            return c;
        }
        Position start1 = p1.getStart();
        Position start2 = p2.getStart();
        return start1.compareTo(start2);
    }
}
