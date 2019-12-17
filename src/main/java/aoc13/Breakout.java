package aoc13;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

/**
 * Controls:
 *
 * A: Left
 * D: Right
 * S: Slower
 * W: Faster
 * M: Toggle Manual / Automatic Mode
 */
public class Breakout {

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Breakout.class.getResource("input.txt").toURI()));

        String[] opcodeArrayString = lines.get(0).split(",");
        long[] opcodeArray = new long[opcodeArrayString.length];

        for (int i = 0; i < opcodeArrayString.length; i++) {
            opcodeArray[i] = Long.parseLong(opcodeArrayString[i]);
        }

        IntcodeComputer computer = new IntcodeComputer(opcodeArray, true);
        BreakoutKeyListener keyListener = new BreakoutKeyListener(computer);

        // Set up window & text area
        JFrame jframe = new JFrame("AOC");
        jframe.setPreferredSize(new Dimension(600, 800));
        jframe.addKeyListener(keyListener);
        jframe.setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        textArea.setEditable(false);
        textArea.addKeyListener(keyListener);
        textArea.setVisible(true);
        jframe.getContentPane().add(BorderLayout.WEST, textArea);
        jframe.pack();
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.toFront();
        jframe.requestFocus();

        Map<Coords, Coords> tiles = new HashMap<>();
        long score = 0;
        int blocks = 0;
        int input = 0;

        int ballX = 0;
        int paddleX = 0;

        while (!computer.isHalt()) {
            if (keyListener.isManualMode()) {
                if (computer.getInput1() == null) {
                    computer.setInput1(keyListener.getInput());
                }
            } else {
                computer.setInput1(input);
            }
            long x = computer.run();
            long y = computer.run();
            long tileId = computer.run();

            if (x == -1 && y == 0) {
                if (score < tileId) {
                    score = tileId;
                }
                if (tileId != 0) {
                    blocks++;
                }
                continue;
            }

            switch ((int) tileId) {
            case 0:
                tiles.remove(new Coords((int) x, (int) y));
                break;
            case 1:
                // Wall
                tiles.put(new Wall((int) x, (int) y), new Wall((int) x, (int) y));
                break;
            case 2:
                // Block
                tiles.put(new Block((int) x, (int) y), new Block((int) x, (int) y));
                break;
            case 3:
                // Paddle
                tiles.put(new Paddle((int) x, (int) y), new Paddle((int) x, (int) y));
                paddleX = (int) x;
                break;
            case 4:
                // Ball
                tiles.put(new Ball((int) x, (int) y), new Ball((int) x, (int) y));
                ballX = (int) x;
                break;
            default:
                throw new IllegalStateException();
            }

            if (!computer.isFirstInput()) {
                continue;
            }

            if (ballX > paddleX) {
                input = 1;
            } else if (ballX < paddleX) {
                input = -1;
            } else {
                input = 0;
            }

            // Find max coords for printing
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (Coords panel : tiles.keySet()) {
                minX = Integer.min(minX, panel.getX());
                minY = Integer.min(minY, panel.getY());
                maxX = Integer.max(maxX, panel.getX());
                maxY = Integer.max(maxY, panel.getY());
            }

            System.out.print("");
            StringBuffer buffer = new StringBuffer();
            for (y = minY; y <= maxY; y++) {
                for (x = minX; x <= maxX; x++) {
                    Coords coord = new Coords((int) x, (int) y);
                    Coords tile = (tiles.containsKey(coord)) ? tiles.get(coord) : null;
                    if (tile == null) {
                        buffer.append(" ");
                    } else if (tile instanceof Wall) {
                        buffer.append("*");
                    } else if (tile instanceof Block) {
                        buffer.append("#");
                    } else if (tile instanceof Paddle) {
                        buffer.append("_");
                    } else if (tile instanceof Ball) {
                        buffer.append("O");
                    }
                }
                if (y == 1) {
                    buffer.append(" SCORE");
                } else if (y == 2) {
                    buffer.append(" " + score);
                }
                buffer.append("\n");
            }
            textArea.setText(buffer.toString());

            Thread.sleep(keyListener.getSpeed());
        }
        System.out.println("");
        System.out.println("BLOCKS DESTROYED: " + blocks);
        System.out.println("FINAL SCORE: " + score);
    }
}
