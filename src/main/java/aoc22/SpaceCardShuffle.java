package aoc22;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpaceCardShuffle {

    private static final BigInteger DECK_SIZE = BigInteger.valueOf(119315717514047L);
    private static final BigInteger REPETITION_COUNT = BigInteger.valueOf(101741582076661L);
    private static final List<ShuffleOperation> operations = new ArrayList<>(100);

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(SpaceCardShuffle.class.getResource("input.txt").toURI()));

        // Part A
        int[] cards = new int[10007];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = i;
        }

        for (String line : lines) {
            if (line.startsWith("cut")) {
                int amount = Integer.parseInt(line.split(" ")[1]);
                cut(cards, amount);
            } else if (line.startsWith("deal with increment")) {
                String[] split = line.split(" ");
                int inc = Integer.parseInt(split[split.length - 1]);
                dealWithIncrement(cards, inc);
            } else {
                dealIntoNewStack(cards);
            }
        }
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == 2019) {
                System.out.println("Card 2019 is at position: " + i);
            }
        }

        // Part B
        for (String line : lines) {
            if (line.startsWith("cut")) {
                int amount = Integer.parseInt(line.split(" ")[1]);
                operations.add(new ShuffleOperation(ShuffleOperation.Operation.CUT, amount));
            } else if (line.startsWith("deal with increment")) {
                String[] split = line.split(" ");
                int inc = Integer.parseInt(split[split.length - 1]);
                operations.add(new ShuffleOperation(ShuffleOperation.Operation.INCREMENT, inc));
            } else {
                operations.add(new ShuffleOperation(ShuffleOperation.Operation.NEW_STACK));
            }
        }

        Collections.reverse(operations);

        // Get values of first 3 shuffle iterations
        BigInteger x = BigInteger.valueOf(2020);
        BigInteger y = applyOperations(x);
        BigInteger z = applyOperations(y);

        // Derive a and b from the results
        // y = a * x + b
        // z = a * y + b
        // Subtract equation 2 from equation 1:
        // y - z = a * x - a * y = a * (x - y)
        // a = (y - z) / (x - y)
        // b = y - a * x
        // Modular division is multiplication with modInverse
        BigInteger a = y.subtract(z).multiply(x.subtract(y).modInverse(DECK_SIZE)).mod(DECK_SIZE);
        BigInteger b = y.subtract(a.multiply(x)).mod(DECK_SIZE);

        // ax + b
        // a(ax + b) + b = a²x + ab + b
        // a(a²x + ab + b) + b = a³x + a²b + ab + b
        // => a^n * x + a^(n-1) * b + a^(n-2) * b + ... + a²b + ab + b
        // => a^n * x + b(a^(n-1) + a^(n-2) + ... + a² + a + 1) [Geometric series]
        // => a^n * x + (a^n - 1) / (a - 1) * b

        BigInteger aPowerN = a.modPow(REPETITION_COUNT, DECK_SIZE);
        BigInteger result = aPowerN.multiply(x).add(b.multiply(
                aPowerN.subtract(BigInteger.ONE).multiply(a.subtract(BigInteger.ONE).modInverse(DECK_SIZE))))
                .mod(DECK_SIZE);

        System.out.println("Card at position 2020 is: " + result);
    }

    private static void cut(int[] cards, int amount) {
        int[] cardsCopy = Arrays.copyOf(cards, cards.length);
        for (int i = 0; i < cards.length; i++) {
            int index = (cards.length + i + amount) % cards.length;
            cards[i] = cardsCopy[index];
        }
    }

    private static void dealWithIncrement(int[] cards, int increment) {
        int[] cardsCopy = Arrays.copyOf(cards, cards.length);
        for (int i = 0; i < cards.length; i++) {
            int index = (cards.length + i * increment) % cards.length;
            cards[index] = cardsCopy[i];
        }
    }

    private static void dealIntoNewStack(int[] cards) {
        for (int i = 0; i < cards.length / 2; i++) {
            int temp = cards[i];
            cards[i] = cards[cards.length - 1 - i];
            cards[cards.length - 1 - i] = temp;
        }
    }

    private static BigInteger applyOperations(BigInteger position) {
        BigInteger card = position;
        for (int i = 0; i < operations.size(); i++) {
            ShuffleOperation operation = operations.get(i);
            card = applyOperation(card, operation);
        }
        return card;
    }

    private static BigInteger applyOperation(BigInteger position, ShuffleOperation operation) {
        switch (operation.getOperation()) {
        case NEW_STACK:
            return dealIntoNewStack(position);
        case CUT:
            return cut(position, BigInteger.valueOf(operation.getParameter()));
        case INCREMENT:
            return dealWithIncrement(position, BigInteger.valueOf(operation.getParameter()));
        default:
            throw new IllegalStateException();
        }
    }

    private static BigInteger cut(BigInteger position, BigInteger amount) {
        // (D + position + amount) % D
        return DECK_SIZE.add(position).add(amount).mod(DECK_SIZE);
    }

    private static BigInteger dealWithIncrement(BigInteger position, BigInteger increment) {
        // position * increment % D -> position / increment % D -> position * modinv(inc) % D
        BigInteger modInverse = increment.modInverse(DECK_SIZE);
        return position.multiply(modInverse).mod(DECK_SIZE);
    }

    private static BigInteger dealIntoNewStack(BigInteger position) {
        // D - 1 - position
        return DECK_SIZE.subtract(BigInteger.ONE).subtract(position);
    }
}
