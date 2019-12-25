package aoc25;

import static java.util.stream.Collectors.joining;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Cryostasis {

    private static final boolean AUTO = true;

    private static final List<String> BAD_ITEMS =
            Arrays.asList("infinite loop", "photons", "giant electromagnet", "molten lava", "escape pod");

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Cryostasis.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        Queue<Long> inputQueue = new LinkedList<>();
        Queue<Long> outputQueue = new LinkedList<>();
        IntcodeComputer computer = new IntcodeComputer(opcodeArray, inputQueue, outputQueue);
        computer.setHaltOnInput(!AUTO);

        Room currentRoom = new Room(new ArrayList<>());
        Set<Room> visited = new HashSet<>();
        Queue<Room> toVisit = new LinkedList<>();
        toVisit.add(currentRoom);

        Room securityCheckpoint = null;
        Direction securityDirection = null;
        Set<String> items = new HashSet<>();

        // Run computer once to go to starting room
        computer.run();

        // Traverse with BFS
        while (!computer.isHalt()) {
            if (!AUTO) {
                computer.run();
                continue;
            }

            while (!toVisit.isEmpty()) {
                // Go to next room to visit
                currentRoom = toVisit.poll();
                visited.add(currentRoom);
                for (Direction direction : currentRoom.getPath()) {
                    outputQueue.clear();
                    runCommand(computer, direction.name());
                }

                // Get output
                StringBuilder sb = new StringBuilder();
                while (!outputQueue.isEmpty()) {
                    char output = (char) outputQueue.poll().intValue();
                    sb.append(output);
                }
                String text = sb.toString();

                // Parse directions and items and grab all items
                Set<Direction> directions = getDirections(text);
                Set<String> itemsInRoom = getItems(text);
                grabAllItems(computer, itemsInRoom, items);

                if (text.contains("Security Checkpoint")) {
                    securityCheckpoint = currentRoom;
                }

                if (currentRoom.equals(securityCheckpoint)) {
                    // Remove direction we came from and save other one for later on
                    directions.remove(currentRoom.getPath().get(currentRoom.getPath().size() - 1).invert());
                    securityDirection = directions.iterator().next();

                } else {
                    // Add new rooms we need to visit
                    for (Direction direction : directions) {
                        List<Direction> pathToRoom = new ArrayList<>(currentRoom.getPath());
                        pathToRoom.add(direction);
                        // If direction is invert of previous direction, simplify by removing both
                        if (pathToRoom.size() >= 2 && pathToRoom.get(pathToRoom.size() - 1) == pathToRoom
                                .get(pathToRoom.size() - 2).invert()) {
                            pathToRoom.remove(pathToRoom.size() - 1);
                            pathToRoom.remove(pathToRoom.size() - 1);
                        }
                        Room newRoom = new Room(pathToRoom);
                        if (!visited.contains(newRoom)) {
                            toVisit.add(newRoom);
                        }
                    }
                }

                // Go back to start
                for (Direction direction : invertDirections(currentRoom.getPath())) {
                    runCommand(computer, direction.name());
                }
            }

            // Go to security checkpoint
            for (Direction direction : securityCheckpoint.getPath()) {
                runCommand(computer, direction.name());
            }

            // Drop all items
            for (String item : items) {
                runCommand(computer, "drop " + item);
            }

            // Try all item combinations to find right one
            List<String> itemList = new ArrayList<>(items);
            for (int combination = 0; combination < Math.pow(2, itemList.size()); combination++) {
                // Build current combination
                List<String> currentItems = new ArrayList<>();
                for (int itemIndex = 0; itemIndex < 8; itemIndex++) {
                    int enabled = combination >> itemIndex & 1;
                    if (enabled == 1) {
                        currentItems.add(itemList.get(itemIndex));
                    }
                }

                // Pick up items
                for (String item : currentItems) {
                    runCommand(computer, "take " + item);
                }

                // Try combination
                runCommand(computer, securityDirection.name());
                if (computer.isHalt()) {
                    String itemString = currentItems.stream().map(Object::toString).collect(joining(", "));
                    System.out.println("Successful item combination: " + itemString);
                    break;
                }

                // Drop all items again
                for (String item : currentItems) {
                    runCommand(computer, "drop " + item);
                }
            }
        }
    }

    public static void grabAllItems(IntcodeComputer computer, Set<String> items, Set<String> allItems) {
        for (String item : items) {
            if (BAD_ITEMS.contains(item)) {
                continue;
            }
            runCommand(computer, "take " + item);
            allItems.add(item);
        }
    }

    public static void runCommand(IntcodeComputer computer, String command) {
        for (Character character : command.toCharArray()) {
            computer.getInputQueue().add((long) character);
        }
        if (!command.endsWith("\n")) {
            computer.getInputQueue().add((long) '\n');
        }
        System.err.println(command);
        computer.run();
    }

    public static Set<Direction> getDirections(String text) {
        Set<Direction> result = new HashSet<>();
        for (Direction direction : Direction.values()) {
            if (text.contains(direction.name())) {
                result.add(direction);
            }
        }
        return result;
    }

    public static Set<String> getItems(String text) {
        final String itemsHereString = "Items here";
        int itemsHereIndex = text.indexOf(itemsHereString);
        if (itemsHereIndex == -1) {
            return Collections.emptySet();
        }
        Set<String> result = new HashSet<>();
        String itemsText = text.substring(itemsHereIndex);
        String[] lines = itemsText.split("\n");
        for (String line : lines) {
            if (!line.startsWith("-")) {
                continue;
            }
            String item = line.substring(2);
            result.add(item);
        }
        return result;
    }

    public static List<Direction> invertDirections(List<Direction> directions) {
        List<Direction> result = new ArrayList<>();
        for (Direction direction : directions) {
            result.add(direction.invert());
        }
        Collections.reverse(result);
        return result;
    }
}
