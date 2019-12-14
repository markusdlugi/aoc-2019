package aoc14;

import java.util.List;
import java.util.stream.Collectors;

public class Reaction {

    private List<Chemical> inputChemicals;
    private Chemical outputChemical;

    public Reaction(final List<Chemical> inputChemicals, final Chemical outputChemical) {
        this.inputChemicals = inputChemicals;
        this.outputChemical = outputChemical;
    }

    public List<Chemical> getInputChemicals() {
        return inputChemicals;
    }

    public Chemical getOutputChemical() {
        return outputChemical;
    }

    public String toString() {
        String inputString = inputChemicals.stream().map(c -> c.toString()).collect(Collectors.joining(", "));
        return inputString + " => " + outputChemical;
    }
}
