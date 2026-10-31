import java.util.ArrayList;
import java.util.List;

public class TreatmentRoomPool {
    private final List<TreatmentRoom> rooms = new ArrayList<>();
    private final Semaphore availableRooms;
    private final Semaphore mutex;

    public TreatmentRoomPool(int numberOfRooms) {
        if (numberOfRooms <= 0) {
            throw new IllegalArgumentException("numberOfRooms must be positive");
        }
        for (int i = 1; i <= numberOfRooms; i++) {
            rooms.add(new TreatmentRoom(i));
        }
        availableRooms = new Semaphore(numberOfRooms);
        mutex = new Semaphore(1);
    }

    public TreatmentRoom acquireRoom(long timeoutMillis) throws InterruptedException {

        if (!availableRooms.tryAcquire(timeoutMillis)) {
            return null;
        }
        mutex.acquire();
        try {
            for (TreatmentRoom room : rooms) {
                if (room.isAvailable()) {
                    room.setAvailable(false);
                    return room;
                }
            }
        } finally {
            mutex.release();
        }
        availableRooms.release();
        return null;
    }

    public void releaseRoom(TreatmentRoom room) throws InterruptedException {
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }
        mutex.acquire();
        try {
            if (room.isAvailable()) {
                return; 
            }
            room.setAvailable(true);
        } finally {
            mutex.release();
        }
        availableRooms.release();
    }
}