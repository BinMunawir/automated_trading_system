package rms;

public class Main {

    public static void main(String[] args) {

        Signal signal = new Signal("F", "D", "x", 50, 8.6, 10, 7.9);
        signal.verifyRisk();
        System.out.println(signal.isRiskVerified());

        if (signal.isRiskVerified()) {
            signal.calculatePositionSize();
            System.out.println(signal.getNumOfShares());
        }
    }
}
