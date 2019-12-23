package aoc23;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class IntcodeNetworkMain {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(IntcodeNetworkMain.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        Map<Integer, IntcodeComputer> computerMap = new HashMap<>();
        IntcodeNetwork network = new IntcodeNetwork(computerMap);

        for (int i = 0; i < 50; i++) {
            Queue<Long> input = new LinkedList<>();
            Queue<Long> output = new LinkedList<>();
            IntcodeComputer computer = new IntcodeComputer(opcodeArray, input, output, i, network);
            // Send network address as initialization
            input.add((long) i);

            computerMap.put(i, computer);
        }

        boolean idle = false;
        while (!idle) {
            int nothingReceived = 0;
            for (IntcodeComputer computer : computerMap.values()) {
                long received = computer.run();
                if (received == -1) {
                    nothingReceived++;
                }
            }
            if (nothingReceived == computerMap.size()) {
                idle = network.sendNatMessage();
            }
        }
    }
}
