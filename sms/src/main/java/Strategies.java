public class Strategies {
    private Stock stock;

    public Strategies(Stock stock) {
        this.stock = stock;
    }

    public Signal simpleStupid() {

        String type;
        String strategy;
        int probability;
        double entryPrice;
        double takeProfit;
        double stopLoss;

        double avgPrice = 0;
        for (int i = this.stock.getCharts().get("d").getCandleList().size() - 20; i < this.stock.getCharts().get("d").getCandleList().size(); i++) {
            avgPrice+=this.stock.getCharts().get("d").getCandleList().get(i).getClose();
        }
        avgPrice/=20;
        double lastPrice = this.stock.getCharts().get("d").getCandleList().get(this.stock.getCharts().get("d").getCandleList().size() -1).getClose();

        if (avgPrice/lastPrice - lastPrice > 0) {
            return new Signal(this.stock.getSymbol(), "d", "simpleStupid", 50,
                    lastPrice, avgPrice, lastPrice-lastPrice*0.01);
        }

        return null;
    }
}
