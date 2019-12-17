package aoc17;

import java.util.Objects;

public class Command {

    private Direction direction;
    private int steps;
    private Integer group;

    public Command(Direction direction, int steps) {
        this.direction = direction;
        this.steps = steps;
    }

    public void increaseSteps() {
        this.steps++;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(final Integer group) {
        this.group = group;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Command command = (Command) o;
        return steps == command.steps && direction == command.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, steps);
    }

    public String toString() {
        return direction + "," + steps;
    }

    public static enum Direction {
        L, R;
    }
}
