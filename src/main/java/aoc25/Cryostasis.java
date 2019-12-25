package aoc25;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Cryostasis {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Cryostasis.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        long [] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        Queue<Long> inputQueue = new LinkedList<>();
        Queue<Long> outputQueue = new LinkedList<>();
        IntcodeComputer computer = new IntcodeComputer(opcodeArray, inputQueue, outputQueue);
        while(!computer.isHalt()) {
            int output = (int) computer.run();
            if(output <= 255) {
                char outputChar = (char) output;
                System.out.print(outputChar);
            }
        }
    }
}
