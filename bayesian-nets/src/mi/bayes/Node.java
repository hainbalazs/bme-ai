package mi.bayes;

import java.util.ArrayList;

public class Node {
    public int id;
    public int numOfParents;
    public ArrayList<Integer> parents;
    public int numOfDF;
    ArrayList<ArrayList<Integer>> boolTable = new ArrayList<>();
    ArrayList<ArrayList<Double>> probabilities = new ArrayList<>();
    public boolean isEvidence;
    public int vEvidence;

    public Node(int id, int numOfParents, ArrayList<Integer> parents, int numOfDF, ArrayList<ArrayList<Integer>> boolTable, ArrayList<ArrayList<Double>> probabilities) {
        this.id = id;
        this.numOfParents = numOfParents;
        this.parents = parents;
        this.numOfDF = numOfDF;
        this.boolTable = boolTable;
        this.probabilities = probabilities;
        isEvidence = false;
        vEvidence = -1;
    }

    public void setEvidence(int e){
        isEvidence = true;
        vEvidence = e;
    }
}
