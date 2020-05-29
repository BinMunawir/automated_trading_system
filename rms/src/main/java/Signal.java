import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

        double totalAcceptedLoss = Risk.walletRiskPercentage / 100 * Risk.wallet;
        if (singleLoss > totalAcceptedLoss) {
            this.isRiskVerified = false;
            return;
        }

        this.isRiskVerified = true;
    }

    public void calculatePositionSize() {
        double riskPerShare = this.entryPrice - this.stopLoss;
        this.numOfShares = (int)(Risk.walletRiskPercentage /100 * Risk.wallet / riskPerShare);

        double maxAllowedSize = Risk.maxPositionSizeOfWalletPercentage /100 * Risk.wallet;
        while (true) {
            double cost = numOfShares * this.entryPrice;
            if(cost <= maxAllowedSize && cost <= Risk.wallet)
                break;
            this.numOfShares--;
        }
        Risk.wallet-=(this.numOfShares*this.entryPrice);
    }


    public static Signal fromJSON(String text) {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject json = (JSONObject) jsonParser.parse(text);
            return new Signal((String) json.get("symbol"), (String) json.get("type"), (String) json.get("strategy"), Integer.parseInt(json.get("probability").toString()), Double.parseDouble(json.get("entryPrice").toString()), Double.parseDouble(json.get("takeProfit").toString()), Double.parseDouble(json.get("stopLoss").toString()));
        } catch (ParseException e) {
//            e.printStackTrace();
            return null;
        }
    }
    public JSONObject toJSON() {

        JSONObject json = new JSONObject();
        json.put("symbol", this.symbol);
        json.put("type", this.type);
        json.put("strategy", this.strategy);
        json.put("probability", this.probability);
        json.put("numOfShares", this.numOfShares);
        json.put("takeProfit", this.takeProfit);
        json.put("entryPrice", this.entryPrice);
        json.put("stopLoss", this.stopLoss);

        return  json;
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
