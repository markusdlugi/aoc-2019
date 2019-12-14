package aoc14;

import java.util.Objects;

public class Chemical {

    private long amount;
    private String name;

    public Chemical(long amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return amount + " " + name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Chemical chemical = (Chemical) o;
        return Objects.equals(name, chemical.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
