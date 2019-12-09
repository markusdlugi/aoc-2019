package aoc1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FuelRequirementsTest {

    @Test
    public void testRecursive() {
        assertEquals(2, FuelRequirements.calculateFuelRecursive(14));
        assertEquals(966, FuelRequirements.calculateFuelRecursive(1969));
        assertEquals(50346, FuelRequirements.calculateFuelRecursive(100756));
    }
}
