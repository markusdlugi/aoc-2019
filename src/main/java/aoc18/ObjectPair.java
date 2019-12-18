package aoc18;

import java.util.Objects;

public class ObjectPair {

    private LabyrinthObject objA;
    private LabyrinthObject objB;

    public ObjectPair(final LabyrinthObject objA, final LabyrinthObject objB) {
        this.objA = objA;
        this.objB = objB;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ObjectPair that = (ObjectPair) o;
        return (Objects.equals(objA, that.objA) && Objects.equals(objB, that.objB)) || (Objects.equals(objA, that.objB)
                && Objects.equals(objB, that.objA));
    }

    @Override
    public int hashCode() {
        return Objects.hash(objA.hashCode() + objB.hashCode());
    }
}
