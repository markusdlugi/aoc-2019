package aoc2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OpcodesTest {

    @Test
    public void testExecuteProgram() {
        int[] input1 = new int[] {1,0,0,0,99};
        Opcodes.executeProgram(input1);
        assertEquals(2, input1[0]);

        int[] input2 = new int[] {2,3,0,3,99};
        Opcodes.executeProgram(input2);
        assertEquals(6, input2[3]);

        int[] input3 = new int[] {2,4,4,5,99,0};
        Opcodes.executeProgram(input3);
        assertEquals(9801, input3[5]);

        int[] input4 = new int[] {1,1,1,4,99,5,6,0,99};
        Opcodes.executeProgram(input4);
        assertEquals(30, input4[0]);
    }

    @Test
    public void intcodeComputerTest() {
        int[] input1 = new int[] {1,0,0,0,99};
        IntcodeComputer computer = new IntcodeComputer(input1, 0);
        assertEquals(2, computer.run());

        int[] input2 = new int[] {2,3,0,3,99};
        computer = new IntcodeComputer(input2, 3);
        assertEquals(6, computer.run());

        int[] input3 = new int[] {2,4,4,5,99,0};
        computer = new IntcodeComputer(input3, 5);
        assertEquals(9801, computer.run());

        int[] input4 = new int[] {1,1,1,4,99,5,6,0,99};
        computer = new IntcodeComputer(input4, 0);
        assertEquals(30, computer.run());
    }
}
