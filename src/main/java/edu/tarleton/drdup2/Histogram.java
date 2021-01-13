package edu.tarleton.drdup2;

import java.util.Map;
import java.util.TreeMap;

/**
 * The class that represents histogram.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Histogram {

    private final Map<Integer, Integer> mapEdgesNodes = new TreeMap<>();
    private final Map<Integer, Integer> mapPosEdges = new TreeMap<>();

    public void storeNode(int edges) {
        Integer c = mapEdgesNodes.getOrDefault(edges, 0);
        mapEdgesNodes.put(edges, c + 1);
    }

    public void storeEdge(int pos) {
        Integer c = mapPosEdges.getOrDefault(pos, 0);
        mapPosEdges.put(pos, c + 1);
    }

    public void print() {
        System.out.println("histogram edges:#nodes");
        for (Integer edges : mapEdgesNodes.keySet()) {
            Integer c = mapEdgesNodes.get(edges);
            System.out.printf("%d:%d ", edges, c);
        }
        System.out.println();
        System.out.println("histogram pos:#edges");
        for (Integer pos : mapPosEdges.keySet()) {
            Integer c = mapPosEdges.get(pos);
            System.out.printf("%d:%d ", pos, c);
        }
        System.out.println();
    }
}
