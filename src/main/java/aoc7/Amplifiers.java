package aoc7;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Amplifiers {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Amplifiers.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        int[] opcodeArray = new int[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Integer.parseInt(opcodeArrayString[i]);
        }

        int highestSignal = Integer.MIN_VALUE;
        String highestPhaseSetting = "";

        Integer[] digits = new Integer[] { 5, 6, 7, 8, 9 };
        Permute<Integer> permutations = new Permute<>(digits);

        while (permutations.hasNext()) {
            Integer[] phaseSetting = permutations.next();
            String phaseSettingStr =
                    "" + phaseSetting[0] + phaseSetting[1] + phaseSetting[2] + phaseSetting[3] + phaseSetting[4];

            // Part A
            /*IntcodeComputer ampA = new IntcodeComputer(opcodeArray, 0, phaseSetting[0], 0);
            ampA.run();
            IntcodeComputer ampB = new IntcodeComputer(opcodeArray, 0, phaseSetting[1], ampA.getOutput());
            ampB.run();
            IntcodeComputer ampC = new IntcodeComputer(opcodeArray, 0, phaseSetting[2], ampB.getOutput());
            ampC.run();
            IntcodeComputer ampD = new IntcodeComputer(opcodeArray, 0, phaseSetting[3], ampC.getOutput());
            ampD.run();
            IntcodeComputer ampE = new IntcodeComputer(opcodeArray, 0, phaseSetting[4], ampD.getOutput());
            ampE.run();*/

            // Part B
            IntcodeComputer[] amps = new IntcodeComputer[5];
            amps[0] = new IntcodeComputer(opcodeArray, phaseSetting[0]);
            amps[1] = new IntcodeComputer(opcodeArray, phaseSetting[1]);
            amps[2] = new IntcodeComputer(opcodeArray, phaseSetting[2]);
            amps[3] = new IntcodeComputer(opcodeArray, phaseSetting[3]);
            amps[4] = new IntcodeComputer(opcodeArray, phaseSetting[4]);

            // Run the feedback loop
            while (!amps[4].isHalt()) {
                for (int i = 0; i < 5; i++) {
                    // Wire up consecutive amps, last one to first one
                    int prev = i - 1;
                    if (i == 0) {
                        prev = 4;
                    }
                    amps[i].setInput2(amps[prev].getOutput());

                    // Run amp
                    amps[i].run();
                }
            }

            System.out.println("Phase Setting " + phaseSettingStr + " resulted in Signal: " + amps[4].getOutput());

            // Find highest signal
            if (amps[4].getOutput() > highestSignal) {
                highestSignal = amps[4].getOutput();
                highestPhaseSetting = phaseSettingStr;
            }
        }

        System.out.println("Highest phase setting " + highestPhaseSetting + " with output signal " + highestSignal);
    }
}
