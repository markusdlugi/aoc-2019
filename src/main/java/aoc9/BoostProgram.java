package aoc9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BoostProgram {

    private static final String PATH = "C:/dev/workspace/aoc/src/main/resources/aoc9/";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH + "input.txt"));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        IntcodeComputer computer = new IntcodeComputer(opcodeArray);
        computer.run();
    }
}
