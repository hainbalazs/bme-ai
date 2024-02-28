import javax.swing.*;
import java.awt.*;

public class FlappyCanvas extends JPanel {
    FlappyGui gui;

    FlappyCanvas(FlappyGui gui) {
        this.gui = gui;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        InfoDTO info = gui.getInformation();
        int envScaler = gui.envScaler;
        g.setColor(Color.RED);
        g.fillRect(
                info.birdX*envScaler,
                info.birdY*envScaler,
                (info.birdSize)*envScaler,
                (info.birdSize)*envScaler
        );

        g.setColor(Color.GREEN);
        for(Tube t: info.tubes) {
            g.fillRect(
                    t.pos * envScaler,
                    (t.height + info.tubeGapsize) * envScaler,
                    info.tubeWidth * envScaler,
                    (FlappyEnv.mapSizeY - (t.height + info.tubeGapsize)) * envScaler
            );
            g.fillRect(
                    t.pos * envScaler,
                    0,
                    info.tubeWidth * envScaler,
                    t.height * envScaler
            );
        }
        int reward = gui.getReward();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.drawString(Integer.toString(reward), (FlappyEnv.mapSizeX - 5) * envScaler, 40);
    }
}
