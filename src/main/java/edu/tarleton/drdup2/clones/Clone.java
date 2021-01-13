package edu.tarleton.drdup2.clones;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The representation of a clone.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Clone {

    private final Integer similarity;
    private final Pos[] positions;

    public Clone(Integer similarity, Pos[] positions) {
        this.similarity = similarity;
        this.positions = positions;
    }

    public Integer getSimilarity() {
        return similarity;
    }

    public Pos[] getPositions() {
        return positions;
    }

    public boolean subsetOf(Clone that) {
        for (Pos pos : positions) {
            if (!subsetOf(pos, that.positions)) {
                return false;
            }
        }
        return true;
    }

    private boolean subsetOf(Pos pos, Pos[] positions) {
        for (Pos p : positions) {
            if (pos.subsetOf(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(similarity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Clone) {
            Clone that = (Clone) obj;
            return similarity.equals(that.similarity)
                    && equal(positions, that.positions);
        }
        return false;
    }

    private boolean equal(Pos[] pp1, Pos[] pp2) {
        List<Pos> list1 = Arrays.asList(pp1);
        List<Pos> list2 = Arrays.asList(pp2);
        return list1.containsAll(list2) && list2.containsAll(list1);
    }
}
