

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Manager {
    public ArrayList<Signal> signals = new ArrayList<Signal>();

    public void orchestrate() {
            this.receiveSignals().forEach(signal -> {
                this.applyRiskManagement(signal);
//                System.out.println(signal.getProbability()+": "+signal.isRiskVerified());
                this.serveSignal(signal);
            });
    }

    public Stream<Signal> receiveSignals() {
        return Stream.generate(() -> {
            try (WatchService watcher = FileSystems.getDefault().newWatchService();) {
                Path dir = Paths.get("/home/abm/MEGA/myprojects/ats/medium/signals");
                dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
                return watcher.take().pollEvents().stream().map  (watchEvent -> {
                    try {
                        String data = Files.readString(Paths.get("/home/abm/MEGA/myprojects/ats/medium/signals/" + watchEvent.context().toString()));
                        return Signal.fromJSON(data);
                    } catch (IOException e) {
//                        e.printStackTrace();
                        return null;
                    }
                }).filter(signal -> signal != null);
            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
                return null;
            }
        }).flatMap((s) -> s).filter(signal -> signal != null);
    }

    public void applyRiskManagement(Signal signal) {
        signal.verifyRisk();
        if (signal.isRiskVerified())
            signal.calculatePositionSize();
        signals.add(signal);
    }

    public void serveSignal(Signal signal) {
        if (! signal.isRiskVerified())
            return;

        try (var file = new FileWriter("/home/abm/MEGA/myprojects/ats/medium/orders/"+signal.getSymbol()+"@"+signal.getEntryPrice()+".json");) {
            file.write(signal.toJSON().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
