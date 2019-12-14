package aoc13;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BreakoutKeyListener implements KeyListener {

    private IntcodeComputer computer;
    private int input;
    private int speed = 120;
    private boolean manualMode = true;

    public BreakoutKeyListener(IntcodeComputer computer) {
        this.computer = computer;
    }

    public int getInput() {
        return this.input;
    }

    public int getSpeed() {
        return this.speed;
    }

    public boolean isManualMode() {
        return this.manualMode;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
            input = -1;
        } else if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
            input = 1;
        } else if ((e.getKeyChar() == 'w' || e.getKeyChar() == 'W')) {
            speed -= 20;
            if (speed < 0) {
                speed = 0;
            }
        } else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
            speed += 20;
        } else if (e.getKeyChar() == 'm' || e.getKeyChar() == 'M') {
            manualMode = !manualMode;
        } else {
            input = 0;
        }
        computer.setInput1(input);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        // Do nothing
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        input = 0;
        computer.setInput1(0);
    }
}
