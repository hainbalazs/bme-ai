import jason.environment.grid.Location;


public class Carrier {
    public int id;
    private Patient takenPatient = null;
    public Location currentPosition;
    private HospitalElement destination = null;
    private int takenSteps;
    private HospitalEnvironment environment;
    static private int speed = 200;

    public Carrier(int id, Location pos, HospitalEnvironment env){
        this.id = id;
        this.currentPosition = pos;
        this.environment = env;
    }

    public void takePatient(){
        HospitalElement from = environment.getHEfromPosition(currentPosition);
        takenPatient = from.takePatient();
    }

    public void dropPatient(){
        Department to = (Department) environment.getHEfromPosition(currentPosition);
        takenPatient.hospitalized(to);
        takenPatient = null;
    }

    public int getId(){return id;}

    public long getTakenId(){
        if(takenPatient != null){
            return takenPatient.getId();
        }
        return -1L;
    }


}
