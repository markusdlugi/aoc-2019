package aoc7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class IntcodeComputer {
    
    private int[] memory;
    private int instructionPointer;

    private Integer input1;
    private Integer input2;
    private int output;
    private boolean halt;

    private boolean muteOutput;
    
    public IntcodeComputer(int[] memory) {
        this.memory = Arrays.copyOf(memory, memory.length);
    }

    public IntcodeComputer(int[] memory, Integer input1, Integer input2) {
        this(memory);
        this.input1 = input1;
        this.input2 = input2;
        this.muteOutput = true;
    }

    public IntcodeComputer(int[] memory, Integer input1) {
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

    public int getOutput() {
        return this.output;
    }

    public boolean isHalt() {
        return this.halt;
    }
    
    public int run() {
        while(instructionPointer < memory.length) {
            Integer opcode = next();

            // Handle reading modes (parameter vs immediate)
            Iterator<String> readingModes = null;
            if(opcode > 100) {
                // Extract actual opcode
                int operation = Integer.parseInt(opcode.toString().substring(opcode.toString().length() - 2));
                String modeString = Integer.valueOf(opcode - operation).toString();
                modeString = modeString.substring(0, modeString.length() - 2);
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
                readingModes = modes.iterator();
            }
            else {
                // All position mode
                readingModes = Arrays.asList("0","0","0").iterator();
            }

            int param1;
            int param2;
            int result;
            switch (opcode) {
            case 1:
                // Add
                param1 = read(readingModes.next());
                param2 = read(readingModes.next());
                result = next();
                write(param1 + param2, result);
                break;
            case 2:
                // Multiply
                param1 = read(readingModes.next());
                param2 = read(readingModes.next());
                result = next();
                write(param1 * param2, result);
                break;
            case 3:
                // Input
                write(input(), next());
                break;
            case 4:
                // Output
                param1 = read(readingModes.next());
                output(param1);
                // Special case: feedback loop
                return output;
            case 5:
                // Jump if true
                param1 = read(readingModes.next());
                if(param1 != 0) {
                    instructionPointer = read(readingModes.next());
                }
                else {
                    // Skip parameter
                    next();
                }
                break;
            case 6:
                // Jump if false
                param1 = read(readingModes.next());
                if(param1 == 0) {
                    instructionPointer = read(readingModes.next());
                }
                else {
                    // Skip parameter
                    next();
                }
                break;
            case 7:
                // Less than
                param1 = read(readingModes.next());
                param2 = read(readingModes.next());
                write(param1 < param2 ? 1 : 0, next());
                break;
            case 8:
                // Equals
                param1 = read(readingModes.next());
                param2 = read(readingModes.next());
                write(param1 == param2 ? 1 : 0, next());
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

    private int next() {
        return memory[instructionPointer++];
    }

    private int read(String readingMode) {
        return readingMode.equals("0") ? read(next()) : next();
    }

    private int read(int memoryAddress) {
        return memory[memoryAddress];
    }

    private void write(int value, int memoryAddress) {
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

    private void output(int value) {
        if(!muteOutput) {
            System.out.println("Output: " + value);
        }
        output = value;
    }
}
