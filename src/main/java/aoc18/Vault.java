package aoc18;

import static java.util.stream.Collectors.joining;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * This is still a mess and unfortunately very slow for the Part B input, for some reason. Still solved it with a little
 * trick using nearest neighbor - always sorting the reachable keys by distance first before recursing, that yielded the
 * correct result very quickly, even though the algorithm takes ages to terminate.
 */
public class Vault {

    private static Map<ObjectPair, Distance> distanceMap = new HashMap<>();
    private static Map<PositionsWithKeys, Integer> acquiredKeyMinStepMap = new HashMap<>();

    private static int triedSolutions = 0;
    private static int minimumSteps = Integer.MAX_VALUE;
    private static List<Key> minimumStepsKeys;

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Vault.class.getResource("input.txt").toURI()));

        // Read input to array
        String[][] grid = new String[lines.get(0).length()][lines.size()];
        Iterator<String> iterator = lines.iterator();
        for (int y = 0; y < grid[0].length; y++) {
            String[] chars = iterator.next().split("");
            for (int x = 0; x < grid.length; x++) {
                grid[x][y] = chars[x];
            }
        }

        // Print grid after reading
        print(grid);

        // Find all starting points
        List<int[]> starts = findPositions(grid, "@");
        List<StartingPoint> startingPoints = new ArrayList<>();
        Character startCounter = 'A';
        for (int[] start : starts) {
            startingPoints.add(new StartingPoint("Start" + startCounter.toString(), start[0], start[1]));
            startCounter++;
        }

        // Find all keys
        List<Key> keys = new ArrayList<>();
        for (Character key = 'a'; key <= 'z'; key++) {
            int[] position = findPosition(grid, key.toString());
            if (position != null) {
                keys.add(new Key(key.toString(), position[0], position[1]));
            }
        }

        // Compute distances between all keys and starting points
        for (Key keyA : keys) {
            for (Key keyB : keys) {
                computeDistances(grid, keyA, keyB);
            }
            for (StartingPoint startingPoint : startingPoints) {
                computeDistances(grid, keyA, startingPoint);
            }
        }

        List<Key> acquiredKeys = new ArrayList<>();
        int steps = findShortestPath(startingPoints, keys, acquiredKeys, 0);

        System.out.println("Steps: " + steps);
        System.out.println("Path: " + minimumStepsKeys.stream().map(Object::toString).collect(joining(",")));
    }

    private static Integer findShortestPath(List<? extends LabyrinthObject> positions, List<Key> keys,
            List<Key> acquiredKeys, int steps) {
        // If we are already higher than minimum, cancel
        if (steps > minimumSteps) {
            return null;
        }

        // If we found all keys, cancel
        if (acquiredKeys.size() == keys.size()) {
            //System.out.println(acquiredKeys);
            triedSolutions++;
            if (triedSolutions % 100 == 0) {
                System.out.println(triedSolutions + " (current min steps: " + minimumSteps + ")");
            }
            if (steps < minimumSteps) {
                minimumSteps = steps;
                minimumStepsKeys = acquiredKeys;
                System.out.println(triedSolutions + " (current min steps: " + minimumSteps + ")");
            }
            return steps;
        }

        // If we already found the same keys in a different order and ended at the same position...
        // ...and also had less steps, then cancel
        Set<Key> acquiredKeySet = new HashSet<>(acquiredKeys);
        PositionsWithKeys positionsWithKeys = new PositionsWithKeys(new HashSet<>(positions), acquiredKeySet);
        if (acquiredKeyMinStepMap.containsKey(positionsWithKeys)
                && acquiredKeyMinStepMap.get(positionsWithKeys) < steps) {
            return null;
        } else {
            acquiredKeyMinStepMap.put(positionsWithKeys, steps);
        }

        // Get all reachable keys
        Set<Key> remainingKeys = new HashSet<>(keys);
        remainingKeys.removeAll(acquiredKeys);
        Set<Key> reachableKeys = new HashSet<>();
        Map<Key, LabyrinthObject> keyToPosition = new HashMap<>();
        for (LabyrinthObject position : positions) {
            Set<Key> reachableKeysForThisPosition = new HashSet<>();
            for (Key key : remainingKeys) {
                Distance distance = getDistance(position, key, acquiredKeySet);
                if (distance != null) {
                    Key newKey = new Key(key);
                    reachableKeysForThisPosition.add(newKey);
                    keyToPosition.put(newKey, position);
                    newKey.setDistance(distance.getDistance());
                }
            }
            if (reachableKeysForThisPosition.isEmpty()) {
                continue;
            }

            reachableKeys.addAll(reachableKeysForThisPosition);
        }

        // If we have no reachable keys, something went wrong. Oh well.
        if (reachableKeys.isEmpty()) {
            return null;
        }

        List<Key> reachableKeyList = new ArrayList<>(reachableKeys);
        Collections.sort(reachableKeyList, Comparator.comparingInt(Key::getDistance));

        // Recurse for all reachable keys
        int minSteps = Integer.MAX_VALUE;
        List<Key> path = null;
        for (Key key : reachableKeyList) {
            List<Key> acquiredKeysCopy = new ArrayList<>(acquiredKeys);
            acquiredKeysCopy.add(new Key(key));

            List<LabyrinthObject> newPositions = new ArrayList<>(positions);
            newPositions.remove(keyToPosition.get(key));
            newPositions.add(key);

            Integer totalSteps = findShortestPath(newPositions, keys, acquiredKeysCopy, steps + key.getDistance());
            if (totalSteps != null && totalSteps < minSteps) {
                minSteps = totalSteps;
            }
        }

        return minSteps;
    }

    private static void print(String[][] grid) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
    }

    private static List<int[]> findPositions(String[][] grid, String toFind) {
        List<int[]> resultList = new ArrayList<>();
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y].equals(toFind)) {
                    resultList.add(new int[] { x, y, 0 });
                }
            }
        }
        return resultList;
    }

    private static int[] findPosition(String[][] grid, String toFind) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y].equals(toFind)) {
                    return new int[] { x, y, 0 };
                }
            }
        }
        return null;
    }

    private static Distance getDistance(LabyrinthObject src, LabyrinthObject dest, Set<Key> keys) {
        ObjectPair objectPair = new ObjectPair(src, dest);
        Distance distance = distanceMap.get(objectPair);
        if (distance == null) {
            return null;
        }
        if (keys != null) {
            if (!keys.containsAll(distance.getRequiredKeys())) {
                return null;
            }
        }
        return distance;
    }

    private static void computeDistances(String[][] grid, LabyrinthObject src, LabyrinthObject dest) {
        ObjectPair objectPair = new ObjectPair(src, dest);
        int[] srcPosition = new int[] { src.getX(), src.getY() };
        int[] destPosition = new int[] { dest.getX(), dest.getY() };
        Distance distance = computeDistances(grid, srcPosition, destPosition);
        if (distance != null) {
            distanceMap.put(objectPair, distance);
            distance.getRequiredKeys().remove(src);
            distance.getRequiredKeys().remove(dest);
        }
    }

    private static Distance computeDistances(String[][] grid, int[] src, int[] dest) {
        Set<Integer> visited = new HashSet<>();
        visited.add(src[0] * 100 + src[1]);

        Queue<Position> queue = new LinkedList<>();
        queue.add(new Position(src[0], src[1], new Distance()));

        while (!queue.isEmpty()) {
            Position curr = queue.peek();
            if (curr.getX() == dest[0] && curr.getY() == dest[1]) {
                return curr.getDistance();
            }

            queue.remove();
            for (int i = 0; i < 4; i++) {
                Position adjacent = applyDirection(curr, i);
                int hashCode = adjacent.getX() * 100 + adjacent.getY();

                if (isValid(grid, adjacent) && !visited.contains(hashCode)) {
                    Key key = getKeyForDoorOrKey(grid, adjacent);
                    if (key != null) {
                        adjacent.getDistance().getRequiredKeys().add(key);
                    }
                    visited.add(hashCode);
                    queue.add(adjacent);
                }
            }
        }

        return null;
    }

    private static boolean isValid(String[][] grid, Position gridPosition) {
        if (gridPosition.getX() < 0 || gridPosition.getX() >= grid.length || gridPosition.getY() < 0
                || gridPosition.getY() >= grid.length) {
            return false;
        }
        String gridValue = grid[gridPosition.getX()][gridPosition.getY()];
        if (gridValue.equals("#")) {
            return false;
        }
        if (gridValue.equals(".") || gridValue.equals("@")) {
            return true;
        }
        return true;
    }

    private static Key getKeyForDoorOrKey(String[][] grid, Position gridPosition) {
        String gridValue = grid[gridPosition.getX()][gridPosition.getY()];
        Character character = gridValue.toCharArray()[0];
        if (character >= 'a' && character <= 'z') {
            return new Key(character.toString(), 0, 0);
        }
        if (character >= 'A' && character <= 'Z') {
            return new Key(character.toString().toLowerCase(), 0, 0);
        }
        return null;
    }

    private static final int[] dx = { 0, 1, 0, -1 };
    private static final int[] dy = { 1, 0, -1, 0 };

    private static Position applyDirection(Position position, int dir) {
        if (dir < 0 || dir > 3) {
            throw new IllegalStateException();
        }
        Distance distance = new Distance(position.getDistance().getDistance() + 1,
                new HashSet<>(position.getDistance().getRequiredKeys()));
        return new Position(position.getX() + dx[dir], position.getY() + dy[dir], distance);
    }
}
