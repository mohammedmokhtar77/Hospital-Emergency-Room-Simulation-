import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    private final int capacity;
    private final Queue<Patient> patientsQueue;

    private final Semaphore emptySlots;
    private final Semaphore fullSlots;
    private final Semaphore mutex;

    public WaitingRoom(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }

        this.capacity = capacity;
        this.patientsQueue = new LinkedList<>();

        this.emptySlots = new Semaphore(capacity);
        this.fullSlots = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    public void put(Patient patient) throws InterruptedException {
        if (patient == null) {
            throw new IllegalArgumentException("patient cannot be null");
        }

        emptySlots.acquire();

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            emptySlots.release();
            throw e;
        }

        try {
            patientsQueue.offer(patient);
        } finally {
            mutex.release();
        }

        fullSlots.release();
    }

    public Patient take() throws InterruptedException {

        fullSlots.acquire();

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            fullSlots.release();
            throw e;
        }

        Patient patient;

        try {
            patient = patientsQueue.poll();
        } finally {
            mutex.release();
        }

        emptySlots.release();

        return patient;
    }

    public boolean isFull() {
        return patientsQueue.size() >= capacity;
    }
}
