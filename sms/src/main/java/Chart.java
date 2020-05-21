import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Chart {
    private String interval;
    private int start;
    private int end;
    private int refreshPeriod = 5000;
    private List<Candle> candleList;

    public Chart(String interval, int start, int end) {
        this.interval = interval;
        this.start = start;
        this.end = end;
    }

    public void setHistory() throws Exception {
        File file = new File("/home/abm/MEGA/myprojects/ats/medium/data/F/" + this.interval + "/data.json");
        String data = Files.readString(file.toPath());
        Candle.listFromJSON(data).forEach(this.candleList::add);
    }

    public void connectLive() throws Exception {
        while (true) {
            File file = new File("/home/abm/MEGA/myprojects/ats/medium/data/F/" + this.interval + "/live.json");
            String data = Files.readString(file.toPath());
            Candle candle = Candle.fromJSON(data);
            if (this.candleList.get(this.candleList.size() -1).getDate() != candle.getDate())
                this.candleList.add(candle);
            Thread.sleep(this.refreshPeriod);
        }
    }

    public String getInterval() {
        return interval;
    }
    public int getStart() {
        return start;
    }
    public int getEnd() {
        return end;
    }
}
