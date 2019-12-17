package aoc16;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import aoc13.IntcodeComputer;
import aoc15.Coords;

public class FlawedFrequencyTransmission {

    private static final int[] PHASE_PATTERN = new int[] {0, 1, 0, -1};
    private static final int PHASES = 100;
    private static final int REPETITIONS = 10000;

    private static int inputSize;

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(FlawedFrequencyTransmission.class.getResource("input.txt").toURI()));
        List<Integer> digits = Arrays.stream(lines.get(0).split("")).map(Integer::parseInt).collect(toList());

        inputSize = digits.size();

        partA(digits);
        partB(digits);
    }

    private static void partA(List<Integer> digits) {
        for (int p = 0; p < PHASES; p++) {
            List<Integer> outputList = new ArrayList<>(inputSize);
            // For each output number
            for (int o = 0; o < inputSize; o++) {
                int number = 0;
                List<Integer> phaseList = new ArrayList<>(inputSize);
                // Build phase list
                int currentPhaseIndex = 0;
                for(int i = 0; i < inputSize; i++) {
                    if((i+1) % (o+1) == 0) {
                        currentPhaseIndex++;
                    }
                    phaseList.add(PHASE_PATTERN[currentPhaseIndex % 4]);
                }
                // For each input digit
                for (int i = 0; i < inputSize; i++) {
                    int phaseValue = phaseList.get((i) % inputSize);
                    number += digits.get(i) * phaseValue;
                }
                int finalDigit = Math.abs(number) % 10;
                outputList.add(finalDigit);
            }
            digits = outputList;
        }

        String output = digits.stream().map(Object::toString).collect(joining());
        System.out.println(output.substring(0, 8));
    }

    private static void partB(List<Integer> digits) {
        // Message offset
        String offsetString = "";
        for(int i = 0; i < 7; i++) {
            offsetString += digits.get(i);
        }
        int offset = Integer.parseInt(offsetString);

        int repeatedInputSize = inputSize * REPETITIONS;
        int actualSize = repeatedInputSize - offset;

        Integer[] digitsArray = new Integer[actualSize];

        // Repeat input
        List<Integer> repeatedInput = new ArrayList<>(actualSize);
        for(int i = 0; i < actualSize; i++) {
            digitsArray[i] = digits.get((i + offset) % inputSize);
        }

        Integer[][] phaseArray = new Integer[PHASES + 1][];
        phaseArray[0] = digitsArray;
        for(int i = 1; i < PHASES + 1; i++) {
            phaseArray[i] = new Integer[actualSize];
        }

        for(int phase = 0; phase < PHASES; phase ++) {
            System.out.println("Calculating phase " + phase);
            int sum = 0;
            for(int i = actualSize - 1; i >= 0; i--) {
                sum += phaseArray[phase][i];
                sum = sum % 10;
                phaseArray[phase + 1][i] = sum;
            }
        }

        for(int i = 0; i < 8; i++) {
            System.out.print(phaseArray[100][i]);
        }
    }
}
