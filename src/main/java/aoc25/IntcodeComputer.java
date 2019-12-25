package aoc25;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import aoc23.IntcodeNetwork;

public class IntcodeComputer {

    private long[] memory;
    private int instructionPointer;
    private int relativeBase;

    private Queue<Long> inputQueue;
    private Queue<Long> outputQueue;
    private int networkAddress;
    private IntcodeNetwork network;
    private boolean firstInput;
    private boolean halt;

    private boolean haltOnInput;
    private boolean muteOutput;

    public IntcodeComputer(long[] memory) {
        this.memory = Arrays.copyOf(memory, memory.length * 10);
    }

    public IntcodeComputer(long[] memory, boolean muteOutput) {
        this(memory);
        this.muteOutput = muteOutput;
    }

    public IntcodeComputer(long[] memory, Queue<Long> inputQueue, Queue<Long> outputQueue, int networkAddress,
            IntcodeNetwork network) {
        this(memory);
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.networkAddress = networkAddress;
        this.network = network;
        this.muteOutput = true;
    }

    public IntcodeComputer(long[] memory, Queue<Long> inputQueue, Queue<Long> outputQueue) {
        this(memory);
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    public Queue<Long> getInputQueue() {
        return inputQueue;
    }

    public void setHaltOnInput(boolean haltOnInput) {
        this.haltOnInput = haltOnInput;
    }

    public Queue<Long> getOutputQueue() {
        return this.outputQueue;
    }

    public int getNetworkAddress() {
        return this.networkAddress;
    }

    public boolean isFirstInput() {
        return this.firstInput;
    }

    public boolean isHalt() {
        return this.halt;
    }

    public long run() {
        boolean receivedSomething = false;
        while (instructionPointer < memory.length) {
            Long opcode = next();

            // Handle parameter modes (position vs immediate vs relative)
            Iterator<String> parameterModes = null;
            if (opcode > 100) {
                // Extract actual opcode
                long operation = opcode % 100;
                String modeString = Long.valueOf((opcode - operation) / 100).toString();
                opcode = operation;

                // Build iterator with reading modes
                List<String> modes = Arrays.asList(modeString.split(""));
                Collections.reverse(modes);
                if (modes.size() < 3) {
                    modes = new ArrayList<>(modes);
                    while (modes.size() < 3) {
                        modes.add("0");
                    }
                }
                parameterModes = modes.iterator();
            } else {
                // All position mode
                parameterModes = Arrays.asList("0", "0", "0").iterator();
            }

            long param1;
            long param2;
            long result;
            switch (opcode.intValue()) {
            case 1:
                // Add
                param1 = read(parameterModes.next());
                param2 = read(parameterModes.next());
                result = next(parameterModes.next());
                write(param1 + param2, (int) result);
                break;
            case 2:
                // Multiply
                param1 = read(parameterModes.next());
                param2 = read(parameterModes.next());
                result = next(parameterModes.next());
                write(param1 * param2, (int) result);
                break;
            case 3:
                // Input
                if(!haltOnInput && inputQueue.isEmpty()) {
                    instructionPointer--;
                    return 0L;
                }
                long input = input();
                write(input, next(parameterModes.next()));
                if (input == -1) {
                    return receivedSomething ? 1 : -1;
                } else {
                    receivedSomething = true;
                }
                break;
            case 4:
                // Output
                param1 = read(parameterModes.next());
                output(param1);
                // Return after to provide new input
                //return outputQueue.poll();
                break;
            case 5:
                // Jump if true
                param1 = read(parameterModes.next());
                if (param1 != 0) {
                    instructionPointer = (int) read(parameterModes.next());
                } else {
                    // Skip parameter
                    next();
                }
                break;
            case 6:
                // Jump if false
                param1 = read(parameterModes.next());
                if (param1 == 0) {
                    instructionPointer = (int) read(parameterModes.next());
                } else {
                    // Skip parameter
                    next();
                }
                break;
            case 7:
                // Less than
                param1 = read(parameterModes.next());
                param2 = read(parameterModes.next());
                write(param1 < param2 ? 1 : 0, next(parameterModes.next()));
                break;
            case 8:
                // Equals
                param1 = read(parameterModes.next());
                param2 = read(parameterModes.next());
                write(param1 == param2 ? 1 : 0, next(parameterModes.next()));
                break;
            case 9:
                // Adjust relative base
                param1 = read(parameterModes.next());
                relativeBase += param1;
                break;
            case 99:
            default:
                halt = true;
                return 0L;
            }
        }
        return 0L;
    }

    private long next() {
        return memory[instructionPointer++];
    }

    private int next(String readingMode) {
        if (readingMode.equals("2")) {
            return relativeBase + (int) next();
        } else {
            return (int) next();
        }
    }

    private long read(String readingMode) {
        if (readingMode.equals("1")) {
            return next();
        } else if (readingMode.equals("2")) {
            return read(relativeBase + (int) next());
        } else {
            return read((int) next());
        }
    }

    private long read(int memoryAddress) {
        return memory[memoryAddress];
    }

    private void write(long value, int memoryAddress) {
        memory[memoryAddress] = value;
    }

    private long input() {
        if (inputQueue != null && !inputQueue.isEmpty()) {
            long input = inputQueue.poll();
            return input;
        }
        try {
            System.out.println("Input: ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String next = scanner.nextLine();
                for(Character character : next.toCharArray()) {
                    inputQueue.add((long) character);
                }
                inputQueue.add((long) '\n');
                return inputQueue.poll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void output(long value) {
        if (!muteOutput) {
            System.out.print((char) value);
        }
        outputQueue.add(value);
    }
}
