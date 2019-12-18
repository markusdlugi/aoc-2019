package aoc18;

public class StartingPoint extends LabyrinthObject {

    public StartingPoint(final String name, final int x, final int y) {
        super(name, x, y);
    }

    public StartingPoint(final StartingPoint other) {
        super(other);
    }
}
