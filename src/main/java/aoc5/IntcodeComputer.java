package aoc5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class IntcodeComputer {
    
    private int[] memory;
    private int resultAddress;
    private int instructionPointer;
    
    public IntcodeComputer(int[] memory, int resultAddress) {
        this.memory = Arrays.copyOf(memory, memory.length);
        this.resultAddress = resultAddress;
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
                break;
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
                return read(resultAddress);
            default:
                throw new IllegalStateException("Unexpected opcode " + opcode);
            }
        }
        return read(resultAddress);
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
        System.out.println("Output: " + value);
    }
}
