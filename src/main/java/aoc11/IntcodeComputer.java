package aoc11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class IntcodeComputer {
    
    private long[] memory;
    private int instructionPointer;
    private int relativeBase;

    private Integer input1;
    private Integer input2;
    private long output;
    private boolean halt;

    private boolean muteOutput;
    
    public IntcodeComputer(long[] memory) {
        this.memory = Arrays.copyOf(memory, memory.length * 10);
    }

    public IntcodeComputer(long[] memory, Integer input1, Integer input2) {
        this(memory);
        this.input1 = input1;
        this.input2 = input2;
        this.muteOutput = true;
    }

    public IntcodeComputer(long[] memory, Integer input1) {
        this(memory);
        this.input1 = input1;
        this.muteOutput = true;
    }

    public void setInput1(final Integer input1) {
        this.input1 = input1;
    }

    public void setInput2(final Integer input2) {
        this.input2 = input2;
    }

    public long getOutput() {
        return this.output;
    }

    public boolean isHalt() {
        return this.halt;
    }
    
    public long run() {
        while(instructionPointer < memory.length) {
            Long opcode = next();

            // Handle parameter modes (position vs immediate vs relative)
            Iterator<String> parameterModes = null;
            if(opcode > 100) {
                // Extract actual opcode
                long operation = opcode % 100;
                String modeString = Long.valueOf((opcode - operation) / 100).toString();
                opcode = operation;

                // Build iterator with reading modes
                List<String> modes = Arrays.asList(modeString.split(""));
                Collections.reverse(modes);
                if(modes.size() < 3) {
                    modes = new ArrayList<>(modes);
                    while (modes.size() < 3) {
                        modes.add("0");
                    }
                }
                parameterModes = modes.iterator();
            }
            else {
                // All position mode
                parameterModes = Arrays.asList("0","0","0").iterator();
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
                write(input(), next(parameterModes.next()));
                break;
            case 4:
                // Output
                param1 = read(parameterModes.next());
                output(param1);
                // Return after to provide new input
                return output;
            case 5:
                // Jump if true
                param1 = read(parameterModes.next());
                if(param1 != 0) {
                    instructionPointer = (int) read(parameterModes.next());
                }
                else {
                    // Skip parameter
                    next();
                }
                break;
            case 6:
                // Jump if false
                param1 = read(parameterModes.next());
                if(param1 == 0) {
                    instructionPointer = (int) read(parameterModes.next());
                }
                else {
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
                halt = true;
                return output;
            default:
                throw new IllegalStateException("Unexpected opcode " + opcode);
            }
        }
        return output;
    }

    private long next() {
        return memory[instructionPointer++];
    }

    private int next(String readingMode) {
        if(readingMode.equals("2")) {
            return relativeBase + (int) next();
        }
        else {
            return (int) next();
        }
    }

    private long read(String readingMode) {
        if(readingMode.equals("1")) {
            return next();
        }
        else if(readingMode.equals("2")) {
            return read(relativeBase + (int) next());
        }
        else {
            return read((int) next());
        }
    }

    private long read(int memoryAddress) {
        return memory[memoryAddress];
    }

    private void write(long value, int memoryAddress) {
        memory[memoryAddress] = value;
    }

    private int input() {
        if(input1 != null) {
            int result = Integer.valueOf(input1);
            input1 = null;
            return result;
        }
        else if(input2 != null) {
            int result = Integer.valueOf(input2);
            input2 = null;
            return result;
        }
        try {
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                return Integer.parseInt(scanner.nextLine());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void output(long value) {
        if(!muteOutput) {
            System.out.println("Output: " + value);
        }
        output = value;
    }
}
