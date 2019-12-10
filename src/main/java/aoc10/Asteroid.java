package aoc10;

import java.util.Comparator;
import java.util.Objects;

public class Asteroid {

    private int x;
    private int y;
    private double angle;
    private double distance;

    public Asteroid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(final double angle) {
        this.angle = angle;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(final double distance) {
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
        final Asteroid that = (Asteroid) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + "x=" + x + ", y=" + y + ')';
    }

    public static class AsteroidComparator implements Comparator<Asteroid> {

        @Override
        public int compare(final Asteroid o1, final Asteroid o2) {
            double angleDiff = o1.getAngle() - o2.getAngle();
            if(Math.abs(angleDiff) < 0.00001) {
                double distanceDiff = o1.getDistance() - o2.getDistance();
                return (int) Math.signum(distanceDiff);
            }
            return (int) Math.signum(angleDiff);
        }
    }
}
