import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.stream.Stream;

public class Candle {
    private long date;
    private double high;
    private double open;
    private double close;
    private double low;
    private double volume;

    public Candle(long date, double high, double open, double close, double low, double volume) {
        this.date = date;
        this.high = high;
        this.open = open;
        this.close = close;
        this.low = low;
        this.volume = volume;
    }

    public static Stream<Candle> listFromJSON(String data) throws ParseException {
        JSONArray jsonData = (JSONArray)new JSONParser().parse(data);
        return jsonData.stream().map(o -> {
            long date = Long.parseLong(((JSONObject)o).get("date").toString());
            double high = Double.parseDouble(((JSONObject)o).get("high").toString());
            double open = Double.parseDouble(((JSONObject)o).get("open").toString());
            double close = Double.parseDouble(((JSONObject)o).get("close").toString());
            double low = Double.parseDouble(((JSONObject)o).get("low").toString());
            double volume = Double.parseDouble(((JSONObject)o).get("volume").toString());
            return new Candle(date, high, open, close, low, volume);
        });
    }
    public static Candle fromJSON(String data) throws ParseException {
        JSONObject jsonData = (JSONObject) new JSONParser().parse(data);
        long date = Long.parseLong(jsonData.get("date").toString());
        double high = Double.parseDouble(jsonData.get("high").toString());
        double open = Double.parseDouble(jsonData.get("open").toString());
        double close = Double.parseDouble(jsonData.get("close").toString());
        double low = Double.parseDouble(jsonData.get("low").toString());
        double volume = Double.parseDouble(jsonData.get("volume").toString());
        return new Candle(date, high, open, close, low, volume);
    }

    public long getDate() {
        return date;
    }

    public double getHigh() {
        return high;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getLow() {
        return low;
    }

    public double getVolume() {
        return volume;
    }
}
