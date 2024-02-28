import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.*;

public class HospitalView extends GridWorldView {
    private HospitalEnvironment env;
    private JPanel sidepanel;

    public HospitalView(GridWorldModel model, HospitalEnvironment env) {
        super(model, "Hospital Logistics", 600);
        this.model = (HospitalModel) model;
        this.env = env;

        sidepanel = new JPanel();
        BoxLayout layout = new BoxLayout(sidepanel, BoxLayout.PAGE_AXIS);
        sidepanel.setLayout(layout);
        updateSidePanel();

        setVisible(true);
        repaint();
    }

    public void updateSidePanel(){

        BorderLayout layout = (BorderLayout) getContentPane().getLayout();
        Component toRemove = layout.getLayoutComponent(BorderLayout.WEST);
        if (toRemove != null)
            getContentPane().remove(toRemove);

        sidepanel.removeAll();
        BoxLayout layoutmgr = new BoxLayout(sidepanel, BoxLayout.PAGE_AXIS);
        sidepanel.setLayout(layoutmgr);
        sidepanel.add(Box.createRigidArea(new Dimension(0,15)));
        sidepanel.add(new JLabel("Department capacities:"));
        sidepanel.add(Box.createRigidArea(new Dimension(0,5)));
        JPanel capacities = updateCapacities();
        sidepanel.add(capacities);

        sidepanel.add(Box.createRigidArea(new Dimension(0,15)));
        sidepanel.add(new JLabel("Status of Carrier Agents:"));
        sidepanel.add(Box.createRigidArea(new Dimension(0,5)));
        JPanel agentStatus = updateStatus();
        sidepanel.add(agentStatus);


        getContentPane().add(sidepanel, BorderLayout.WEST);
        agentStatus.setVisible(true);
        capacities.setVisible(true);
        sidepanel.setVisible(true);
        setVisible(true);
    }

    private JPanel updateCapacities(){
        JPanel panel = new JPanel();
        BoxLayout layoutmgr = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layoutmgr);
        HashMap<Department, Location> asd = env.getDepartments();
        if(asd == null) return panel;
        Set<Department> departments = asd.keySet();

        for (Department d : departments){
            panel.add(new JLabel(d.getDepartmentType().toString() + ": " + d.getCurrentCapacity()));
        }
        return panel;
    }

    private JPanel updateStatus(){
        JPanel panel = new JPanel();
        BoxLayout layoutmgr = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layoutmgr);
        ArrayList<Carrier> carriers = env.getCarrierAgents();
        for(Carrier c : carriers){
            String s = "<html><br>Carrier #" + c.getId() + " is currently<br>";
            if(c.getTakenId() != -1L){
                s += " carrying Patient#" + c.getTakenId();
            }
            else{
                s += " waiting";
            }
            s += "</html>";
            panel.add(new JLabel(s));
        }
        return panel;
    }


    @Override
    public void initComponents(int width) {
        super.initComponents(width);
        JComboBox illnessTypes = new JComboBox();
        for (SicknessType type : SicknessType.values()) {
            illnessTypes.addItem(type.name());
        }

        JPanel sp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sidepanel = sp;
        sp.setBorder(BorderFactory.createEtchedBorder());
        sp.add(new JLabel("Add new Patient:"));
        sp.add(new JLabel("Choose the illness type"));
        sp.add(illnessTypes);
        JButton addPatient = new JButton("Add");
        sp.add(addPatient);

        JPanel p = new JPanel();

        JPanel s = new JPanel(new BorderLayout());
        s.add(BorderLayout.WEST, sp);
        s.add(BorderLayout.EAST, p);
        getContentPane().add(BorderLayout.SOUTH, s);

        // Events handling
        addPatient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                env.addPatient(Enum.valueOf(SicknessType.class, (String) illnessTypes.getSelectedItem()));
            }
        } );


    }

    @Override
    public void draw(Graphics g, int x, int y, int object) {
        /*if(object >= 10){
            g.setColor(Color.ORANGE);
            g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            g.setColor(Color.WHITE);
            g.drawString(SicknessType.values()[object - 10].name().substring(0, 2), x * cellSizeW, y * cellSizeH);
        }
        else if(object == 1){
            g.setColor(Color.RED);
            g.fillRect(x * cellSizeW - 5*cellSizeW, y * cellSizeH, 10*cellSizeW, cellSizeH);
            g.setColor(Color.WHITE);
            g.drawString("R", x * cellSizeW, y * cellSizeH);
        }*/

        /*if(object == 1){
            g.setColor(Color.RED);
            g.fillRect(x * cellSizeW - 5*cellSizeW, y * cellSizeH, 10*cellSizeW, cellSizeH);
        }*/

        HashMap<Department, Location> departments = env.getDepartments();
        int i = 0;
        for(Map.Entry<Department, Location> p : departments.entrySet()){
            x = p.getValue().x;
            y = p.getValue().y;
            g.setColor(Color.ORANGE);
            g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            g.setColor(Color.BLUE);
            g.drawString(p.getKey().getDepartmentType().name().substring(0, 2), x * cellSizeW, y * cellSizeH);
        }

        Location rec = env.getReceptionPosition();
        x = rec.x;
        y = rec.y;
        g.setColor(Color.RED);
        g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        g.setColor(Color.WHITE);
        g.drawString("R", x * cellSizeW, y * cellSizeH);

        //updateSidePanel();
        invokeAgentsDraw(g);
        //drawAgent(g, x, y, Color.BLUE, object);
        /*updating panels...*/
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        ArrayList<Carrier> ac = env.getCarrierAgents();
        if (id < 0 || id >= ac.size()) return;
        g.setColor(Color.BLUE);
        g.fillOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);


        Carrier carrier = ac.get(id);

        if (carrier.getTakenId() != -1L) {
            g.setColor(Color.RED);
            g.fillOval(x * cellSizeW + cellSizeW / 2, y * cellSizeH + cellSizeH / 2, cellSizeW / 2, cellSizeH / 2);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(carrier.getTakenId()), x * cellSizeW + cellSizeW / 2, y * cellSizeH + cellSizeH / 2);
        }
    }

    public static void main(String[] args) throws Exception {
        HospitalEnvironment env = new HospitalEnvironment(/*24, 5*/);
        env.init(new String[] {"24","1"});
    }

    public void setEnv(HospitalEnvironment e){
        this.env = e;
    }

    public void invokeAgentsDraw(Graphics g){
        ArrayList<Carrier> agents = env.getCarrierAgents();

        for(Carrier c : agents){
            int x = c.currentPosition.x;
            int y = c.currentPosition.y;
            g.setColor(Color.BLUE);
            g.fillOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);


            if (c.getTakenId() != -1L) {
                g.setColor(Color.RED);
                g.fillOval(x * cellSizeW + cellSizeW / 2, y * cellSizeH + cellSizeH / 2, cellSizeW / 2, cellSizeH / 2);
                g.setColor(Color.WHITE);
                g.drawString(String.valueOf(c.getTakenId()), x * cellSizeW + cellSizeW / 2, y * cellSizeH + cellSizeH / 2);
            }
        }
    }
}
