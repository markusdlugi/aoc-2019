package aoc18;

import java.util.Objects;

public class LabyrinthObject {

    private String name;
    private int x;
    private int y;
    private int distance;

    public LabyrinthObject(final String name, final int x, final int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public LabyrinthObject(final LabyrinthObject other) {
        this.name = other.name;
        this.x = other.x;
        this.y = other.y;
        this.distance = other.distance;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(final int distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LabyrinthObject that = (LabyrinthObject) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return name;
    }
}
