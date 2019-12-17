package aoc1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FuelRequirements {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(FuelRequirements.class.getResource("input.txt").toURI()));
        long sum = 0;
        for (String module : lines) {
            sum += calculateFuelRecursive(Long.parseLong(module));
        }
        System.out.println("Required fuel is " + sum);
    }

    public static long calculateFuel(long mass) {
        return (long) Math.floor(mass / 3.0) - 2;
    }

    public static long calculateFuelRecursive(long mass) {
        long fuel = calculateFuel(mass);
        if (fuel <= 0) {
            return 0;
        }
        return fuel + calculateFuelRecursive(fuel);
    }
}
