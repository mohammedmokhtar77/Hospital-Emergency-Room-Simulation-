public class Doctor implements Runnable {

    private final int doctorId;
    private final WaitingRoom waitingRoom;
    private final TreatmentRoomPool treatmentRoomPool;

    private Patient currentPatient;

    private static final long ROOM_TIMEOUT_MS = 3000;

    public Doctor(
            int doctorId,
            WaitingRoom waitingRoom,
            TreatmentRoomPool treatmentRoomPool) {

        this.doctorId = doctorId;
        this.waitingRoom = waitingRoom;
        this.treatmentRoomPool = treatmentRoomPool;
    }

    @Override
    public void run() {
        System.out.println("Doctor " + doctorId + " started working.");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                currentPatient = waitingRoom.take();

                if (currentPatient != null) {
                    treatPatient(currentPatient);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println( "Doctor " + doctorId + " stopped working.");
    }

    public void treatPatient(Patient patient) throws InterruptedException {

        TreatmentRoom room = treatmentRoomPool.acquireRoom(ROOM_TIMEOUT_MS);
        if (room == null) {
            System.out.println("Doctor " + doctorId + " could not get a treatment room. " + "Requeuing patient " + getPatientId(patient));
            returnPatient(patient);
            return;
        }

        try {
            currentPatient = patient;
            System.out.println("Doctor " + doctorId+ " started treating patient "+ getPatientId(patient)+ " in room " + room.getRoomId());
            simulateTreatment();
            System.out.println("Doctor " + doctorId+ " finished treating patient "+ getPatientId(patient));
            patient.leaveHospital();

        } finally {
            treatmentRoomPool.releaseRoom(room);
            currentPatient = null;
        }
    }

    public void simulateTreatment()
            throws InterruptedException {

        Thread.sleep(2000);
    }

    public void returnPatient(Patient patient)
            throws InterruptedException {

        waitingRoom.put(patient);
    }

    private int getPatientId(Patient patient) {
        return patient.getPatientId();
    }
}
