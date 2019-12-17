package aoc2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Opcodes {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Opcodes.class.getResource("input.txt").toURI()));
        String[] opcodeArrayString = lines.get(0).split(",");
        int[] opcodeArray = new int[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Integer.parseInt(opcodeArrayString[i]);
        }

        // Part A
        //partA(opcodeArray);

        // Part B
        partB(opcodeArray);
    }

    public static void partA(int[] opcodeArray) {
        // Before running
        opcodeArray[1] = 12;
        opcodeArray[2] = 2;

        IntcodeComputer computer = new IntcodeComputer(opcodeArray, 0);

        System.out.println("Final value: " + computer.run());
    }

    public static void partB(int[] opcodeArray) {
        for (int i = 0; i < 99; i++) {
            for (int j = 0; j < 99; j++) {
                opcodeArray[1] = i;
                opcodeArray[2] = j;

                IntcodeComputer computer = new IntcodeComputer(opcodeArray, 0);
                int result = computer.run();
                System.out.println("Result for " + opcodeArray[1] + " and " + opcodeArray[2] + ": " + result);
                if (result == 19690720) {
                    System.out.println("DONE!");
                    return;
                }
            }
        }
    }

    public static int executeProgram(int[] opcodeArray) {
        for (int i = 0; i < opcodeArray.length / 4; i++) {
            int opcode = opcodeArray[i];
            int pos1 = opcodeArray[i * 4 + 1];
            int pos2 = opcodeArray[i * 4 + 2];
            int pos3 = opcodeArray[i * 4 + 3];

            int arg1 = opcodeArray[pos1];
            int arg2 = opcodeArray[pos2];

            int result = 0;
            switch (opcode) {
            case 1:
                result = arg1 + arg2;
                break;
            case 2:
                result = arg1 * arg2;
                break;
            case 99:
                return opcodeArray[0];
            default:
                throw new IllegalStateException("Unexpected opcode " + opcode);
            }

            opcodeArray[pos3] = result;
        }
        return opcodeArray[0];
    }
}
