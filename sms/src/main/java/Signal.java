import org.json.simple.JSONObject;

import java.util.Date;

public class Signal {
    private String symbol;
    private long createdAt;
    private String type;
    private String strategy;
    private int probability;
    private double entryPrice;
    private double takeProfit;
    private double stopLoss;

    public Signal(String symbol, String type, String strategy, int probability, double entryPrice, double takeProfit, double stopLoss) {
        this.symbol = symbol;
        this.createdAt = new Date().getTime();
        this.type = type;
        this.strategy = strategy;
        this.probability = probability;
        this.entryPrice = entryPrice;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("symbol", this.symbol);
        json.put("createdAt", this.createdAt);
        json.put("type", this.type);
        json.put("strategy", this.strategy);
        json.put("probability", this.probability);
        json.put("entryPrice", this.entryPrice);
        json.put("takeProfit", this.takeProfit);
        json.put("stopLoss", this.stopLoss);

        return json.toJSONString();
    }

    public String getSymbol() {
        return symbol;
    }

    public long getCreatedAt() {
        return createdAt;
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
}
