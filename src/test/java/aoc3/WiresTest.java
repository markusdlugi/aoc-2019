package aoc3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WiresTest {

    @Test
    public void testMinDistance() {
        String wire1Str = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        String wire2Str = "U62,R66,U55,R34,D71,R55,D58,R83";

        String[] wire1 = wire1Str.split(",");
        String[] wire2 = wire2Str.split(",");

        int startingX = 300;
        int startingY = 300;

        byte[][] grid = new byte[600][600];

        Wires.trace(wire1, grid, 1, startingX, startingY);
        Wires.trace(wire2, grid, 2, startingX, startingY);

        int minDistance = Wires.calculateMinDistance(grid, startingX, startingY);

        /*for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid.length; j++) {
                if(i == startingX && j == startingY) {
                    System.out.print("o");
                    continue;
                }
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }*/

        assertEquals(159, minDistance);
    }

    @Test
    public void testMinDistance2() {
        String wire1Str = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        String wire2Str = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";

        String[] wire1 = wire1Str.split(",");
        String[] wire2 = wire2Str.split(",");

        int startingX = 300;
        int startingY = 300;

        byte[][] grid = new byte[600][600];

        Wires.trace(wire1, grid, 1, startingX, startingY);
        Wires.trace(wire2, grid, 2, startingX, startingY);

        int minDistance = Wires.calculateMinDistance(grid, startingX, startingY);

        assertEquals(135, minDistance);
    }

    @Test
    public void testCountSteps() {
        String wire1Str = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        String wire2Str = "U62,R66,U55,R34,D71,R55,D58,R83";

        String[] wire1 = wire1Str.split(",");
        String[] wire2 = wire2Str.split(",");

        int startingX = 300;
        int startingY = 300;

        byte[][] grid = new byte[600][600];

        Wires.trace(wire1, grid, 1, startingX, startingY);
        Wires.trace(wire2, grid, 2, startingX, startingY);

        int minSteps = Wires.calculateMinSteps(grid, wire1, wire2, startingX, startingY);

        assertEquals(610, minSteps);
    }

    @Test
    public void testCountSteps2() {
        String wire1Str = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        String wire2Str = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";

        String[] wire1 = wire1Str.split(",");
        String[] wire2 = wire2Str.split(",");

        int startingX = 300;
        int startingY = 300;

        byte[][] grid = new byte[600][600];

        Wires.trace(wire1, grid, 1, startingX, startingY);
        Wires.trace(wire2, grid, 2, startingX, startingY);

        int minSteps = Wires.calculateMinSteps(grid, wire1, wire2, startingX, startingY);

        assertEquals(410, minSteps);
    }
}
