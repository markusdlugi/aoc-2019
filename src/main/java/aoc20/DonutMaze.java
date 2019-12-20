package aoc20;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DonutMaze {

    private static Map<Portal, Portal> portals = new HashMap<>();
    private static Map<Coords, Portal> portalPositions = new HashMap<>();
    private static Map<Coords, Portal> portalWarpPositions = new HashMap<>();

    private static Coords startPosition = null;
    private static Coords endPosition = null;

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(DonutMaze.class.getResource("input.txt").toURI()));

        // Read input to array
        // Only third line has (nearly) full length
        char[][] grid = new char[lines.get(2).length() + 2][lines.size()];
        Iterator<String> iterator = lines.iterator();
        for (int y = 0; y < grid[0].length; y++) {
            String[] chars = iterator.next().split("");
            for (int x = 0; x < grid.length && x < chars.length; x++) {
                grid[x][y] = chars[x].toCharArray()[0];
            }
        }

        // Find all portals
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (!(grid[x][y] >= 'A' && grid[x][y] <= 'Z')) {
                    continue;
                }
                String name = null;
                List<Coords> portalPositions = new ArrayList<>();
                Coords outerPosition = null;
                if (y - 1 >= 0 && grid[x][y - 1] >= 'A' && grid[x][y - 1] <= 'Z') {
                    name = "" + grid[x][y - 1] + grid[x][y];
                    portalPositions.add(new Coords(x, y - 1));
                    portalPositions.add(new Coords(x, y));
                } else if (x + 1 < grid.length && grid[x + 1][y] >= 'A' && grid[x + 1][y] <= 'Z') {
                    // We'll see it later on, continue
                    continue;
                } else if (x - 1 >= 0 && grid[x - 1][y] >= 'A' && grid[x - 1][y] <= 'Z') {
                    name = "" + grid[x - 1][y] + grid[x][y];
                    portalPositions.add(new Coords(x - 1, y));
                    portalPositions.add(new Coords(x, y));
                } else if (y + 1 <= grid[0].length && grid[x][y + 1] >= 'A' && grid[x][y + 1] <= 'Z') {
                    // We'll see it later on, continue
                    continue;
                } else {
                    throw new IllegalStateException();
                }

                // Get closest '.' as warp position
                Coords warpPosition = null;
                outer:
                for (Coords position : portalPositions) {
                    for (int dir = 0; dir < 4; dir++) {
                        Coords otherPosition = applyDirection(position, dir);
                        otherPosition.setDistance(0);
                        if (otherPosition.getX() < 0 || otherPosition.getY() < 0 || otherPosition.getX() >= grid.length
                                || otherPosition.getY() >= grid[0].length) {
                            continue;
                        }
                        if (grid[otherPosition.getX()][otherPosition.getY()] == '.') {
                            outerPosition = position;
                            warpPosition = otherPosition;
                            break outer;
                        }
                    }
                }

                Portal portal = new Portal(name);
                if (portals.containsKey(portal)) {
                    portal = portals.get(portal);
                } else {
                    portals.put(portal, portal);
                }
                if (isOuterPortal(grid, outerPosition)) {
                    portal.setOuterPosition(outerPosition);
                    portal.setOuterWarpPosition(warpPosition);
                } else {
                    portal.setInnerPosition(outerPosition);
                    portal.setInnerWarpPosition(warpPosition);
                }
            }
        }

        // Build position -> portal map
        for (Portal portal : portals.keySet()) {
            portalPositions.put(portal.getOuterPosition(), portal);
            portalWarpPositions.put(portal.getOuterWarpPosition(), portal);
            if (portal.getInnerPosition() == null) {
                continue;
            }
            portalPositions.put(portal.getInnerPosition(), portal);
            portalWarpPositions.put(portal.getInnerWarpPosition(), portal);
        }

        // Get start and finish
        Portal start = new Portal("AA");
        start = portals.get(start);
        startPosition = start.getOuterWarpPosition();
        Portal end = new Portal("ZZ");
        end = portals.get(end);
        endPosition = end.getOuterWarpPosition();

        int shortestDistance = getShortestDistance(grid, startPosition, endPosition);
        System.out.println("Shortest distance: " + shortestDistance);
    }

    private static int getShortestDistance(char[][] grid, Coords src, Coords dest) {
        Set<Coords> visited = new HashSet<>();
        visited.add(src);

        Queue<Coords> queue = new LinkedList<>();
        queue.add(src);

        while (!queue.isEmpty()) {
            Coords curr = queue.peek();
            if (curr.equals(dest)) {
                return curr.getDistance();
            }

            queue.remove();
            for (int i = 0; i < 4; i++) {
                Coords adjacent = applyDirection(curr, i);

                if (isValid(grid, adjacent) && !visited.contains(adjacent)) {
                    if (isPositionOnPortal(adjacent)) {
                        Portal portal = getPortalForPosition(adjacent);
                        if (!portal.getName().equals("AA") && !portal.getName().equals("ZZ")) {
                            int distance = adjacent.getDistance();
                            int oldLevel = adjacent.getLevel();
                            Coords portalPosition = null;
                            if (portal.getOuterPosition().getX() == adjacent.getX()
                                    && portal.getOuterPosition().getY() == adjacent.getY()) {
                                adjacent = new Coords(portal.getInnerWarpPosition());
                                adjacent.setLevel(oldLevel - 1);
                                portalPosition = new Coords(portal.getInnerPosition());
                            } else {
                                adjacent = new Coords(portal.getOuterWarpPosition());
                                adjacent.setLevel(oldLevel + 1);
                                portalPosition = new Coords(portal.getOuterPosition());
                            }
                            adjacent.setDistance(distance);

                            // Set portal position as visited so we don't go back the same way we came
                            portalPosition.setLevel(adjacent.getLevel());
                            visited.add(portalPosition);
                        }
                    }
                    visited.add(adjacent);
                    queue.add(adjacent);
                }
            }
        }

        return -1;
    }

    private static boolean isValid(char[][] grid, Coords gridPosition) {
        if (gridPosition.getX() < 0 || gridPosition.getX() >= grid.length || gridPosition.getY() < 0
                || gridPosition.getY() >= grid[0].length) {
            return false;
        }

        // Special case: outer portals only open for specific levels
        if (!isPositionOpen(gridPosition)) {
            return false;
        }

        char gridValue = grid[gridPosition.getX()][gridPosition.getY()];
        if (gridValue == '#') {
            return false;
        }
        if (gridValue == '.') {
            return true;
        }
        if (gridValue >= 'A' && gridValue <= 'Z') {
            if (isPositionOnPortal(gridPosition)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static boolean isPositionOpen(Coords gridPosition) {
        Coords position = new Coords(gridPosition.getX(), gridPosition.getY());
        if (!portalWarpPositions.containsKey(position)) {
            return true;
        }
        Portal portal = portalWarpPositions.get(position);
        if (portal.getName().equals("AA") || portal.getName().equals("ZZ")) {
            return gridPosition.getLevel() == 0;
        }
        if (position.equals(portal.getInnerWarpPosition())) {
            return true;
        }
        return gridPosition.getLevel() != 0;
    }

    private static boolean isPositionOnPortal(Coords gridPosition) {
        Coords position = new Coords(gridPosition.getX(), gridPosition.getY());
        return portalPositions.containsKey(position);
    }

    private static Portal getPortalForPosition(Coords gridPosition) {
        Coords position = new Coords(gridPosition.getX(), gridPosition.getY());
        return portalPositions.get(position);
    }

    private static boolean isOuterPortal(char[][] grid, Coords position) {
        return position.getY() == 0 || position.getY() == 1 || position.getY() == grid[0].length - 1
                || position.getY() == grid[0].length - 2 || position.getX() == 0 || position.getX() == 1
                || position.getX() == grid.length - 1 || position.getX() == grid.length - 2;
    }

    private static final int[] dx = { 0, 1, 0, -1 };
    private static final int[] dy = { 1, 0, -1, 0 };

    private static Coords applyDirection(Coords position, int dir) {
        if (dir < 0 || dir > 3) {
            throw new IllegalStateException();
        }
        return new Coords(position.getX() + dx[dir], position.getY() + dy[dir], position.getDistance() + 1,
                position.getLevel());
    }
}
