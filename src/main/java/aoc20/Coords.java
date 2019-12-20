package aoc20;

import java.util.Objects;

public class Coords {

    private int x;
    private int y;
    private int distance;
    private int level;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coords(int x, int y, int distance, int level) {
        this(x, y);
        this.distance = distance;
        this.level = level;
    }

    public Coords(Coords other) {
        this.x = other.x;
        this.y = other.y;
        this.distance = other.distance;
        this.level = other.level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        final Coords that = (Coords) o;
        return x == that.x && y == that.y && level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, level);
    }
}
