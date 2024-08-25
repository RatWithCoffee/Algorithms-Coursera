package word_net;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {

    // private final Map<String, Integer> nouns; // <nouns, id>

    private final TreeMap<String, List<Integer>> nounsIds; // <noun, [id1, id2, ...]>


    private final SAP wordNet;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        if (synsetsFile == null || hypernymsFile == null) throw new IllegalArgumentException();

        // read synonyms and its ids
        In synsetsReader = new In(synsetsFile);
        nounsIds = new TreeMap<>();

        int id = 0;
        String[] arrStr;
        int amountRoots = 0;
        List<Integer> newID;
        while (synsetsReader.hasNextLine()) {
            for (String syn : synsetsReader.readLine().split(",")[1].split("\\s")) {
                if (nounsIds.containsKey(syn)) {
                    nounsIds.get(syn).add(id);
                } else {
                    newID = new ArrayList<>();
                    newID.add(id);
                    nounsIds.put(syn, newID);
                }
            }
            id++;
        }

        // read hypernyms and construct wordNet
        In hypernymsReader = new In(hypernymsFile);
        Digraph digraph = new Digraph(nounsIds.size());

        for (int currId = 0; currId < id; currId++) {
            // read hypernyms
            arrStr = hypernymsReader.readLine().split(",");
            for (int i = 1; i < arrStr.length; i++) {
                digraph.addEdge(currId, Integer.parseInt(arrStr[i]));
            }

            if (arrStr.length == 1) {
                amountRoots++; // found the root
            }
        }

        if (amountRoots != 1) throw new IllegalArgumentException();

        // DirectedCycle dc = new DirectedCycle(digraph);
        // if (dc.hasCycle()) throw new IllegalArgumentException();


        wordNet = new SAP(digraph);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new ArrayList<>(nounsIds.keySet());
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();

        return nounsIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        return wordNet.length(nounsIds.get(nounA), nounsIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        int idAncestor = wordNet.ancestor(nounsIds.get(nounA), nounsIds.get(nounB));

        StringBuilder synset = new StringBuilder();
        for (Map.Entry<String, List<Integer>> pair : nounsIds.entrySet()) {
            for (Integer ids : pair.getValue()) {
                if (idAncestor == ids) {
                    synset.append(pair.getKey()).append(" ");
                }
            }
        }


        return synset.toString();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");



        System.out.println(wn.sap("fish_oil", "oil_of_cloves"));
        System.out.println(wn.sap("simple_closed_curve", "genus_Anoa"));
        System.out.println(wn.distance("simple_closed_curve", "genus_Anoa"));

    }


}