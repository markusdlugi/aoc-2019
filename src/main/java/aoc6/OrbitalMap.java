package aoc6;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class OrbitalMap {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(OrbitalMap.class.getResource("input.txt").toURI()));

        // All orbits in one map to have single object for each of them
        HashMap<String, Orbit> orbitMap = new HashMap<>();
        for (String line : lines) {
            String[] components = line.split("\\)");

            // Create orbit
            Orbit orbit = orbitMap.get(components[0]);
            if (orbit == null) {
                orbit = new Orbit(components[0]);
                orbitMap.put(components[0], orbit);
            }

            // Create child (orbitee?)
            Orbit orbitee = orbitMap.get(components[1]);
            if (orbitee == null) {
                orbitee = new Orbit(components[1]);
                orbitMap.put(components[1], orbitee);
            }

            orbit.addChild(orbitee);
            orbitee.setParent(orbit);
        }

        // Part A

        // Count distance for each object to COM
        int totalOrbits = 0;
        for (Orbit orbit : orbitMap.values()) {
            // null is parent of COM, so we just count to null and subtract 1
            totalOrbits += orbit.countDistanceToParent(null) - 1;
        }
        System.out.println("Total number of orbits: " + totalOrbits);

        // Part B

        // Get YOU and SAN
        Orbit you = orbitMap.get("YOU");
        Orbit san = orbitMap.get("SAN");

        // Get your parents
        Orbit yourParent = you.getParent();
        Orbit sanParent = san.getParent();

        // Get each one's entire parent list
        List<Orbit> yourParentList = yourParent.getAllParents();
        List<Orbit> sanParentList = sanParent.getAllParents();

        // Find lowest common parent
        Orbit commonParent = null;
        for (Orbit currentParent : yourParentList) {
            if (sanParentList.contains(currentParent)) {
                commonParent = currentParent;
                break;
            }
        }

        int distanceYourParent = yourParent.countDistanceToParent(commonParent);
        int distanceSanParent = sanParent.countDistanceToParent(commonParent);
        int orbitalTransfers = distanceYourParent + distanceSanParent;

        System.out.println("Minimum orbital transfers: " + orbitalTransfers);
    }
}
