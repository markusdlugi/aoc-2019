package aoc7;

import java.util.Iterator;

public class Permute<T> implements Iterator<T[]> {

    private T[] array;
    private int[] indexes;
    private int i;
    private boolean firstNumber = true;

    public Permute(T[] array) {
        this.array = array;
        this.indexes = new int[array.length];
    }

    @Override
    public boolean hasNext() {
        return i < array.length;
    }

    @Override
    public T[] next() {
        if (!hasNext()) {
            return null;
        }
        // Return first number without permutation
        if (firstNumber) {
            firstNumber = false;
            return array;
        }
        // Permute and return
        permute();
        return array;
    }

    private void permute() {
        resetState();
        if (indexes[i] < i) {
            swap(i % 2 == 0 ? 0 : indexes[i], i);
            indexes[i]++;
            i = 0;
        }
        resetState();
    }

    private void resetState() {
        while (i < array.length && indexes[i] >= i) {
            indexes[i] = 0;
            i++;
        }
    }

    private void swap(int a, int b) {
        T temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}
