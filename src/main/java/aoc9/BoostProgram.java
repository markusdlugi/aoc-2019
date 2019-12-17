package aoc9;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BoostProgram {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(BoostProgram.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        IntcodeComputer computer = new IntcodeComputer(opcodeArray);
        computer.run();
    }
}
