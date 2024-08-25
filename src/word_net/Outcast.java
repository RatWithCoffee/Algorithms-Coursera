package word_net;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    public Outcast(WordNet wordnet) { // constructor takes a WordNet object
        wordNet = wordnet;
    }

    public String outcast(String[] nouns) { // given an array of WordNet nouns, return an outcast
        int maxDist = 0;
        int currDist;
        String outcast = "";
        for (int i = 0; i < nouns.length; i++) {
            currDist = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) currDist += wordNet.distance(nouns[i], nouns[j]);
            }
            if (currDist > maxDist) {
                outcast = nouns[i];
                maxDist = currDist;
            }
        }

        return outcast;
    }

    public static void main(String[] args) { // see test client below
        // WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        // Outcast oc = new Outcast(wn);
        // System.out.println(oc.outcast(new String[]{"horse", "zebra", "cat", "bear",  "table"}));

        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}