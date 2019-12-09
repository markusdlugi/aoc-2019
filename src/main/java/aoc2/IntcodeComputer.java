package aoc2;

import java.util.Arrays;

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
            int opcode = next();

            switch (opcode) {
            case 1:
                write(read(next()) + read(next()), next());
                break;
            case 2:
                write(read(next()) * read(next()), next());
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

    private int read(int memoryAddress) {
        return memory[memoryAddress];
    }

    private void write(int value, int memoryAddress) {
        memory[memoryAddress] = value;
    }
}
