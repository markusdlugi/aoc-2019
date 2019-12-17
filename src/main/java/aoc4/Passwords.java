package aoc4;

public class Passwords {

    private static final int MIN_PASS = 271973;
    private static final int MAX_PASS = 785961;

    public static void main(String[] args) {
        int passwords = 0;
        for (Integer currentPassword = MIN_PASS; currentPassword <= MAX_PASS; currentPassword++) {
            int previousDigit = Integer.MIN_VALUE;
            boolean onlyIncreasing = true;
            boolean hasDouble = false;
            int repeatingChars = 0;
            for (int ind = 0; ind < 6; ind++) {
                int currentDigit = Integer.parseInt(currentPassword.toString().substring(ind, ind + 1));

                if (previousDigit > currentDigit) {
                    onlyIncreasing = false;
                    break;
                }

                if (previousDigit == currentDigit) {
                    repeatingChars++;
                    if (ind == 5 && repeatingChars == 1) {
                        hasDouble = true;
                    }
                } else {
                    if (repeatingChars == 1) {
                        hasDouble = true;
                    }
                    repeatingChars = 0;
                }

                previousDigit = currentDigit;
            }

            if (!onlyIncreasing || !hasDouble) {
                continue;
            }
            System.out.println("Password: " + currentPassword);
            passwords++;
        }
        System.out.println("Number: " + passwords);
    }
}
