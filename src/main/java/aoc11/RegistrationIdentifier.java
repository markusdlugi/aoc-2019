package aoc11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationIdentifier {

    private static final String PATH = "C:/dev/workspace/aoc/src/main/resources/aoc11/";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH + "input.txt"));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        Map<Coords, Byte> panels = new HashMap<>();
        Coords robotPosition = new Coords(0, 0);
        IntcodeComputer computer = new IntcodeComputer(opcodeArray);

        // Start with first white panel
        panels.put(new Coords(0, 0), (byte) 1);

        while (!computer.isHalt()) {
            // Get current panel color as input
            byte currentColor = 0;
            if (panels.get(robotPosition) != null) {
                currentColor = (byte) panels.get(robotPosition);
            }
            computer.setInput1((int) currentColor);

            // Run robot twice for color + direction
            byte color = (byte) computer.run();
            byte turn = (byte) computer.run();

            // Color panel
            panels.put(new Coords(robotPosition.getX(), robotPosition.getY()), color);

            // Turn robot in new direction
            byte direction = robotPosition.getDirection();
            if (turn == 0) {
                direction -= 1;
            } else {
                direction += 1;
            }
            // Direction should be between 0 and 3 (u, r, d, l)
            if (direction < 0) {
                direction = 3;
            } else if (direction > 3) {
                direction = 0;
            }
            robotPosition.setDirection(direction);

            // Move robot in direction it is facing
            switch (direction) {
            case 0:
                robotPosition.setY(robotPosition.getY() + 1);
                break;
            case 1:
                robotPosition.setX(robotPosition.getX() + 1);
                break;
            case 2:
                robotPosition.setY(robotPosition.getY() - 1);
                break;
            case 3:
                robotPosition.setX(robotPosition.getX() - 1);
                break;
            default:
                throw new IllegalStateException();
            }
        }

        System.out.println("Number of panels painted: " + panels.size());

        // Find max coords for printing
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Coords panel : panels.keySet()) {
            minX = Integer.min(minX, panel.getX());
            minY = Integer.min(minY, panel.getY());
            maxX = Integer.max(maxX, panel.getX());
            maxY = Integer.max(maxY, panel.getY());
        }

        // Print painted panels
        for (int y = maxY + 1; y >= minY - 1; y--) {
            for (int x = minX - 1; x <= maxX + 1; x++) {
                Coords coord = new Coords(x, y);
                byte color = (panels.containsKey(coord)) ? panels.get(coord) : 0;
                if (color == 1) {
                    System.out.print("\u2588");
                } else {
                    System.out.print("\u2591");
                }
            }
            System.out.println();
        }
    }
}
