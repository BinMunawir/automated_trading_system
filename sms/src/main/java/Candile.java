public class Candile {
    private int date;
    private double high;
    private double open;
    private double close;
    private double low;
    private double volume;

    public Candile(int date, double high, double open, double close, double low, double volume) {
        this.date = date;
        this.high = high;
        this.open = open;
        this.close = close;
        this.low = low;
        this.volume = volume;
    }

    public int getDate() {
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
