import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.stream.Stream;

public class Manager {
    private String[] stocks = {
            "F"
    };

    public void generateSignals() {
            Stream.of(this.stocks)
                    .flatMap(s -> {
                Stock stock = new Stock(s);
                stock.initChart("d", new Date().getTime() - 1_814_400 *1000, 0);
                        return stock.generateDaySignals(30);
            })
            .forEach(signal -> {
                System.out.println("Signal => buy "+signal.getSymbol()+" @ "+signal.getEntryPrice());
                this.serveSignal(signal);
            })
            ;
    }

    public void serveSignal(Signal signal) {
        try (var file = new FileWriter("/home/abm/MEGA/myprojects/ats/medium/signals/"+
                                        signal.getSymbol()+"@"+signal.getEntryPrice()+
                                        "@"+new SimpleDateFormat("MMdd hhmm").format(signal.getCreatedAt())+
                                        ".json");) {
            file.write(signal.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
