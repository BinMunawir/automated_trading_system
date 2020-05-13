package rms;

public class Order {
    private int numOfShares;
    private Signal signal;

    public Order(Signal signal) {
        this.signal = signal;
    }
    public void calculatePositionSize() {
        double riskPshare = this.signal.entryPrice - this.signal.stopLoss;
        this.numOfShares = (int)(Risk.risk/100 * Risk.capital / riskPshare);

        double maxAllowedSize = Risk.maxPositionSize/100 * Risk.capital;
        while (true) {
            double cost = numOfShares * this.signal.entryPrice;
            if(cost <= maxAllowedSize && cost <= Risk.wallet)
                break;
            this.numOfShares--;
        }
    }
}
