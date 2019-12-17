package aoc5;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DiagnosticProgram {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(DiagnosticProgram.class.getResource("input.txt").toURI()));
        String[] opcodeArrayString = lines.get(0).split(",");
        int[] opcodeArray = new int[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Integer.parseInt(opcodeArrayString[i]);
        }

        IntcodeComputer computer = new IntcodeComputer(opcodeArray, 0);
        computer.run();
    }
}
