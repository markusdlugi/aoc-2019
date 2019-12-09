package aoc5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DiagnosticProgram {

    private static final String PATH = "C:/dev/workspace/aoc/src/main/resources/aoc5/";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH + "input.txt"));
        String[] opcodeArrayString = lines.get(0).split(",");
        int[] opcodeArray = new int[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Integer.parseInt(opcodeArrayString[i]);
        }

        IntcodeComputer computer = new IntcodeComputer(opcodeArray, 0);
        computer.run();
    }
}
