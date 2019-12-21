package aoc21;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import aoc19.IntcodeComputer;

public class Springdroid {

    private static long[] opcodeArray = null;

    private static final String SPRING_SCRIPT_A = "" +
            "NOT B T\n" +
            "NOT J J\n" +
            "AND D J\n" +
            "AND T J\n" +

            "NOT C T\n" +
            "AND D T\n" +
            "OR T J\n" +

            "NOT A T\n" +
            "OR T J\n" +

            "WALK\n";

    private static final String SPRING_SCRIPT_B = "" +
            "NOT B T\n" +
            "NOT J J\n" +
            "AND D J\n" +
            "AND T J\n" +
            "AND H J\n" +

            "NOT C T\n" +
            "AND D T\n" +
            "OR T J\n" +
            "AND H J\n" +

            "NOT A T\n" +
            "OR T J\n" +

            "RUN\n";

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Springdroid.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        int partA = runSpringScript(SPRING_SCRIPT_A);
        System.out.println("Part A: " + partA);

        int partB = runSpringScript(SPRING_SCRIPT_B);
        System.out.println("Part B: " + partB);
    }

    private static int runSpringScript(String script) {
        IntcodeComputer computer = new IntcodeComputer(opcodeArray, true);

        List<Integer> inputList = new ArrayList<>();
        for(Character character : script.toCharArray()) {
            inputList.add((int) character);
        }
        computer.setInputList(inputList);
        int output = 0;
        while(!computer.isHalt()) {
            output = (int) computer.run();
            if(output <= 255) {
                char outputChar = (char) output;
                System.out.print(outputChar);
            }
        }
        return output;
    }
}
