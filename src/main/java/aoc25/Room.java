package aoc25;

import java.util.List;
import java.util.Objects;

public class Room {

    private List<Direction> path;

    public Room(List<Direction> path) {
        this.path = path;
    }

    public List<Direction> getPath() {
        return path;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Room room = (Room) o;
        return Objects.equals(path, room.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return Objects.toString(path);
    }
}
