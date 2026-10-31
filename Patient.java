public class Patient implements Runnable {

    private int patientId;
    private WaitingRoom waitingRoom;

    public Patient(int patientId, WaitingRoom waitingRoom) {
        this.patientId = patientId;
        this.waitingRoom = waitingRoom;
    }

    @Override
    public void run() {
        arrive();

        try {
            enterWaitingRoom();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void arrive() {
        System.out.println("Patient " + patientId + " arrived at the hospital.");
    }

    public void enterWaitingRoom() throws InterruptedException {
        System.out.println("Patient " + patientId + " is waiting.");

        waitingRoom.put(this);

        System.out.println("Patient " + patientId + " entered the waiting room.");
    }

    public void leaveHospital() {
        System.out.println("Patient " + patientId + " left the hospital.");
    }


    
    public int getPatientId() {
        return patientId;
    }
}
