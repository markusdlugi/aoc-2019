package aoc20;

import java.util.Arrays;
import java.util.Objects;

public class Portal {

    private String name;
    private Coords outerPosition;
    private Coords innerPosition;
    private Coords outerWarpPosition;
    private Coords innerWarpPosition;

    public Portal(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Coords getOuterPosition() {
        return outerPosition;
    }

    public void setOuterPosition(final Coords outerPosition) {
        this.outerPosition = outerPosition;
    }

    public Coords getInnerPosition() {
        return innerPosition;
    }

    public void setInnerPosition(final Coords innerPosition) {
        this.innerPosition = innerPosition;
    }

    public Coords getOuterWarpPosition() {
        return outerWarpPosition;
    }

    public void setOuterWarpPosition(final Coords outerWarpPosition) {
        this.outerWarpPosition = outerWarpPosition;
    }

    public Coords getInnerWarpPosition() {
        return innerWarpPosition;
    }

    public void setInnerWarpPosition(final Coords innerWarpPosition) {
        this.innerWarpPosition = innerWarpPosition;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Portal portal = (Portal) o;
        return Objects.equals(name, portal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
