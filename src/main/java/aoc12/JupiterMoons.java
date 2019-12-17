package aoc12;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JupiterMoons {

    private static final String MOON_REGEX = "<x=([-]?[0-9]*), y=([-]?[0-9]*), z=([-]?[0-9]*)>";

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(JupiterMoons.class.getResource("input.txt").toURI()));
        Pattern pattern = Pattern.compile(MOON_REGEX);
        List<Moon> initialMoons = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            Moon moon = new Moon(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)));
            initialMoons.add(moon);
        }

        for (Moon moon : initialMoons) {
            System.out.println(moon);
        }
        System.out.println();

        // Part A
        List<Moon> moons = new ArrayList<>();
        for (Moon moon : initialMoons) {
            moons.add(new Moon(moon.getX(), moon.getY(), moon.getZ()));
        }
        for (int i = 0; i < 1000; i++) {
            applyGravity(moons);
            applyVelocity(moons);

            System.out.println("After " + i + " steps:");
            for (Moon moon : moons) {
                System.out.println(moon);
            }
            System.out.println();
        }

        int totalEnergy = 0;
        for (Moon moon : moons) {
            totalEnergy += moon.getTotalEnergy();
        }
        System.out.println("Total Energy: " + totalEnergy);
        System.out.println();

        // Part B
        moons.clear();
        for (Moon moon : initialMoons) {
            moons.add(new Moon(moon.getX(), moon.getY(), moon.getZ()));
        }

        int steps = 0;
        long[] loops = new long[3];
        while (loops[0] == 0 || loops[1] == 0 || loops[2] == 0) {
            applyGravity(moons);
            applyVelocity(moons);
            steps++;

            for (int dimension = 0; dimension < 3; dimension++) {
                if (loops[dimension] == 0 && hasDimensionLooped(moons, initialMoons, dimension)) {
                    loops[dimension] = steps;
                }
            }
        }

        System.out.println("Loops: x after " + loops[0] + ", y after " + loops[1] + ", z after " + loops[2]);

        Long loop = lcm(loops);
        System.out.println("All dimensions loop after " + loop);
    }

    private static void applyGravity(List<Moon> moons) {
        List<Moon> alreadyChanged = new ArrayList<>();

        for (Moon moon1 : moons) {
            for (Moon moon2 : moons) {
                if (alreadyChanged.contains(moon2) || moon1.equals(moon2)) {
                    continue;
                }

                for (int dimension = 0; dimension < 3; dimension++) {
                    int diff = moon1.getPosition(dimension).compareTo(moon2.getPosition(dimension));
                    moon1.changeVel(diff * -1, dimension);
                    moon2.changeVel(diff, dimension);
                }
            }
            alreadyChanged.add(moon1);
        }
    }

    private static void applyVelocity(List<Moon> moons) {
        for (Moon moon : moons) {
            moon.applyVelocity();
        }
    }

    private static boolean hasDimensionLooped(List<Moon> moons, List<Moon> initialMoons, int dimension) {
        for (int i = 0; i < moons.size(); i++) {
            Moon moon = moons.get(i);
            Moon initialMoon = initialMoons.get(i);
            if (!moon.getPosition(dimension).equals(initialMoon.getPosition(dimension))
                    || !moon.getVelocity(dimension).equals(initialMoon.getVelocity(dimension))) {
                return false;
            }
        }
        return true;
    }

    public static long gcd(long a, long b) {
        return a == 0 ? b : b == 0 ? a : a > b ? gcd(b, a % b) : gcd(a, b % a);
    }

    public static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    public static long lcm(long[] numbers) {
        long lcm = 1;
        for(long number : numbers) {
            lcm = lcm(lcm, number);
        }
        return lcm;
    }
}
