import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class FlappyGui extends JFrame {

    private class FlappyKeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    jump = true;
                }
            }
            return false;
        }
    }

    FlappyEnv env;
    public int envScaler = 10;
    int updateInterval = 100;
    boolean jump;
    FlappyAgent agent;
    ObservationDTO observation;
    FlappyCanvas panel;
    Timer timer;
    int rewardCounter = 0;

    public FlappyGui() {
        this(null);
    }

    public FlappyGui(FlappyAgent _agent) {
        super("Flappy Birds");
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        env = new FlappyEnv();
        agent = _agent;
        observation = env.reset();

        setResizable(false);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        JButton b0 = new JButton("Action 0");
        b0.setPreferredSize(new Dimension(100,50));
        b0.addActionListener(e -> sendAction(0));
        b0.setFocusable(false);
        add(b0, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        JButton b1 = new JButton("Action 1");
        b1.setPreferredSize(new Dimension(100,50));
        b1.addActionListener(e -> sendAction(1));
        b1.setFocusable(false);
        add(b1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        JButton br = new JButton("Reset");
        br.setPreferredSize(new Dimension(100,50));
        br.addActionListener(e -> reset());
        br.setFocusable(false);
        add(br, c);

        c.fill = GridBagConstraints.RELATIVE;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 40;
        panel = new FlappyCanvas(this);
        panel.setPreferredSize(new Dimension(FlappyEnv.mapSizeX * envScaler, FlappyEnv.mapSizeY * envScaler));
        add(panel, c);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new FlappyKeyDispatcher());

        pack();
        refreshScreen();
    }

    void sendAction(int action) {
        observation = env.step(action);
        rewardCounter += observation.reward;
        panel.repaint();
    }
    void reset() {
        observation = env.reset();
        rewardCounter = 0;
        panel.repaint();
    }
    public void refreshScreen() {
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int action = 0;
                if(agent != null) {
                    action = agent.step(observation.state);
                }
                else {
                    if(jump) {
                        action = 1;
                        jump = false;
                    }
                    else {
                        action = 0;
                    }
                }
                sendAction(action);
            }
        });
        timer.setRepeats(true);
        timer.setDelay(updateInterval);
        timer.start();
    }

    InfoDTO getInformation() {
        return this.observation.info;
    }
    int getReward() {
        return rewardCounter;
    }
}
