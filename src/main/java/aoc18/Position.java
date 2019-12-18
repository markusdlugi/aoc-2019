package aoc18;

public class Position {

    private int x;
    private int y;
    private Distance distance;

    public Position(final int x, final int y, final Distance distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Distance getDistance() {
        return distance;
    }
}
