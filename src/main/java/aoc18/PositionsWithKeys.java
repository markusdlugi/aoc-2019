package aoc18;

import java.util.Objects;
import java.util.Set;

public class PositionsWithKeys {

    private Set<LabyrinthObject> positions;
    private Set<Key> acquiredKeys;

    public PositionsWithKeys(final Set<LabyrinthObject> positions, final Set<Key> acquiredKeys) {
        this.positions = positions;
        this.acquiredKeys = acquiredKeys;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PositionsWithKeys that = (PositionsWithKeys) o;
        return Objects.equals(positions, that.positions) && Objects.equals(acquiredKeys, that.acquiredKeys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positions, acquiredKeys);
    }
}
