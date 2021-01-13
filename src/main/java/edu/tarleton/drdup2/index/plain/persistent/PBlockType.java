package edu.tarleton.drdup2.index.plain.persistent;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum that represents various block types.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public enum PBlockType {
    DIRECT,
    INDIRECT,
    INDIRECT2,
    INDIRECT3,
    INDIRECT4;

    private static final Map<Integer, PBlockType> map = new HashMap<>();

    static {
        for (PBlockType t : PBlockType.values()) {
            map.put(t.ordinal(), t);
        }
    }

    public static PBlockType fromOrdinal(int ordinal) {
        return map.get(ordinal);
    }
}
