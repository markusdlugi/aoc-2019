package aoc15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import aoc13.IntcodeComputer;

public class OxygenSystemMaze {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(OxygenSystemMaze.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        IntcodeComputer computer = new IntcodeComputer(opcodeArray, true);

        // We always move north, west, south, east
        final int[] directions = new int[] { 1, 4, 2, 3 };
        int currentDirection = 0;

        Map<Coords, Integer> maze = new HashMap<>();
        Set<Coords> exploredPositions = new HashSet<>();
        Set<Coords> pathPositions = new HashSet<>();
        Coords currentPosition = new Coords(0, 0);
        pathPositions.add(currentPosition);

        // Part A
        Coords oxygenSystem = null;
        while (exploredPositions.size() < pathPositions.size()) {
            // Explore all directions of current position
            for (int exploreDirection = 1; exploreDirection <= 4; exploreDirection++) {
                Coords targetCoords = applyDirection(currentPosition, exploreDirection);
                if (maze.containsKey(targetCoords)) {
                    continue;
                }

                computer.setInput1(exploreDirection);
                int status = (int) computer.run();
                int returnDirection = (exploreDirection % 2 == 0) ? exploreDirection - 1 : exploreDirection + 1;
                if (returnDirection > 4) {
                    returnDirection = returnDirection % 4;
                }
                switch (status) {
                case 0:
                    // Hit a wall, add to maze
                    maze.put(targetCoords, 0);
                    break;
                case 2:
                    // Found oxygen system
                    oxygenSystem = targetCoords;
                case 1:
                    // Add to maze and return to previous position
                    maze.put(targetCoords, status);
                    pathPositions.add(targetCoords);
                    computer.setInput1(returnDirection);
                    computer.run();
                }
            }
            exploredPositions.add(currentPosition);

            // Move along right wall to traverse entire maze
            Coords targetCoords = null;
            int directionToMove = 1;
            for (int i = currentDirection - 1; i < currentDirection + 3; i++) {
                int newIndex = (i < 0) ? 3 : i % 4;
                int newDirection = directions[newIndex];
                targetCoords = applyDirection(currentPosition, newDirection);
                if (pathPositions.contains(targetCoords)) {
                    currentDirection = newIndex;
                    directionToMove = newDirection;
                    break;
                }
            }

            // Do actual move
            computer.setInput1(directionToMove);
            int status = (int) computer.run();

            // Update position
            switch (status) {
            case 0:
                throw new IllegalStateException();
            case 1:
            case 2:
                currentPosition = targetCoords;
            }
        }

        int distance = getShortestDistance(maze, new Coords(0, 0), oxygenSystem);
        System.out.println("Distance to oxygen system: " + distance);

        System.out.println();
        printMaze(maze, currentPosition);
        System.out.println();

        // Part B
        int minutes = 0;
        Map<Coords, Object> oxygenCoords = new HashMap<>();
        oxygenCoords.put(oxygenSystem, null);
        while (oxygenCoords.size() < pathPositions.size()) {
            Set<Coords> currentOxygenCoords = new HashSet<>(oxygenCoords.keySet());
            for (Coords oxygenCoord : currentOxygenCoords) {
                for (int direction = 1; direction <= 4; direction++) {
                    Coords targetCoords = applyDirection(oxygenCoord, direction);
                    if (!pathPositions.contains(targetCoords) || oxygenCoords.containsKey(targetCoords)) {
                        continue;
                    }
                    oxygenCoords.put(targetCoords, null);
                }
            }
            minutes++;
        }
        System.out.println("Minutes to fill: " + minutes);
    }

    private static int getShortestDistance(Map<Coords, Integer> maze, Coords src, Coords dest) {
        Set<Coords> visited = new HashSet<>();
        visited.add(src);

        Queue<Coords> queue = new LinkedList<>();
        queue.add(src);

        while (!queue.isEmpty()) {
            Coords curr = queue.peek();
            if (curr.getX() == dest.getX() && curr.getY() == dest.getY()) {
                return curr.getDistance();
            }

            queue.remove();
            for (int i = 1; i <= 4; i++) {
                Coords adjacent = applyDirection(curr, i);
                adjacent.setDistance(curr.getDistance() + 1);

                if (maze.containsKey(adjacent) && maze.get(adjacent) > 0 && !visited.contains(adjacent)) {
                    visited.add(adjacent);
                    queue.add(adjacent);
                }
            }
        }

        return -1;
    }

    private static final int[] dx = {0, 0, -1, 1};
    private static final int[] dy = {1, -1, 0, 0};

    private static Coords applyDirection(Coords position, int dir) {
        if(dir < 1 || dir > 4) {
            throw new IllegalStateException();
        }
        return new Coords(position.getX() + dx[dir - 1], position.getY() + dy[dir - 1]);
    }

    private static void printMaze(Map<Coords, Integer> maze, Coords currentPosition) {
        // Find max coords for printing
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Coords panel : maze.keySet()) {
            minX = Integer.min(minX, panel.getX());
            minY = Integer.min(minY, panel.getY());
            maxX = Integer.max(maxX, panel.getX());
            maxY = Integer.max(maxY, panel.getY());
        }

        // Print painted panels
        for (int y = maxY; y >= minY; y--) {
            for (int x = minX; x <= maxX; x++) {
                if (x == currentPosition.getX() && y == currentPosition.getY()) {
                    System.out.print("D");
                    continue;
                }
                if (x == 0 && y == 0) {
                    System.out.print("S");
                    continue;
                }
                Coords coord = new Coords(x, y);
                int status = (maze.containsKey(coord)) ? maze.get(coord) : -1;
                if (status == 0) {
                    System.out.print("#");
                } else if (status == 1) {
                    System.out.print(".");
                } else if (status == 2) {
                    System.out.print("O");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
