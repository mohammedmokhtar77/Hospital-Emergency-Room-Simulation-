public class TreatmentRoom {
    private final int roomId;
    private boolean available;

    public TreatmentRoom(int roomId) {
        this.roomId = roomId;
        this.available = true;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isAvailable() {
        return available;
    }

    void setAvailable(boolean available) {
        this.available = available;
    }
}