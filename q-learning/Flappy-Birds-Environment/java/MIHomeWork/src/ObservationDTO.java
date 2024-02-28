public class ObservationDTO {
    public FlappyAgent.StateDTO state;
    public int reward;
    public boolean done;
    public InfoDTO info;

    public ObservationDTO() {
    }

    public ObservationDTO(FlappyAgent.StateDTO state, int reward, boolean done, InfoDTO info) {
        this.state = state;
        this.reward = reward;
        this.done = done;
        this.info = info;
    }

    @Override
    public String toString() {
        return "ObservationDTO{" +
                "state=" + state +
                ", reward=" + reward +
                ", done=" + done +
                ", info=" + info +
                '}';
    }
}
