import java.util.List;

public class InfoDTO {
    public int birdX;
    public int birdY;
    public int birdSize;
    public List<Tube> tubes;
    public int tubeWidth;
    public int tubeGapsize;

    public InfoDTO() {
    }

    public InfoDTO(int birdX, int birdY, int birdSize, List<Tube> tubes, int tubeWidth, int tubeGapsize) {
        this.birdX = birdX;
        this.birdY = birdY;
        this.birdSize = birdSize;
        this.tubes = tubes;
        this.tubeWidth = tubeWidth;
        this.tubeGapsize = tubeGapsize;
    }

    @Override
    public String toString() {
        return "InfoDTO{" +
                "birdX=" + birdX +
                ", birdY=" + birdY +
                ", birdSize=" + birdSize +
                ", tubes=" + tubes +
                ", tubeWidth=" + tubeWidth +
                ", tubeGapsize=" + tubeGapsize +
                '}';
    }
}
