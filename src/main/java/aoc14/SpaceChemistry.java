package aoc14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpaceChemistry {

    private static final String REACTION_REGEX = "([0-9A-Z, ]*) => ([0-9A-Z ]*)";
    private static final String CHEMICAL_REGEX = "([0-9]*) ([A-Z]*)";

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(SpaceChemistry.class.getResource("input.txt").toURI()));
        Pattern reactionPattern = Pattern.compile(REACTION_REGEX);
        Pattern chemicalPattern = Pattern.compile(CHEMICAL_REGEX);

        // Build map of reactions
        Map<Chemical, Reaction> reactions = new HashMap<>();
        for (String line : lines) {
            Matcher matcher = reactionPattern.matcher(line);
            matcher.matches();

            List<Chemical> inputChemicals = new ArrayList<>();

            String input = matcher.group(1);
            for (String inputChemical : input.split(", ")) {
                Matcher chemicalMatcher = chemicalPattern.matcher(inputChemical);
                chemicalMatcher.matches();
                Chemical chemical = new Chemical(Integer.parseInt(chemicalMatcher.group(1)), chemicalMatcher.group(2));
                inputChemicals.add(chemical);
            }

            Matcher chemicalMatcher = chemicalPattern.matcher(matcher.group(2));
            chemicalMatcher.matches();
            Chemical chemical = new Chemical(Integer.parseInt(chemicalMatcher.group(1)), chemicalMatcher.group(2));
            Reaction reaction = new Reaction(inputChemicals, chemical);
            reactions.put(chemical, reaction);
        }

        // Part A
        long ore = getOreRequiredForChemical(new Chemical(1, "FUEL"), reactions, new HashMap<>());
        System.out.println("Required Ore for 1 Fuel: " + ore);
        System.out.println();

        // Part B
        long oreAmount = 1000000000000L;
        long fuelAmount = 0;
        long lowerBound = oreAmount / ore;
        long upperBound = lowerBound * 2;

        int steps = 0;
        while(lowerBound < upperBound) {
            steps++;
            fuelAmount = (upperBound + lowerBound) / 2;
            ore = getOreRequiredForChemical(new Chemical(fuelAmount, "FUEL"), reactions, new HashMap<>());

            if(ore > oreAmount) {
                upperBound = fuelAmount - 1;
            }
            else {
                lowerBound = fuelAmount + 1;
            }

            System.out.println(ore + " ORE => "+ fuelAmount + " FUEL");
        }

        System.out.println("Steps: " + steps);
        System.out.println("FUEL produced with " + oreAmount + " ORE: " + fuelAmount);
    }

    private static long getOreRequiredForChemical(Chemical chemical, Map<Chemical, Reaction> reactions, Map<Chemical, Long> leftoverChemicals) {
        long requiredAmount = chemical.getAmount();
        if(chemical.getName().equals("ORE")) {
            return requiredAmount;
        }

        Reaction reaction = reactions.get(chemical);

        // Check if we can use leftovers
        long producedAmount = reaction.getOutputChemical().getAmount();
        Long leftoverAmount = leftoverChemicals.get(chemical);
        if(leftoverAmount != null && leftoverAmount != 0) {
            leftoverChemicals.put(chemical, Math.max(leftoverAmount - requiredAmount, 0));
            requiredAmount -= leftoverAmount;
            if(requiredAmount <= 0) {
                return 0;
            }
        }

        long reactionCount = (long) Math.ceil((double) requiredAmount / producedAmount);

        // In case we still have some leftovers, stash them for later usage
        long totallyProducedAmount = producedAmount * reactionCount;
        if(totallyProducedAmount > requiredAmount) {
            leftoverChemicals.put(chemical, totallyProducedAmount - requiredAmount);
        }

        long ore = 0;
        for(Chemical inputChemical : reaction.getInputChemicals()) {
            Chemical inputWithAmount = new Chemical(reactionCount * inputChemical.getAmount(), inputChemical.getName());
            ore += getOreRequiredForChemical(inputWithAmount, reactions, leftoverChemicals);
        }
        return ore;
    }

    private static Map<Chemical, Long> cloneHashMap(Map<Chemical, Long> map) {
        Map<Chemical, Long> result = new HashMap<>();
        for(Map.Entry<Chemical, Long> entry : map.entrySet()) {
            // Ensure we have a new reference to the Long
            result.put(entry.getKey(), Long.valueOf(entry.getValue()));
        }
        return result;
    }
}
