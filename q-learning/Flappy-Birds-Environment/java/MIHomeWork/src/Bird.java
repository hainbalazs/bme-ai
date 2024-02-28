import java.util.List;

public class Bird {
    static int size = 3;
    static int maxSpeed = 25;

    int posX, posY;
    int speed;

    Bird(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.speed = 0;
    }

    void step(int force, boolean keep) {
        if(keep) {
            this.speed += force;
        }
        else {
            this.speed = force;
        }
        if(this.speed > maxSpeed) {
            this.speed = maxSpeed;
        }
        this.posY += speed;
    }

    boolean checkCollide(List<Tube> tubes) {
        if(this.posY < 0 || this.posY + size > FlappyEnv.mapSizeY) return true;
        for(Tube tube : tubes) {
            if(this.posX + size > tube.pos && this.posX < tube.pos + Tube.width)
                if(this.posY < tube.height || this.posY + size > tube.height + Tube.gapSize)
                    return true;
        }
        return false;
    }
}
