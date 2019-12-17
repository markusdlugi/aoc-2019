package aoc3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Wires {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Wires.class.getResource("input.txt").toURI()));

        String wire1Str = lines.get(0);
        String wire2Str = lines.get(1);

        String[] wire1 = wire1Str.split(",");
        String[] wire2 = wire2Str.split(",");

        int startingX = 25000;
        int startingY = 25000;

        byte[][] grid = new byte[50000][50000];

        trace(wire1, grid, 1, startingX, startingY);
        trace(wire2, grid, 2, startingX, startingY);

        // Part A
        //int minDistance = calculateMinDistance(grid, startingX, startingY);

        // Part B
        int minSteps = calculateMinSteps(grid, wire1, wire2, startingX, startingY);
    }

    public static void trace(String[] wire, byte[][] grid, int wireNumber, int startingX, int startingY) {
        int x = startingX;
        int y = startingY;
        for(String path : wire) {
            String dir = path.substring(0,1);
            int dist = Integer.parseInt(path.substring(1));
            switch(dir) {
            case "U":
                // Up
                for(int i = 0; i < dist; i++) {
                    grid[x][++y]+= wireNumber;
                }
                break;
            case "D":
                // Down
                for(int i = 0; i < dist; i++) {
                    grid[x][--y]+= wireNumber;
                }
                break;
            case "R":
                // Right
                for(int i = 0; i < dist; i++) {
                    grid[++x][y]+= wireNumber;
                }
                break;
            case "L":
                // Left
                for(int i = 0; i < dist; i++) {
                    grid[--x][y]+= wireNumber;
                }
                break;
            }
        }
    }

    public static int countSteps(String[] wire, int startingX, int startingY, int destX, int destY) {
        int x = startingX;
        int y = startingY;
        int steps = 0;
        for(String path : wire) {
            String dir = path.substring(0,1);
            int dist = Integer.parseInt(path.substring(1));
            switch(dir) {
            case "U":
                // Up
                for(int i = 0; i < dist; i++) {
                    y++;
                    steps++;
                    if(x == destX && y == destY) {
                        return steps;
                    }
                }
                break;
            case "D":
                // Down
                for(int i = 0; i < dist; i++) {
                    y--;
                    steps++;
                    if(x == destX && y == destY) {
                        return steps;
                    }
                }
                break;
            case "R":
                // Right
                for(int i = 0; i < dist; i++) {
                    x++;
                    steps++;
                    if(x == destX && y == destY) {
                        return steps;
                    }
                }
                break;
            case "L":
                // Left
                for(int i = 0; i < dist; i++) {
                    x--;
                    steps++;
                    if(x == destX && y == destY) {
                        return steps;
                    }
                }
                break;
            }
        }
        return Integer.MAX_VALUE;
    }

    public static int calculateMinDistance(byte[][] grid, int startingX, int startingY) {
        int minDist = Integer.MAX_VALUE;
        int minX = 0;
        int minY = 0;

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid.length; j++) {
                if((i == startingX && j == startingY) || grid[i][j] != 3) {
                    continue;
                }
                int distance = Math.abs(i - startingX) + Math.abs(j - startingY);
                System.out.println("Intersection " + i + "," + j + " has distance " + distance);
                if(distance < minDist) {
                    minDist = distance;
                    minX = i;
                    minY = j;
                }
            }
        }
        System.out.println("Closest intersection: " + minX + "," + minY + " with distance " + minDist);
        return minDist;
    }

    public static int calculateMinSteps(byte[][] grid, String[] wire1, String[] wire2, int startingX, int startingY) {
        int minDist = Integer.MAX_VALUE;
        int minX = 0;
        int minY = 0;

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid.length; j++) {
                if((i == startingX && j == startingY) || grid[i][j] != 3) {
                    continue;
                }
                int steps1 = countSteps(wire1, startingX, startingY, i, j);
                int steps2 = countSteps(wire2, startingX, startingY, i, j);

                int distance = steps1 + steps2;
                System.out.println("Intersection " + i + "," + j + " has steps " + distance);
                if(distance < minDist) {
                    minDist = distance;
                    minX = i;
                    minY = j;
                }
            }
        }
        System.out.println("Closest intersection: " + minX + "," + minY + " with steps " + minDist);
        return minDist;
    }
}
