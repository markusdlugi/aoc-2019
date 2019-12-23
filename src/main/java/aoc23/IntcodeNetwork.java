package aoc23;

import java.util.Map;
import java.util.Queue;

public class IntcodeNetwork {

    private Map<Integer, IntcodeComputer> computerMap;

    private long natX;
    private long natY;
    private long lastNat = -1;

    public IntcodeNetwork(Map<Integer, IntcodeComputer> computerMap) {
        this.computerMap = computerMap;
    }

    public boolean sendNatMessage() {
        // Skip if we haven't received a NAT value yet
        if(natY == 0) {
            return false;
        }
        System.out.println("NAT message to 0: (" + natX + ", " + natY + ")");
        sendPacket(-1, 0, natX, natY);
        if (natY == lastNat) {
            System.out.println("NAT Y value " + natY + " repeated, halting.");
            return true;
        }
        lastNat = natY;
        return false;
    }

    public void notifyPacket(IntcodeComputer computer) {
        Queue<Long> outputQueue = computer.getOutputQueue();
        if (outputQueue.size() < 3) {
            return;
        }

        while (!outputQueue.isEmpty() && outputQueue.size() >= 3) {
            final long recipientAddress = outputQueue.poll();
            final long x = outputQueue.poll();
            final long y = outputQueue.poll();

            sendPacket(computer.getNetworkAddress(), (int) recipientAddress, x, y);
        }
    }

    private void sendPacket(int senderAddress, int recipientAddress, long x, long y) {
        System.out.println(senderAddress + " -> " + recipientAddress + " (" + x + ", " + y + ")");

        if (recipientAddress == 255) {
            // Special case: NAT received
            natX = x;
            natY = y;
            return;
        }

        IntcodeComputer recipient = computerMap.get(recipientAddress);
        if (recipient == null) {
            System.err.println("Recipient " + recipientAddress + " not available!");
            return;
        }
        recipient.getInputQueue().add(x);
        recipient.getInputQueue().add(y);
    }
}
