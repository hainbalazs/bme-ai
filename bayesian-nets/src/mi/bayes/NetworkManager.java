package mi.bayes;

import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class NetworkManager {
    private ArrayList<Node> nodes;
    private ArrayList<Node> evidenceNodes;
    private Node target;
    private int nV;
    private int offset = 0;


    public NetworkManager(){
        nodes = new ArrayList<>();
        evidenceNodes = new ArrayList<>();
    }

    public void run() throws IOException {
        read();
        //test();
        if(canReduce())
            reduce();

        ArrayList<Double> result = deduce(target, evidenceNodes, nodes);
        write(result);
    }

    private void write(ArrayList<Double> result) {
        for(Double q : result){
            System.out.println(q);
        }
    }

    private void read() throws IOException {
        /*
         * 1) Csomopontok szamanak beolvasasa
         * 2) Csomoponotok feldolgozasa
         * 3) Evidencia valtozok szamanak beolvasasa
         * 4) Csomopontok evidencia valtozokka tevese
         * 5) Celvaltozo kijelölese
         */

        Scanner s = new Scanner(System.in);
        //File testfile = new File("bn_examples/input3.txt");
        //System.out.println("Attempting to read from file in: "+testfile.getCanonicalPath());
        //Scanner s = new Scanner(testfile);

        /// Csomopontok szamanak beolvasasa
        nV = s.nextInt();
        s.nextLine();
        /// Csomoponotok feldolgozasa
        for(int i = 0; i < nV; i++) {
            // params of a node
            int id;
            int numOfParents;
            ArrayList<Integer> parents = new ArrayList<>();
            int numOfDF;
            ArrayList<ArrayList<Integer>> boolTable = new ArrayList<>();
            ArrayList<ArrayList<Double>> probabilities = new ArrayList<>();

            // egy sor beolvasasa
            id = i;
            // lehetseges ertekek
            numOfDF = s.nextInt();
            // szulok szama
            numOfParents = s.nextInt();
            int bTableHeight = 1;
            for(int j = 0; j < numOfParents; j++){
                int Pi = s.nextInt();
                // szulok indexeinek feljegyzese
                parents.add(Pi);
                bTableHeight *= nodes.get(Pi).numOfDF;
            }

            // booltabla keszitese
            if(numOfParents > 0) {
                for (int y = 0; y < bTableHeight; y++) {
                    String[] bRowParts = s.next().split(":");
                    String[] boolParts = bRowParts[0].split(",");
                    String[] probParts = bRowParts[1].split(",");
                    ArrayList<Integer> rowT = new ArrayList<>();
                    for (int x = 0; x < numOfParents; x++) {
                        rowT.add(Integer.parseInt(boolParts[x]));
                    }
                    boolTable.add(rowT);
                    ArrayList<Double> rowP = new ArrayList<>();
                    for (int x = 0; x < numOfDF; x++) {
                        rowP.add(Double.parseDouble(probParts[x]));
                    }
                    probabilities.add(rowP);
                }
            }
            else {
                String[] probs = s.next().split(",");
                ArrayList<Integer> r1 = new ArrayList<>();
                ArrayList<Double> r2 = new ArrayList<>();
                for (int k = 0; k < numOfDF; k++){
                    r2.add(Double.parseDouble(probs[k]));
                }
                r1.add(0);
                boolTable.add(r1);
                probabilities.add(r2);
            }
            Node node = new Node(id, numOfParents, parents, numOfDF, boolTable, probabilities);
            nodes.add(node);
            s.nextLine();
        }

        int numOfE = s.nextInt();
        for(int i = 0; i < numOfE; i++){
            int Ni = s.nextInt();
            int Nv = s.nextInt();
            Node eNode = nodes.get(Ni);
            eNode.setEvidence(Nv);
            evidenceNodes.add(eNode);
        }

        target = nodes.get(s.nextInt());

        s.close();
    }

    public ArrayList<Double> deduce(Node _target, ArrayList<Node> _evidenceNodes, ArrayList<Node> bn) {
        ArrayList<Double> distribution = new ArrayList<>();
        HashMap<Integer, Integer> variables = new HashMap<>();
        for(Node e: _evidenceNodes){
            variables.put(e.id, e.vEvidence);
        }

        for(int x = 0; x < _target.numOfDF; x++){
            variables.put(_target.id, x);
            HashMap<Integer, Integer> variablesCopy = new HashMap<>(variables);
            distribution.add(listAll(offset, bn, variablesCopy));
        }
        return normalize(distribution);
        //return distribution;
    }

    private void reduce() {
        offset += nV-10;
    }

    private boolean canReduce() {
        if(target.id < nV - 10)
            return false;

        for(Node e : evidenceNodes){
            if(e.id == nV - 10)
                return true;
        }

        return false;
    }

    private double listAll(int nid, ArrayList<Node> bn, HashMap<Integer, Integer> variables){
        if(nid >= bn.size())
            return 1.0;

        Node observedNode = bn.get(nid);
        if(variables.containsKey(observedNode.id))
            return (probability(new Pair<>(observedNode.id, variables.get(observedNode.id)), variables)) * listAll(nid+1, bn, variables);
        else {
            double result = 0.0;
            for(int y = 0; y < observedNode.numOfDF; y++){
                variables.put(observedNode.id, y);
                HashMap<Integer, Integer> variablesCopy = new HashMap<>(variables);
                double product = probability(new Pair<>(observedNode.id, y), variablesCopy) * listAll(nid+1, bn, variablesCopy);
                result += product;
            }
            return result;
        }
    }

    private double probability(Pair<Integer, Integer> Y, HashMap<Integer, Integer> vars){
        Node observedNode = nodes.get(Y.getKey());
        if(observedNode.numOfParents == 0) {
            double p = observedNode.probabilities.get(0).get(Y.getValue());
            return p;
        }

        for(int y = 0; y < observedNode.boolTable.size(); y++){
            boolean match = true;
            for (int x = 0; x < observedNode.numOfParents; x++) {
                int parentId = observedNode.parents.get(x);
                match = match && (observedNode.boolTable.get(y).get(x).equals(vars.get(parentId)));
            }
            if(match){
                double p = observedNode.probabilities.get(y).get(Y.getValue());
                return p;
            }
        }

        return -1.0;
    }

    private ArrayList<Double> normalize(ArrayList<Double> q) {
        double sum = 0.0;
        for(double r : q)
            sum += r;

        ArrayList<Double> result = new ArrayList<>();
        for (Double aDouble : q) result.add(aDouble / sum);

        return result;
    }


    public void test() {
        System.out.println(nodes.size());
        for(Node n : nodes){
            //System.out.print(n.id + "\t");
            System.out.print(n.numOfDF + "\t");
            System.out.print(n.numOfParents + "\t");
            for(int i = 0; i < n.numOfParents; i++){
                System.out.print(n.parents.get(i) + "\t");
            }

            for(int i = 0; i < n.boolTable.size(); i++){
                for(int j = 0; j < n.boolTable.get(i).size(); j++){
                    System.out.print(n.boolTable.get(i).get(j) + " ");
                }
            }
            System.out.print(":");
            for(int i = 0; i < n.probabilities.size(); i++){
                for(int j = 0; j < n.probabilities.get(i).size(); j++){
                    System.out.print(n.probabilities.get(i).get(j) + " ");
                }
            }
            System.out.println(";");
        }
        System.out.println(evidenceNodes.size());
        for(Node n: evidenceNodes){
            System.out.println(n.id + ":" + n.vEvidence);
        }

        System.out.println(target.id);
    }


}
