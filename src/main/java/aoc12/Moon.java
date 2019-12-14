package aoc12;

import java.util.Objects;

public class Moon {

    private Integer x;
    private Integer y;
    private Integer z;

    private Integer xVel = 0;
    private Integer yVel = 0;
    private Integer zVel = 0;

    public Moon(Integer x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Integer getDimension(int dimension) {
        switch (dimension) {
        case 0:
            return x;
        case 1:
            return y;
        case 2:
            return z;
        default:
            throw new IllegalArgumentException();
        }
    }

    public void changeVel(final Integer velChange, final int dimension) {
        switch (dimension) {
        case 0:
            this.xVel += velChange;
            break;
        case 1:
            this.yVel += velChange;
            break;
        case 2:
            this.zVel += velChange;
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    public void applyVelocity() {
        x += xVel;
        y += yVel;
        z += zVel;
    }

    public int getPotentialEnergy() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public int getKineticEnergy() {
        return Math.abs(xVel) + Math.abs(yVel) + Math.abs(zVel);
    }

    public int getTotalEnergy() {
        return getPotentialEnergy() * getKineticEnergy();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Moon moon = (Moon) o;
        return x == moon.x && y == moon.y && z == moon.z && xVel == moon.xVel && yVel == moon.yVel && zVel == moon.zVel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, xVel, yVel, zVel);
    }

    @Override
    public String toString() {
        return "pos=<" + "x=" + x + ", y=" + y + ", z=" + z + ">, vel=<x=" + xVel + ", y=" + yVel + ", z=" + zVel + '>';
    }
}
