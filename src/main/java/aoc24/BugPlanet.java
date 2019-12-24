package aoc24;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BugPlanet {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(BugPlanet.class.getResource("input.txt").toURI()));

        byte[][] grid = new byte[lines.size()][lines.get(0).length()];

        int y = 0;
        for (String line : lines) {
            String[] chars = line.split("");
            int x = 0;
            for (String character : chars) {
                if (character.equals("#")) {
                    grid[x][y] = 1;
                }
                x++;
            }
            y++;
        }

        Map<Long, Integer> bioDiversityMap = new HashMap<>();
        Map<Integer, byte[][]> gridMap = new HashMap<>();
        gridMap.put(0, grid);

        byte[][] gridCopy = new byte[grid.length][grid[0].length];
        for (int x = 0; x < grid.length; x++) {
            gridCopy[x] = Arrays.copyOf(grid[x], grid[x].length);
        }

        // Part A
        for (int steps = 0; ; steps++) {
            simulateBugs(gridMap, false);
            long bioDiversity = computeBioDiversity(grid);
            if (bioDiversityMap.containsKey(bioDiversity)) {
                System.out.println("Biodiversity " + bioDiversity + " was reached twice after " + steps + " steps");
                break;
            } else {
                bioDiversityMap.put(bioDiversity, steps);
            }
        }

        // Part B
        gridMap.put(0, gridCopy);
        for (int steps = 0; steps < 200; steps++) {
            simulateBugs(gridMap, true);
        }
        long bugs = countAllBugs(gridMap);
        System.out.println("Bugs after 200 minutes: " + bugs);
    }

    public static void simulateBugs(Map<Integer, byte[][]> gridMap, boolean recursive) {
        int[] position = new int[] { 0, 0, 0 };
        Set<Integer> visited = new HashSet<>();
        visited.add(position[2] * 10000 + position[0] * 100 + position[1]);

        Queue<Integer[]> queue = new LinkedList<>();
        queue.add(new Integer[] { position[0], position[1], position[2] });

        Set<Integer[]> toDie = new HashSet<>();
        Set<Integer[]> toInfest = new HashSet<>();

        int currentMinLevel = Integer.MAX_VALUE;
        int currentMaxLevel = Integer.MIN_VALUE;
        for (Integer level : gridMap.keySet()) {
            currentMinLevel = Math.min(currentMinLevel, level);
            currentMaxLevel = Math.max(currentMaxLevel, level);
        }

        while (!queue.isEmpty()) {
            Integer[] curr = queue.poll();
            int bugs = 0;
            for (int i = 0; i < 4; i++) {
                Integer[] adjacent = applyDirection(curr, i);

                List<Integer[]> adjacentList = null;
                if (recursive) {
                    if (adjacent[0] == 2 && adjacent[1] == 2 || !isValid(gridMap.get(0), adjacent)) {
                        adjacentList = getRecursiveNeighbors(curr, i);
                    }
                } else if (!isValid(gridMap.get(0), adjacent)) {
                    continue;
                }

                if (adjacentList == null) {
                    adjacentList = Collections.singletonList(adjacent);
                }

                for (Integer[] adj : adjacentList) {
                    int hashCode = adj[2] * 10000 + adj[0] * 100 + adj[1];
                    if (!visited.contains(hashCode)) {
                        visited.add(hashCode);
                        queue.add(adj);
                    }
                    byte[][] currentGrid = gridMap.get(adj[2]);
                    if (currentGrid == null) {
                        // Bounds to avoid infinite BFS
                        if (adj[2] < currentMinLevel - 1 || adj[2] > currentMaxLevel + 1) {
                            queue.remove(adj);
                            continue;
                        }
                        currentGrid = new byte[gridMap.get(0).length][gridMap.get(0)[0].length];
                        gridMap.put(adj[2], currentGrid);
                    }

                    if (currentGrid[adj[0]][adj[1]] == 1) {
                        bugs++;
                    }
                }
            }

            byte[][] currentGrid = gridMap.get(curr[2]);
            if (currentGrid[curr[0]][curr[1]] == 1) {
                if (bugs != 1) {
                    toDie.add(curr);
                }
            } else {
                if (bugs == 1 || bugs == 2) {
                    toInfest.add(curr);
                }
            }
        }

        for (Integer[] die : toDie) {
            gridMap.get(die[2])[die[0]][die[1]] = 0;
        }
        for (Integer[] infest : toInfest) {
            gridMap.get(infest[2])[infest[0]][infest[1]] = 1;
        }
    }

    public static boolean isValid(byte[][] grid, Integer[] adjacent) {
        return adjacent[0] >= 0 && adjacent[0] < grid.length && adjacent[1] >= 0 && adjacent[1] < grid[0].length;
    }

    private static final int[] dx = { 0, 1, 0, -1 };
    private static final int[] dy = { -1, 0, 1, 0 };

    private static Integer[] applyDirection(Integer[] position, int dir) {
        if (dir < 0 || dir > 3) {
            throw new IllegalStateException();
        }
        return new Integer[] { position[0] + dx[dir], position[1] + dy[dir], position[2] };
    }

    private static List<Integer[]> getRecursiveNeighbors(Integer[] position, int dir) {
        if (dir < 0 || dir > 3) {
            throw new IllegalStateException();
        }
        List<Integer[]> resultList = new ArrayList<>();
        if (position[0] + dx[dir] == 2 && position[1] + dy[dir] == 2) {
            for (int i = 0; i < 5; i++) {
                int newX = dx[dir] == 0 ? i : dx[dir] == -1 ? 4 : 0;
                int newY = dy[dir] == 0 ? i : dy[dir] == -1 ? 4 : 0;
                resultList.add(new Integer[] { newX, newY, position[2] + 1 });
            }
        } else {
            resultList.add(new Integer[] { 2 + dx[dir], 2 + dy[dir], position[2] - 1 });
        }
        return resultList;
    }

    private static long computeBioDiversity(byte[][] grid) {
        long result = 0;
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y] == 1) {
                    int exp = y * grid.length + x;
                    result += Math.pow(2, exp);
                }
            }
        }
        return result;
    }

    private static long countAllBugs(Map<Integer, byte[][]> gridMap) {
        long result = 0;
        for (byte[][] grid : gridMap.values()) {
            for (int y = 0; y < grid[0].length; y++) {
                for (int x = 0; x < grid.length; x++) {
                    result += grid[x][y];
                }
            }
        }
        return result;
    }

    private static void printGrid(Map<Integer, byte[][]> gridMap) {
        int currentMinLevel = Integer.MAX_VALUE;
        int currentMaxLevel = Integer.MIN_VALUE;
        for (Integer level : gridMap.keySet()) {
            currentMinLevel = Math.min(currentMinLevel, level);
            currentMaxLevel = Math.max(currentMaxLevel, level);
        }
        for (int level = currentMinLevel; level <= currentMaxLevel; level++) {
            System.out.println("Level " + level + ":");
            byte[][] grid = gridMap.get(level);
            for (int y = 0; y < grid[0].length; y++) {
                for (int x = 0; x < grid.length; x++) {
                    char printChar = grid[x][y] == 1 ? '#' : '.';
                    System.out.print(printChar);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
