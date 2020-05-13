package rms;

public class Signal {
    String symbol;
    String type;
    String strategy;
    int probability;
    boolean isRiskVerified;
    double entryPrice;
    double takeProfit;
    double stopLoss;

    public Signal(String symbol, String type, String strategy, int probability, double entryPrice, double takeProfit, double stopLoss) {
        this.symbol = symbol;
        this.type = type;
        this.strategy = strategy;
        this.probability = probability;
        this.entryPrice = entryPrice;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public void verifyRisk() {
        if (this.takeProfit / this.stopLoss >= Risk.r) {
            this.isRiskVerified = false;
            return;
        }
        double singleLoss = this.entryPrice - this.stopLoss;
        double totalAcceptedLoss = Risk.risk / 100 * Risk.capital;
        if (singleLoss > totalAcceptedLoss) {
            this.isRiskVerified = false;
            return;
        }
        this.isRiskVerified = true;
    }
}
