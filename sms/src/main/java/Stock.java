import java.util.List;
import java.util.Map;

public class Stock {
    private String symbol;
    private Map<String, List<Signal>> signals;
    private Map<String, Chart> charts;

    public Stock(String symbol) {
        this.symbol = symbol;
    }


    public String getSymbol() {
        return symbol;
    }

    public Map<String, List<Signal>> getSignals() {
        return signals;
    }

    public Map<String, Chart> getCharts() {
        return charts;
    }
}
