package aoc1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FuelRequirements {

    private static final String PATH = "C:/dev/workspace/aoc/src/main/resources/aoc1/";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH + "input.txt"));
        long sum = 0;
        for(String module : lines) {
            sum += calculateFuelRecursive(Long.parseLong(module));
        }
        System.out.println("Required fuel is " + sum);
    }

    public static long calculateFuel(long mass) {
        return (long) Math.floor(mass/3.0) - 2;
    }

    public static long calculateFuelRecursive(long mass) {
        long fuel = calculateFuel(mass);
        if(fuel <= 0) {
            return 0;
        }
        return fuel + calculateFuelRecursive(fuel);
    }
}
