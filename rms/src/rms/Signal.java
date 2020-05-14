package rms;

public class Signal {
    private String symbol;
    private String type;
    private String strategy;
    private int probability;
    private boolean isRiskVerified;
    private int numOfShares;
    private double entryPrice;
    private double takeProfit;
    private double stopLoss;

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

        if (this.probability < Risk.acceptedProbabilityPercentage) {
            this.isRiskVerified = false;
            return;
        }

        double singleLoss = this.entryPrice - this.stopLoss;
        double singleProfit = this.takeProfit - this.entryPrice;

        if (singleProfit / singleLoss < Risk.riskRatio) {
            this.isRiskVerified = false;
            return;
        }

        double totalAcceptedLoss = Risk.capitalRiskPercentage / 100 * Risk.capital;
        if (singleLoss > totalAcceptedLoss) {
            this.isRiskVerified = false;
            return;
        }

        this.isRiskVerified = true;
    }

    public void calculatePositionSize() {
        double riskPerShare = this.entryPrice - this.stopLoss;
        this.numOfShares = (int)(Risk.capitalRiskPercentage /100 * Risk.capital / riskPerShare);

        double maxAllowedSize = Risk.maxPositionSizeOfCapitalPercentage /100 * Risk.capital;
        while (true) {
            double cost = numOfShares * this.entryPrice;
            if(cost <= maxAllowedSize && cost <= Risk.wallet)
                break;
            this.numOfShares--;
        }
    }

    public String getSymbol() {
        return symbol;
    }
    public String getType() {
        return type;
    }
    public String getStrategy() {
        return strategy;
    }
    public int getProbability() {
        return probability;
    }
    public double getEntryPrice() {
        return entryPrice;
    }
    public double getTakeProfit() {
        return takeProfit;
    }
    public double getStopLoss() {
        return stopLoss;
    }
    public boolean isRiskVerified() {
        return isRiskVerified;
    }
    public int getNumOfShares() {
        return numOfShares;
    }
}
