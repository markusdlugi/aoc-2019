package aoc5;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DiagnosticProgramTest {

    @Test
    public void intcodeComputerTest() {
        int[] input5 = new int[] {1101,100,-1,4,4};
        IntcodeComputer computer = new IntcodeComputer(input5, 4);
        assertEquals(99, computer.run());
    }
}
