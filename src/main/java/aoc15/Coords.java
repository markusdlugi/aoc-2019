package aoc15;

import java.util.Objects;

public class Coords {

    private int x;
    private int y;
    private int distance;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
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
        if (o == null) {
            return false;
        }
        final Coords that = (Coords) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + "x=" + x + ", y=" + y + ", distance="+distance+")";
    }
}
