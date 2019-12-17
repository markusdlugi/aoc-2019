package aoc10;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AsteroidStation {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(AsteroidStation.class.getResource("input.txt").toURI()));

        HashMap<Asteroid, HashSet<Asteroid>> asteroidMap = new HashMap<>();

        int y = 0;
        for (String line : lines) {
            String[] characters = line.split("");
            for (int x = 0; x < characters.length; x++) {
                String character = characters[x];
                if (character.equals("#")) {
                    Asteroid coords = new Asteroid(x, y);
                    asteroidMap.put(coords, new HashSet<>());
                }
            }
            y++;
        }

        // Part A
        int highestCount = 0;
        Asteroid highestCountCoords = null;
        for (Asteroid coords : asteroidMap.keySet()) {
            int visibleCount = calculateVisibleAsteroids(coords, asteroidMap);
            if (visibleCount > highestCount) {
                highestCount = visibleCount;
                highestCountCoords = coords;
            }
        }

        System.out.println("Best spot for the station: " + highestCountCoords + " with count " + highestCount);
        System.out.println();

        // Part B
        vaporizeAsteroids(highestCountCoords, asteroidMap);
    }

    private static int calculateVisibleAsteroids(Asteroid coords, HashMap<Asteroid, HashSet<Asteroid>> asteroidMap) {
        int result = 0;
        HashSet<Asteroid> visibleAsteroids = asteroidMap.get(coords);
        for (Asteroid asteroid : asteroidMap.keySet()) {
            if (coords.equals(asteroid)) {
                continue;
            }
            if (isVisible(coords, asteroid, asteroidMap)) {
                visibleAsteroids.add(asteroid);
                result++;
            }
        }
        return result;
    }

    private static boolean isVisible(Asteroid source, Asteroid target,
            HashMap<Asteroid, HashSet<Asteroid>> asteroidMap) {
        // This is a mess. Oh well.
        int xDiff = target.getX() - source.getX();
        int yDiff = target.getY() - source.getY();

        double normalizedX;
        double normalizedY;
        if (xDiff == 0 || yDiff == 0) {
            if (xDiff == 0) {
                normalizedX = 0;
                normalizedY = Math.signum(yDiff);
            } else {
                normalizedX = (int) Math.signum(xDiff);
                normalizedY = 0;
            }
        } else {
            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                normalizedX = (int) Math.signum(xDiff);
                normalizedY = yDiff / (double) Math.abs(xDiff);
                while ((int) normalizedY != normalizedY) {
                    normalizedX += (normalizedX > 0) ? 1 : -1;
                    normalizedY = yDiff / Math.abs(xDiff / normalizedX);
                }
            } else {
                normalizedY = (int) Math.signum(yDiff);
                normalizedX = xDiff / (double) Math.abs(yDiff);
                while ((int) normalizedX != normalizedX) {
                    normalizedY += (normalizedY > 0) ? 1 : -1;
                    normalizedX = xDiff / Math.abs(yDiff / normalizedY);
                }
            }
        }

        HashSet<Asteroid> line = new HashSet<>();
        int currentX = source.getX() + (int) normalizedX;
        int currentY = source.getY() + (int) normalizedY;
        while (currentX != target.getX() || currentY != target.getY()) {
            line.add(new Asteroid(currentX, currentY));
            currentX += normalizedX;
            currentY += normalizedY;
        }

        for (Asteroid lineCoord : line) {
            if (asteroidMap.containsKey(lineCoord)) {
                return false;
            }
        }
        return true;
    }

    private static void vaporizeAsteroids(Asteroid station, HashMap<Asteroid, HashSet<Asteroid>> asteroidMap) {
        // Calculate all angles and distances
        for (Asteroid asteroid : asteroidMap.keySet()) {
            asteroid.setAngle(calculateAngle(station, asteroid));
            asteroid.setDistance(calculateDistance(station, asteroid));
        }

        // Sort by angle & distance
        List<Asteroid> sortedAsteroids = new ArrayList<>(asteroidMap.keySet());
        sortedAsteroids.remove(station);
        Collections.sort(sortedAsteroids, new Asteroid.AsteroidComparator());

        // Vaporize first 200
        Asteroid resultAsteroid = null;
        int vaporized = 0;
        double previousAngle = -1;
        while (!sortedAsteroids.isEmpty() && vaporized < 200) {
            Set<Asteroid> vaporizedAsteroids = new HashSet<>();
            for (Asteroid asteroid : sortedAsteroids) {
                // Only vaporize closest one at same angle, skip others for now
                if (Math.abs(previousAngle - asteroid.getAngle()) < 0.0001) {
                    continue;
                }
                vaporized++;
                System.out.println((vaporized) + ": Vaporizing " + asteroid + " at angle " + asteroid.getAngle());
                asteroidMap.remove(asteroid);
                previousAngle = asteroid.getAngle();

                if (vaporized == 200) {
                    resultAsteroid = asteroid;
                    break;
                }
            }

            sortedAsteroids.removeAll(vaporizedAsteroids);
        }

        System.out.println();
        System.out.println("200th asteroid: " + resultAsteroid);
    }

    private static double calculateAngle(Asteroid station, Asteroid asteroid) {
        double xDiff = asteroid.getX() - station.getX();
        double yDiff = asteroid.getY() - station.getY();
        double degrees = Math.toDegrees(Math.atan2(yDiff, xDiff));
        // Standard range is -180 to +180, make positive and shift by 90 so up is 0
        degrees += 90;
        if (degrees < 0) {
            degrees += 360;
        }
        return degrees;
    }

    private static double calculateDistance(Asteroid station, Asteroid asteroid) {
        double xDiff = asteroid.getX() - station.getX();
        double yDiff = asteroid.getY() - station.getY();
        return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
    }
}
