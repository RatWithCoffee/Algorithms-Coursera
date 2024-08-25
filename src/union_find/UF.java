package union_find;

public class UF {
    private int[] id;
    private int[] weight;


    public static void main(String[] args) {
    }

    UF(int n) { //initialize union-find data structure with N objects (0 to N � 1)
        id = new int[n];
        weight = new int[n];
        for (int i = 0; i < id.length; i++) {
            id[i] = i;
            weight[i] = 1;
        }
    }


    //using weights array
    void union(int p, int q) { //add connection between p and q
        int rootP = root(p);
        int rootQ = root(q);
        if (weight[rootP] > weight[rootQ]) {
            id[rootQ] = rootP;
            weight[rootP] += weight[rootQ];
        } else {
            id[rootP] = rootQ;
            weight[rootQ] += weight[rootP];
        }

    }

    boolean connected(int p, int q) { //are p and q in the same component?
        return root(p) == root(q);
    }

    int root(int p) { //find item's root
        while (p != id[p]) {
            id[p] = id[id[p]]; //path compression
            p = id[p];
        }
        return p;
    }

}
