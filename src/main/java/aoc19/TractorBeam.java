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
        int lastFirstX = 0;
        for(int y = 1000; y < 10000; y++) {
            System.out.println("Line " + y);
            boolean firstX = true;
            for(int x = 0; x < 10000; x++) {
                // Jump to last position where beam started
                if(x < lastFirstX) {
                    x = lastFirstX;
                    continue;
                }
                int output = getBeam(x, y);
                // If beam just started, jump a little since box cannot start at the beginning of 1s
                if (output == 1 && firstX) {
                    firstX = false;
                    lastFirstX = x;
                    x += 50;
                    continue;
                }
                // Check if beam is large enough for box now
                if(output == 1) {
                    int xStart = x - (SHIP_SIZE - 1);
                    int yStart = y;
                    int yEnd = y + (SHIP_SIZE - 1);
                    if(getBeam(xStart, yStart) == 1 && getBeam(xStart,yEnd) == 1) {
                        System.out.println("Found point: " + xStart + "," + yStart + " -> " + (xStart * 10000 + yStart));
                        return;
                    }
                }
                // At the end of the beam, try next line
                else if(!firstX) {
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
