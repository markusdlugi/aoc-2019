package aoc8;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessing {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(ImageProcessing.class.getResource("input.txt").toURI()));

        String[] pixels = lines.get(0).split("");

        int imageWidth = 25;
        int imageHeight = 6;
        int size = imageWidth * imageHeight;
        int layerCount = pixels.length / size;

        List<int[][]> layers = new ArrayList<>();
        int[] zeroCount = new int[layerCount];
        int[] oneCount = new int[layerCount];
        int[] twoCount = new int[layerCount];

        // Read input to layers
        for (int i = 0; i < pixels.length; i++) {
            int layerNumber = i / size;
            int x = i % imageWidth;
            int y = (i - layerNumber * size) / imageWidth;

            int[][] layer;
            if (layers.size() <= layerNumber) {
                layer = new int[imageWidth][imageHeight];
                layers.add(layer);
            } else {
                layer = layers.get(layerNumber);
            }

            layer[x][y] = Integer.parseInt(pixels[i]);

            if (layer[x][y] == 0) {
                zeroCount[layerNumber]++;
            } else if (layer[x][y] == 1) {
                oneCount[layerNumber]++;
            } else if (layer[x][y] == 2) {
                twoCount[layerNumber]++;
            }
        }

        //Print layers
        for (int layerNo = 0; layerNo < layers.size(); layerNo++) {
            System.out.println("Layer " + layerNo + ":");
            int[][] layer = layers.get(layerNo);
            for (int y = 0; y < layer[0].length; y++) {
                for (int x = 0; x < layer.length; x++) {
                    System.out.print(layer[x][y]);
                }
                System.out.println();
            }
        }

        // Calculate Part A
        int lowestZeroes = Integer.MAX_VALUE;
        int lowestZeroLayer = 0;
        for (int i = 0; i < zeroCount.length; i++) {
            if (zeroCount[i] < lowestZeroes) {
                lowestZeroes = zeroCount[i];
                lowestZeroLayer = i;
            }
        }

        System.out.println();
        System.out.println("Number: " + oneCount[lowestZeroLayer] * twoCount[lowestZeroLayer]);
        System.out.println();

        // Part B
        Integer[][] image = new Integer[imageWidth][imageHeight];

        // Compute pixels in image
        for (int layerNo = 0; layerNo < layers.size(); layerNo++) {
            int[][] layer = layers.get(layerNo);
            for (int x = 0; x < layer.length; x++) {
                for (int y = 0; y < layer[x].length; y++) {
                    if (image[x][y] != null) {
                        continue;
                    }
                    if (layer[x][y] == 2) {
                        continue;
                    }
                    image[x][y] = layer[x][y];
                }
            }
        }

        // Print image
        for (int y = 0; y < image[0].length; y++) {
            for (int x = 0; x < image.length; x++) {
                int color = image[x][y];
                if (color == 1) {
                    System.out.print("\u2588");
                } else {
                    System.out.print("\u2591");
                }
            }
            System.out.println();
        }
    }
}
