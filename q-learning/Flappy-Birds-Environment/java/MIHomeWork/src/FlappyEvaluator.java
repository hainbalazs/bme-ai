import java.util.ArrayList;
import java.util.List;

public class FlappyEvaluator {
    List<Integer> epochIterationsList = new ArrayList<>();
    List<Integer> epochRewardsList = new ArrayList<>();
    int bestEpochRewardSum = -1;
    int bestEpoch = 0;

    int nIterations = (int) 2e5;
    int iteration = 0;
    int epoch = 0;

    int printEpochInterval = 500;

    FlappyEnv env = new FlappyEnv();
    FlappyAgent agent = new FlappyAgent(env.getObservationSpaceSize(),env.getActionSpace(), nIterations);

    public FlappyEvaluator() {

    }
    public void run() {
        while (iteration < nIterations) {
            ObservationDTO observation = env.reset();

            int epochIteration = 0, epochRewardSum = 0;
            boolean done = false;

            while(!done) {
                int action = agent.step(observation.state);
                ObservationDTO newObservation = env.step(action);
                agent.learn(observation.state, action, newObservation.state, newObservation.reward);

                observation = newObservation;

                epochIteration += 1;
                epochRewardSum += observation.reward;
                done = observation.done;

                iteration += 1;
            }
            if (epochRewardSum > bestEpochRewardSum) {
                bestEpochRewardSum = epochRewardSum;
                bestEpoch = epoch;
            }
            agent.epochEnd(epochRewardSum);

            if(epoch % printEpochInterval == 0) {
                System.out.println("Epoch: "+epoch);
                System.out.println("Iteration: " + iteration + " " + String.format("%.2f", 100.0 * iteration / nIterations) + "%%");
                System.out.println("Best aggregate reward: "+ bestEpochRewardSum);
            }
            epochIterationsList.add(epochIteration);
            epochRewardsList.add(epochRewardSum);

            epoch += 1;
        }
        agent.trainEnd();

        System.out.println("Training finished.");
        System.out.println("Last 10 iterations: "+ epochIterationsList.subList(epochIterationsList.size()-Math.min(epochIterationsList.size(),10), epochIterationsList.size()));
        System.out.println("Last 10 aggregate rewards: "+ epochRewardsList.subList(epochRewardsList.size()-Math.min(epochRewardsList.size(),10), epochRewardsList.size()));
        System.out.println("Best aggregate reward: "+ bestEpochRewardSum);
        System.out.println("Best epoch: "+ bestEpoch);

        System.out.println("Starting test.");

        int nMaxReward = 25;
        int testIteration = 0;
        int rewardSum = 0;

        boolean done = false;
        ObservationDTO observation = env.reset();

        while(!done && rewardSum < nMaxReward) {
            int action = agent.step(observation.state);
            ObservationDTO newObservation = env.step(action);
            rewardSum += newObservation.reward;
            done = newObservation.done;
            observation = newObservation;
            testIteration += 1;
        }
        System.out.println("Iterations: "+testIteration);
        System.out.println("Aggregate reward: "+rewardSum);
    }

    public static void main(String[] args) {
        FlappyEvaluator flappyEvaluator = new FlappyEvaluator();
        flappyEvaluator.run();
        FlappyGui gui = new FlappyGui(flappyEvaluator.agent);
        //FlappyGui gui = new FlappyGui(); // Empty constructor for playing manually
        gui.setVisible(true);
    }
}
