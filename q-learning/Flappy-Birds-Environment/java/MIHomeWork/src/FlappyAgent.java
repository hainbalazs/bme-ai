import java.util.*;

public class FlappyAgent {
    static class StateDTO {
        public int birdPos;
        public int birdSpeed;
        public int tubeDistance;
        public int tubeHeight;

        public StateDTO(int birdPos, int birdSpeed, int tubeDistance, int tubeHeight) {
            this.birdPos = birdPos;
            this.birdSpeed = birdSpeed;
            this.tubeDistance = tubeDistance;
            this.tubeHeight = tubeHeight;
        }

        @Override
        public String toString() {
            return "StateDTO{" +
                    "birdPos=" + birdPos +
                    ", birdSpeed=" + birdSpeed +
                    ", tubeDistance=" + tubeDistance +
                    ", tubeHeight=" + tubeHeight +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateDTO stateDTO = (StateDTO) o;
            return birdPos == stateDTO.birdPos &&
                    birdSpeed == stateDTO.birdSpeed &&
                    tubeDistance == stateDTO.tubeDistance &&
                    tubeHeight == stateDTO.tubeHeight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(birdPos, birdSpeed, tubeDistance, tubeHeight);
        }
    }

    static class QTable implements java.io.Serializable {
        public double[][][][][] table;

        public QTable() {

        }

        public QTable(int[] stateSpaceSize, int actionDimension) {
            table = new double[stateSpaceSize[0]][stateSpaceSize[1]][stateSpaceSize[2]][stateSpaceSize[3]][actionDimension];
        }

        public double[] getActions(FlappyAgent.StateDTO state) {
            return table[state.birdPos][state.birdSpeed][state.tubeDistance][state.tubeHeight];
        }

        public void setAction(FlappyAgent.StateDTO state, int action, double actionScore) {
            table[state.birdPos][state.birdSpeed][state.tubeDistance][state.tubeHeight][action] = actionScore;
        }

        public FlappyAgent.QTable copy() {
            FlappyAgent.QTable res = new FlappyAgent.QTable();
            res.table = this.table.clone();
            return res;
        }
    }

    QTable qTable;
    HashSet<Double> set;
    HashMap<StateDTO, Integer[]> nTable;
    int[] actionSpace;
    int nIterations;
    int minFreq = 0;

    boolean test = false;

    public FlappyAgent(int[] observationSpaceSize, int[] actionSpace, int nIterations) {
        this.qTable = new QTable(observationSpaceSize,actionSpace.length);
        this.actionSpace = actionSpace;
        this.nIterations = nIterations;
        this.nTable = new HashMap<>();
        this.set = new HashSet<>();
    }

    public int step(StateDTO state) {
        double[] actionScore = qTable.getActions(state);

        if(actionScore == null) {
            actionScore = new double[]{0.0, 0.0};
        }
            Integer[] frequency;

            if (nTable.containsKey(state))
                frequency = nTable.get(state);
            else
                frequency = new Integer[]{0, 0};

            int action = 0;
            if (EExF(actionScore[0], frequency[0]) < EExF(actionScore[1], frequency[1]))
                action = 1;

        return action;
    }

    private double EExF(double actionScore, Integer freq) {
        double exploreRate = 3.0;

        if(freq < minFreq)
            return exploreRate;
        else
            return actionScore;
    }

    public void epochEnd(int epochRewardSum) {
        // noop
    }

    public void learn(StateDTO oldState, int action, StateDTO newState, int reward) {

        Integer[] oldFreq = new Integer[]{0, 0};
        if(nTable.containsKey(oldState))
            oldFreq = nTable.get(oldState);

        oldFreq[action] += 1;
        nTable.put(oldState,  oldFreq);

        reward = rescaleReward(reward);

        double learningRate = 0.000003;
        double discount = 0.02;
        double[] oldActionScore = qTable.getActions(oldState);
        double[] newActionScore = qTable.getActions(newState);

        double res =
                oldActionScore[action] + learningRate * ((double) nTable.get(oldState)[action])  * (((double)reward) + discount * Math.max(newActionScore[0], newActionScore[1]) - oldActionScore[action]);

        qTable.setAction(   oldState,
                            action,
                            res
        );
        set.add(res);
    }

    private int rescaleReward(int reward) {
        switch (reward){
            case -1:
                return -1;
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return reward;
        }
    }

    public void trainEnd() {
        /*...*/
        minFreq = 0;
        // itt mit csinalunk

        //qTable = null;//
        test = true;
    }
}
