package aoc19;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TractorBeam {

    private static final int SHIP_SIZE = 100;
    private static long[] opcodeArray;

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(TractorBeam.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        // Part A
        int sum = 0;
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                if (getBeam(x,y) == 1) {
                    sum++;
                }
            }
        }
        System.out.println("Affected points: " + sum);

        // Part B
        // We only start at 1000 because beam is really improbable to be large enough earlier
        int beamStartX = 0;
        for(int y = 1000; y < 10000; y++) {
            System.out.println("Line " + y);
            for(int x = beamStartX; x < 10000; x++) {
                int output = getBeam(x, y);
                if (output == 1) {
                    beamStartX = x;

                    // Check if top right corner of box is also in beam
                    int xRight = x + (SHIP_SIZE - 1);
                    int yTop = y - (SHIP_SIZE - 1);
                    if(getBeam(xRight, yTop) == 1) {
                        System.out.println("Found point: " + x + "," + yTop + " -> " + (x * 10000 + yTop));
                        return;
                    }

                    // Didn't fit, continue with next line
                    break;
                }
            }
        }
    }

    private static int getBeam(int x, int y) {
        IntcodeComputer computer = new IntcodeComputer(opcodeArray, true);
        computer.setInputList(new ArrayList<>(Arrays.asList(x, y)));
        return (int) computer.run();
    }
}
