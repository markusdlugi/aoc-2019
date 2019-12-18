package aoc18;

public class Key extends LabyrinthObject {

    public Key(final String name, final int x, final int y) {
        super(name, x, y);
    }

    public Key(final Key other) {
        super(other);
    }
}
