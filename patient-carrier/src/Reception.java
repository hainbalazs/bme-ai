import java.util.LinkedList;
import java.util.Random;

public class Reception implements HospitalElement {
    private LinkedList<Patient> waitingPatients;
    private HospitalEnvironment env;
    private long idCounter;

    public Reception(int nInitPatients, HospitalEnvironment environment){
        env = environment;
        waitingPatients = new LinkedList<>();
        // generate patients

        int nSicknesses = SicknessType.values().length;
        for(int i = 0; i < nInitPatients; i++){
            Random r = new Random();
            Patient patient = new Patient(10 + r.nextInt(90), SicknessType.values()[r.nextInt(nSicknesses)]);
            patient.setId(i);
            waitingPatients.add(patient);
        }
        idCounter = nInitPatients;

        // advertising first patient
        env.advertisePatient(waitingPatients.peekFirst());
    }
    @Override
    public Patient takePatient() {
        if(waitingPatients.isEmpty())
            return null;

        Patient takenPatient = waitingPatients.removeFirst();
        env.advertisePatient(waitingPatients.peekFirst());
        return takenPatient;
    }

    @Override
    public void placePatient(Patient patient) {
        patient.setId(idCounter++);
        boolean queueWasEmpty = waitingPatients.isEmpty();
        waitingPatients.add(patient);
        if(queueWasEmpty)
            env.advertisePatient(waitingPatients.peekFirst());
    }

}
