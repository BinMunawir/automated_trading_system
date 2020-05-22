import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class Chart {
    private String symbol;
    private String interval;
    private long start;
    private long end;
    private int refreshPeriodInMinutes = 5;
    private List<Candle> candleList;

    public Chart(String symbol, String interval, long start, long end) {
        this.symbol = symbol;
        this.interval = interval;
        this.start = start;
        this.end = end;
        this.candleList = new ArrayList<Candle>();
    }

    public Chart(String symbol, String interval, long start) {
        this(symbol, interval, start, 0);
    }

    public void setHistory() throws Exception {
        String id = this.symbol+ "@" + this.interval + "@" + this.start + "-" + this.end;

        File requestFile = new File("/home/abm/MEGA/myprojects/ats/medium/data/requests/" + id + ".json");
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("symbol", this.symbol);
        requestJSON.put("interval", this.interval);
        requestJSON.put("start", this.start);
        requestJSON.put("end", this.end);
        Files.writeString(requestFile.toPath(), requestJSON.toString());

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path responseDir = Paths.get("/home/abm/MEGA/myprojects/ats/medium/data/responses");
        responseDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        Stream.generate(() -> {
            try {
                return watchService.take().pollEvents().stream().filter(watchEvent -> watchEvent.context().toString() != id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }).flatMap(watchEventStream -> watchEventStream).limit(1).forEach(watchEvent -> {
            try {
                System.out.println(watchEvent.context().toString());
                String data = Files.readString(Paths.get(responseDir.toString() + "/" + watchEvent.context().toString()));
                Candle.listFromJSON(data).forEach(candle -> this.candleList.add(candle));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        watchService.close();
    }

    public void connectLive() throws Exception {
        String id = this.symbol+ "@" + this.interval+"@L";

        File requestFile = new File("/home/abm/MEGA/myprojects/ats/medium/data/requests/" + id + ".json");
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("symbol", this.symbol);
        requestJSON.put("interval", this.interval);
        requestJSON.put("isLive", true);
        Files.writeString(requestFile.toPath(), requestJSON.toString());

        Path responseDir = Paths.get("/home/abm/MEGA/myprojects/ats/medium/data/responses");
        Stream.generate(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService();) {
                responseDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                return watchService.take().pollEvents().stream().filter(watchEvent -> watchEvent.context().toString() != id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).flatMap(watchEventStream -> watchEventStream).forEach(watchEvent -> {
            try {
                System.out.println("data processed");
                File file = new File(responseDir.toString() + "/" + watchEvent.context().toString());
                String data = Files.readString(file.toPath());
                file.delete();
                this.candleList.add(Candle.fromJSON(data));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String getInterval() {
        return interval;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public List<Candle> getCandleList() {
        return candleList;
    }
}
