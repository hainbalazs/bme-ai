import javax.xml.stream.Location;
import java.util.HashSet;

public class Department implements HospitalElement {
    private int maxCapacity;
    private SicknessType label;
    private HashSet<Long> patientsInCare;
    private Location location;

    public Department(SicknessType label){
        this.label = label;
        maxCapacity = 20;
        patientsInCare = new HashSet<>();
    }

    public SicknessType getDepartmentType(){
        return label;
    }

    public void patientHealed(long id){
        patientsInCare.remove(id);
    }

    public int getCurrentCapacity(){
        return maxCapacity - patientsInCare.size();
    }

    @Override
    public Patient takePatient() {
        /* Empty, should not be used */
        return null;
    }

    @Override
    public void placePatient(Patient patient) {
        patientsInCare.add(patient.getId());
    }
}
