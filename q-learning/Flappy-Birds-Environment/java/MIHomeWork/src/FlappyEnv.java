import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FlappyEnv {
    public static int mapSizeX = 80;
    public static int mapSizeY = 40;
    int tubeInterval = 30;
    int gravity = 1;
    int jumpForce = -5;
    Random rnd;
    Bird bird;
    int stepCounter;
    List<Tube> tubes = new ArrayList<>();
    boolean done;

    public FlappyEnv() {
        reset();
    }

    public int[] getActionSpace() {
        return new int[]{0, 1};
    }
    public int[][] getObservationSpace() {
        return new int[][]{
                {-1, mapSizeY - Bird.size + 1},
                {-Bird.maxSpeed, Bird.maxSpeed},
                {-(Tube.width + 1), mapSizeX},
                {Tube.heightRange[0], Tube.heightRange[1]}
        };
    }
    public int[] getObservationSpaceSize() {
        int[][] observationSpace = getObservationSpace();
        return new int[] {
                observationSpace[0][1] - observationSpace[0][0] +1,
                observationSpace[1][1] - observationSpace[1][0] +1,
                observationSpace[2][1] - observationSpace[2][0] +1,
                observationSpace[3][1] - observationSpace[3][0] +1
        };
    }

    public FlappyAgent.StateDTO state() {
        Tube lastTube = this.tubes.stream().sorted(Comparator.comparingInt(o -> o.pos)).collect(Collectors.toList()).get(0);

        int currentBirdPos;
        if(this.bird.posY < 0) currentBirdPos = -1;
        else if(this.bird.posY > mapSizeY - Bird.size) currentBirdPos = mapSizeY - Bird.size + 1;
        else currentBirdPos = this.bird.posY;

        currentBirdPos+= 1;
        int currentBirdVel = this.bird.speed + Bird.maxSpeed;
        int currentTubeDistance = lastTube.distanceToBird(this.bird) + Tube.width + 1;
        int currentTubeHeight = lastTube.height - Tube.heightRange[0];

        return new FlappyAgent.StateDTO(currentBirdPos, currentBirdVel, currentTubeDistance, currentTubeHeight);
    }

    private void spawnTube() {
        int tubeHeight = this.rnd.nextInt(Tube.heightRange[1] - Tube.heightRange[0]) + Tube.heightRange[0];
        this.tubes.add(new Tube(mapSizeX, tubeHeight));
    }

    private boolean validAction(int action) {
        for(int a : getActionSpace()) {
            if(a == action)
                return true;
        }
        return false;
    }

    public ObservationDTO step(int action) {
        if(!validAction(action)) {
            return null;
        }
        int reward = 0;
        if(!this.done) {
            this.stepCounter += 1;
            if(action == 0) {
                this.bird.step(gravity, true);
            }
            else if(action == 1) {
                this.bird.step(jumpForce, false);
            }

            if(this.stepCounter % tubeInterval == 0) {
                spawnTube();
            }

            for(Tube t: this.tubes) {
                t.step();

                if(t.pos + Tube.width < this.bird.posX && !t.scored) {
                    t.scored = true;
                    reward = 1;
                }
            }
            this.tubes = this.tubes.stream().filter(tube -> !tube.scored).collect(Collectors.toList());

            if(this.bird.checkCollide(this.tubes)) {
                reward = -1;
                this.done = true;
            }
        }

        InfoDTO info = new InfoDTO(this.bird.posX, this.bird.posY, Bird.size, this.tubes, Tube.width, Tube.gapSize);
        ObservationDTO result = new ObservationDTO(state(), reward, done, info);

        return result;
    }

    public ObservationDTO reset() {
        this.rnd = new Random(123);
        this.bird = new Bird(10, mapSizeY / 2);
        this.stepCounter = 0;
        this.tubes.clear();
        spawnTube();
        this.done = false;

        InfoDTO info = new InfoDTO(this.bird.posX, this.bird.posY, Bird.size, this.tubes, Tube.width, Tube.gapSize);
        ObservationDTO result = new ObservationDTO(state(), 0, done, info);
        return result;
    }

}