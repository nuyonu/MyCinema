package App.Database.Utils;

import java.time.LocalTime;

public class ScreeningRoom {
    private final int room;
    private final LocalTime time;

    public ScreeningRoom(int room, LocalTime time) {
        this.room = room;
        this.time = time;
    }

    public int getRoom() {
        return room;
    }

    public LocalTime getTime() {
        return time;
    }
}
