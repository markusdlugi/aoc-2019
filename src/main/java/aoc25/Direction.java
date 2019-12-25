package aoc25;

public enum Direction {
    north(0),
    east(1),
    south(2),
    west(3);

    private int value;

    public static Direction fromValue(int value) {
        return Direction.values()[value];
    }

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public Direction invert() {
        return Direction.fromValue((this.getValue() + 2) % 4);
    }
}
