import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Stock {
    private String symbol;
    private Map<String, List<Signal>> signals;
    private Map<String, Chart> charts;
    private Strategies strategies;

    public Stock(String symbol) {
        this.symbol = symbol;
        this.signals = new HashMap<>();
        this.charts = new HashMap<>();
        this.strategies = new Strategies(this);
    }

    public void initChart(String interval, long start, long end) {
        new Thread(() -> {
            Chart chart = new Chart(this.symbol, interval, start, end);
            try {
                chart.setHistory();
                this.charts.put(interval, chart);
                chart.connectLive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void generateSignals() {
        this.generateDaySignals(0);
    }

    public Stream<Signal> generateDaySignals(int refreshPeriod) {
        int finalRefreshPeriod = refreshPeriod == 0 ? 10 : refreshPeriod;
        final Object[] finalReturnValue = new Object[1];
        new Thread(() -> {
            if (this.signals.get("d") == null)
                this.signals.put("d", new ArrayList<Signal>());

            finalReturnValue[0] = Stream.generate(() -> {
                try {
                    Thread.sleep(finalRefreshPeriod *1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Stream.of(strategies.simpleStupid());
            })
                    .flatMap(signalStream -> signalStream)
                    .filter(signal -> signal != null)
                    .filter(signal -> (!this.signals.get("d").isEmpty() &&
                            signal.getEntryPrice() != this.signals.get("d").get(this.signals.get("d").size()-1).getEntryPrice()) ||
                            this.signals.get("d").isEmpty())
                    .filter(signal -> signal.getEntryPrice() % 2 == 0)
            .peek(signal -> {
                this.signals.get("d").add(signal);
            });
        }).start();

        return Stream.generate(() -> finalReturnValue[0])
                .filter(o -> o != null)
                .peek(o -> finalReturnValue[0] = null)
                .flatMap(o -> (Stream<Signal>)o)
                ;
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
