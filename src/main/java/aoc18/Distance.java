package aoc18;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Distance {

    private int distance;
    private Set<Key> requiredKeys;

    public Distance() {
        this.requiredKeys = new HashSet<>();
    }

    public Distance(final int distance, final Set<Key> requiredKeys) {
        this.distance = distance;
        this.requiredKeys = requiredKeys;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Key> getRequiredKeys() {
        return requiredKeys;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Distance that = (Distance) o;
        return Objects.equals(requiredKeys, that.requiredKeys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredKeys);
    }
}
