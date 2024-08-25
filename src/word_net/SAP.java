package word_net;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();

        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) throw new IllegalArgumentException();

        if (v == w) return 0;

        Map<Integer, Integer> adjacentVertices = new HashMap<>();
        adjacentVertices.put(v, 1);
        adjacentVertices.put(w, -1);

        Queue<Integer> vQueue = new Queue<>();
        vQueue.enqueue(v);
        Queue<Integer> wQueue = new Queue<>();
        wQueue.enqueue(w);

        return bfsLength(vQueue, wQueue, adjacentVertices) - 2;

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) throw new IllegalArgumentException();

        if (v == w) return v;

        Map<Integer, Boolean> adjacentVertices = new HashMap<>();
        adjacentVertices.put(v, true);
        adjacentVertices.put(w, false);

        Queue<Integer> vQueue = new Queue<>();
        vQueue.enqueue(v);
        Queue<Integer> wQueue = new Queue<>();
        wQueue.enqueue(w);

        return bfsAncestor(vQueue, wQueue, adjacentVertices);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> vSet, Iterable<Integer> wSet) {
        if (vSet == null || wSet == null) throw new IllegalArgumentException();

        Map<Integer, Integer> adjacentVertices = new HashMap<>();
        Queue<Integer> vQueue = new Queue<>();
        Queue<Integer> wQueue = new Queue<>();

        for (Integer vert : vSet) {
            if (vert == null || vert < 0 || vert >= digraph.V()) throw new IllegalArgumentException();

            adjacentVertices.put(vert, 1);
            vQueue.enqueue(vert);
        }
        for (Integer vert : wSet) {
            if (vert == null || vert < 0 || vert >= digraph.V()) throw new IllegalArgumentException();

            // check common vertices
            if (adjacentVertices.containsKey(vert) && adjacentVertices.get(vert) > 0) return 0;

            adjacentVertices.put(vert, -1);
            wQueue.enqueue(vert);
        }

        return bfsLength(vQueue, wQueue, adjacentVertices) - 2;

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> vSet, Iterable<Integer> wSet) {
        if (vSet == null || wSet == null) throw new IllegalArgumentException();

        Map<Integer, Boolean> adjacentVertices = new HashMap<>();
        Queue<Integer> vQueue = new Queue<>();
        Queue<Integer> wQueue = new Queue<>();

        for (Integer vert : vSet) {
            if (vert == null || vert < 0 || vert >= digraph.V()) throw new IllegalArgumentException();

            adjacentVertices.put(vert, true);
            vQueue.enqueue(vert);
        }
        for (Integer vert : wSet) {
            if (vert == null || vert < 0 || vert >= digraph.V()) throw new IllegalArgumentException();

            // check common vertices
            if (adjacentVertices.containsKey(vert) && adjacentVertices.get(vert)) return vert;

            adjacentVertices.put(vert, false);
            wQueue.enqueue(vert);
        }

        return bfsAncestor(vQueue, wQueue, adjacentVertices);
    }

    private int bfsLength(Queue<Integer> vQueue, Queue<Integer> wQueue, Map<Integer, Integer> adjacentVertices) {
        int vDist;
        int wDist;
        int currVert;
        while (!vQueue.isEmpty() || !wQueue.isEmpty()) {
            if (!vQueue.isEmpty()) {
                currVert = vQueue.dequeue();
                vDist = adjacentVertices.get(currVert) + 1;
                for (Integer vert : digraph.adj(currVert)) {
                    if (adjacentVertices.containsKey(vert)) {
                        if (adjacentVertices.get(vert) < 0) {
                            return -adjacentVertices.get(vert) + vDist;
                        } else if (adjacentVertices.get(vert) > vDist) { // find shorter path
                            adjacentVertices.put(vert, vDist);
                        } else { // maybe find cycle or add this vertex is useless
                            continue;
                        }
                    } else {
                        adjacentVertices.put(vert, vDist);
                    }

                    vQueue.enqueue(vert);
                }
            }

            if (!wQueue.isEmpty()) {
                currVert = wQueue.dequeue();
                wDist = adjacentVertices.get(currVert) - 1;
                for (Integer vert : digraph.adj(currVert)) {
                    if (adjacentVertices.containsKey(vert)) {
                        if (adjacentVertices.get(vert) > 0) {
                            return adjacentVertices.get(vert) - wDist;
                        } else if (adjacentVertices.get(vert) < wDist) { // find shorter path
                            adjacentVertices.put(vert, wDist);
                        } else { // maybe find cycle or add this vertex is useless
                            continue;
                        }
                    } else {
                        adjacentVertices.put(vert, wDist);
                    }

                    wQueue.enqueue(vert);
                }
            }

        }

        return 1;
    }

    private int bfsAncestor(Queue<Integer> vQueue, Queue<Integer> wQueue, Map<Integer, Boolean> adjacentVertices) {
        while (!vQueue.isEmpty() || !wQueue.isEmpty()) {
            if (!vQueue.isEmpty()) {
                for (Integer vert : digraph.adj(vQueue.dequeue())) {
                    if (adjacentVertices.containsKey(vert)) {
                        if (!adjacentVertices.get(vert)) {
                            return vert;
                        } else { // maybe find cycle
                            continue;
                        }
                    }

                    vQueue.enqueue(vert);
                    adjacentVertices.put(vert, true);
                }
            }

            if (!wQueue.isEmpty()) {
                for (Integer vert : digraph.adj(wQueue.dequeue())) {
                    if (adjacentVertices.containsKey(vert)) {
                        if (adjacentVertices.get(vert)) {
                            return vert;
                        } else { // maybe find cycle
                            continue;
                        }
                    }

                    wQueue.enqueue(vert);
                    adjacentVertices.put(vert, false);
                }
            }

        }

        return -1;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        System.out.println(args[0]);
        SAP sap = new SAP(new Digraph(in));

        int i = 7;
        int j = 17;
        System.out.println(i + ", " + j + " = " + sap.ancestor(i, j) + ", " + sap.length(i, j));
        // for (int i = 0; i < 25; i++) {
        //     for (int j = 0; j < 25; j++) {
        //         System.out.println(i + ", " + j + " = " + sap.ancestor(i, j) + ", " + sap.length(i, j));
        //     }
        // }

        // Integer[] arr1 = {13, 23, 24};
        // Integer[] arr2 = {6, 16, 17};
        // List<Integer> a = Arrays.asList(arr1);
        // List<Integer> b = Arrays.asList(arr2);
        //
        // System.out.println(a + ", " + b + " = " + sap.ancestor(a, b) + ", " + sap.length(a, b));


        // System.out.println(sap.length(3, 11));
        // System.out.println(sap.ancestor(3, 11));
        //
        // System.out.println(sap.length(9, 12));
        // System.out.println(sap.ancestor(9, 12));
        //
        // System.out.println(sap.length(7, 2));
        // System.out.println(sap.ancestor(7, 2));
        //
        //
        // System.out.println(sap.length(1, 6));
        // System.out.println(sap.ancestor(1, 6));


    }
}
