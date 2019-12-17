package aoc17;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VacuumRobot {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(VacuumRobot.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        IntcodeComputer computer = new IntcodeComputer(opcodeArray, true);
        computer.setWaitForFirstInput(true);

        boolean inputReceived = false;
        String text = "";
        Set<Coords> scaffold = new HashSet<>();
        Coords startingPoint = null;
        int currentX = 0;
        int currentY = 0;
        while (!computer.isHalt()) {
            long output = computer.run();
            if (output <= 255) {
                char character = (char) output;
                if (character == '\n') {
                    currentY++;
                    currentX = 0;
                } else {
                    currentX++;
                }

                if (character == '#') {
                    scaffold.add(new Coords(currentX, currentY));
                } else if (character == '^') {
                    startingPoint = new Coords(currentX, currentY);
                    scaffold.add(startingPoint);
                }

                System.out.print(character);
                if (!inputReceived) {
                    text += character;
                }
            }

            if (!inputReceived && computer.isFirstInput()) {
                inputReceived = true;
                List<Command> path = calculatePath(scaffold, startingPoint);
                List<Integer> inputList = calculateInputList(path);
                computer.setInputList(inputList);
            }
        }

        int index = 0;
        int sum = 0;
        int lineLength = text.indexOf('\n') + 1;
        while (true) {
            index = text.indexOf("###", index + 1);
            if (index == -1) {
                break;
            }
            int intersectionMiddle = index + 1;
            int top = intersectionMiddle - lineLength;
            int bottom = intersectionMiddle + lineLength;
            if (top < 0 || bottom < 0 || text.charAt(top) != '#' || text.charAt(bottom) != '#') {
                continue;
            }
            int x = intersectionMiddle % lineLength;
            int y = intersectionMiddle / lineLength;
            sum += x * y;
        }

        System.out.println();
        System.out.println("Sum of alignment parameters: " + sum);
        System.out.println();
        System.out.println("Dust: " + computer.getOutput());
    }

    private static List<Command> calculatePath(Set<Coords> scaffold, Coords startingPoint) {
        Coords currentPosition = startingPoint;
        int direction = 0; // Up

        Set<Coords> visited = new HashSet<>();
        visited.add(startingPoint);
        List<Command> resultList = new ArrayList<>();
        while (visited.size() < scaffold.size()) {
            // First try to go straight
            Coords target = applyDirection(currentPosition, direction);
            if (scaffold.contains(target)) {
                resultList.get(resultList.size() - 1).increaseSteps();
                currentPosition = target;
                visited.add(target);
                continue;
            }

            // Otherwise try L
            direction = (4 + direction - 1) % 4;
            target = applyDirection(currentPosition, direction);
            if (scaffold.contains(target)) {
                Command left = new Command(Command.Direction.L, 1);
                resultList.add(left);
                currentPosition = target;
                visited.add(target);
                continue;
            }

            // Otherwise try R
            direction = (4 + direction + 2) % 4;
            target = applyDirection(currentPosition, direction);
            if (scaffold.contains(target)) {
                Command right = new Command(Command.Direction.R, 1);
                resultList.add(right);
                currentPosition = target;
                visited.add(target);
                continue;
            }

            // This shouldn't happen
            throw new IllegalStateException("Unable to find path from coord " + currentPosition);
        }

        return resultList;
    }

    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {-1, 0, 1, 0};

    private static Coords applyDirection(Coords currentPosition, int dir) {
        if(dir < 0 || dir > 3) {
            throw new IllegalStateException();
        }
        return new Coords(currentPosition.getX() + dx[dir], currentPosition.getY() + dy[dir]);
    }

    private static List<Integer> calculateInputList(List<Command> path) {
        int[] groupSizes = { 1, 1, 1 };
        List<Command>[] groups = new List[3];
        boolean foundSolution = false;
        boolean moreGroups = true;
        while (moreGroups) {
            foundSolution = checkGroupCombination(path, groups, groupSizes);
            if (foundSolution) {
                break;
            }
            moreGroups = changeGroupSizes(groupSizes);
        }

        if(!foundSolution) {
            System.out.println("No solution found, sorry :(");
            System.exit(0);
        }

        List<Integer> resultList = new ArrayList<>();
        List<String> mainFunction = new ArrayList<>();
        int currentGroupRemaining = 0;
        for(Command command : path) {
            if(currentGroupRemaining > 0) {
                currentGroupRemaining--;
                continue;
            }
            switch(command.getGroup()) {
            case 0:
                mainFunction.add("A");
                break;
            case 1:
                mainFunction.add("B");
                break;
            case 2:
                mainFunction.add("C");
                break;
            default:
                throw new IllegalStateException();
            }
            currentGroupRemaining = groupSizes[command.getGroup()] - 1;
        }

        // Calculate input list for main function
        String mainFunctionString = mainFunction.stream().map(Object::toString).collect(joining(","));
        for(char character : mainFunctionString.toCharArray()) {
            resultList.add((int) character);
        }
        resultList.add((int) '\n');

        // Calculate input list for groups
        for(int group = 0; group < groups.length; group++) {
            String groupString = groups[group].stream().map(Object::toString).collect(joining(","));
            for(char character : groupString.toCharArray()) {
                resultList.add((int) character);
            }
            resultList.add((int) '\n');
        }

        // Add input for video stream
        resultList.add((int) 'n');
        resultList.add((int) '\n');

        return resultList;
    }

    private static boolean checkGroupCombination(List<Command> path, List<Command>[] groups, int[] groupSizes) {
        for (Command command : path) {
            command.setGroup(null);
        }

        for (int group = 0; group < groupSizes.length; group++) {
            int groupSize = groupSizes[group];
            List<Command> commandList = new ArrayList<>();
            groups[group] = commandList;

            // Build group
            for (Command command : path) {
                if (commandList.size() == groupSize) {
                    break;
                }
                if (command.getGroup() != null) {
                    if(commandList.size() == 0) {
                        continue;
                    }
                    return false;
                }
                commandList.add(command);
                command.setGroup(group);
            }

            // Mark group everywhere
            List<Integer> indicesOfGroup = findSubListInList(path, commandList);
            for(Integer indexOfGroup : indicesOfGroup) {
                for(int i = indexOfGroup; i < indexOfGroup + groupSize && i < path.size(); i++) {
                    path.get(i).setGroup(group);
                }
            }
        }

        // Check if entire path is part of the 3 groups
        for(Command command : path) {
            if(command.getGroup() == null) {
                return false;
            }
        }
        return true;
    }

    private static boolean changeGroupSizes(int[] groupSizes) {
        final int maxGroupSize = 7;

        groupSizes[2]++;
        if (groupSizes[2] > maxGroupSize) {
            groupSizes[2] = 1;
            groupSizes[1]++;
        }
        if (groupSizes[1] > maxGroupSize) {
            groupSizes[1] = 1;
            groupSizes[0]++;
        }
        if(groupSizes[0] > maxGroupSize) {
            return false;
        }
        return true;
    }

    private static List<Integer> findSubListInList(List<?> source, List<?> target) {
        List<?> sourceCopy = new ArrayList<>(source);
        List<Integer> indices = new ArrayList<>();
        int index = 0;
        int removedElements = 0;
        while(true) {
            index = Collections.indexOfSubList(sourceCopy, target);
            if (index == -1) {
                break;
            }
            int result = index + removedElements;
            indices.add(result);
            for (int i = 0; i < index + target.size(); i++) {
                sourceCopy.remove(0);
                removedElements++;
            }
        }
        return indices;
    }
}
