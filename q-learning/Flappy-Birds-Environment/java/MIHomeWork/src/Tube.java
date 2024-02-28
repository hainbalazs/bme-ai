public class Tube {
    static int gapSize = 20;
    static int tubeSpeed = -2;
    static int width = 10;
    static int[] heightRange = {5, 15};

    public int pos;
    public int height;
    boolean scored;

    Tube(int pos, int height) {
        this.pos = pos;
        this.height = height;
        this.scored = false;
    }

    void step() {
        this.pos+= tubeSpeed;
    }

    int distanceToBird(Bird bird) {
        return this.pos - bird.posX;
    }

    @Override
    public String toString() {
        return "Tube{" +
                "pos=" + pos +
                ", height=" + height +
                ", scored=" + scored +
                '}';
    }
}
