import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Patient {
    private long id;
    private int age;
    private SicknessType type;

    private Department department;

    public Patient(int age, SicknessType type) {
        this.age = age;
        this.type = type;
    }

    public void setId(long id){ this.id = id; }

    public long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public SicknessType getType() {
        return type;
    }

    public void hospitalized(Department at){
        department = at;
        department.placePatient(this);
        TimerTask task = new TimerTask() {
            public void run() {
                department.patientHealed(id);
                System.out.println("Patient healed.");
            }
        };

        Timer timer = new Timer("Timer" + id + type);
        int delay = 20  + new Random().nextInt(age / 3);
        timer.schedule(task, delay);
    }
}
