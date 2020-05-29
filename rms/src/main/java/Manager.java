

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Manager {
    public ArrayList<Signal> signals = new ArrayList<Signal>();

    public void orchestrate() {
            new Thread(() -> this.receiveSignals().forEach(signal -> new Thread(() -> {
                System.out.println(this.getCurrentTime()+":\t\tSignal received\t\t"+signal.getSymbol()+"@"+signal.getEntryPrice());
                this.applyRiskManagement(signal);
                if (signal.isRiskVerified()) {
                    System.out.println(this.getCurrentTime()+":\t\tSignal verified risk\t\t"+signal.getSymbol()+"@"+signal.getEntryPrice());
                    this.serveSignal(signal);
                    System.out.println(this.getCurrentTime()+":\t\tSignal served\t\t"+signal.getSymbol()+"@"+signal.getEntryPrice());
                    return;
                }
                System.out.println(this.getCurrentTime()+":\t\tSignal failed risk\t\t"+signal.getSymbol()+"@"+signal.getEntryPrice());
            }).start())).start();
            new Thread(() -> this.watchWalletUpdate()).start();
    }

    private void watchWalletUpdate() {
        Stream.generate(() -> {
            try (WatchService watcher = FileSystems.getDefault().newWatchService();) {
                Path dir = Paths.get("/home/abm/MEGA/myprojects/ats/medium");
                dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
                return watcher.take().pollEvents().stream().map  (watchEvent -> {
                    if (watchEvent.context().toString().equals("wallet.txt"))
                        return watchEvent.context().toString();
                    return null;
                });
            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
                return null;
            }
        }).flatMap(stringStream -> stringStream).filter(s -> s != null).forEach(s -> {
            try {
                String data = Files.readString(Paths.get("/home/abm/MEGA/myprojects/ats/medium/wallet.txt"));
                Risk.wallet+=Double.parseDouble(data);
                System.out.println(this.getCurrentTime()+":\t\t"+data+" added to the wallet");
                new File("/home/abm/MEGA/myprojects/ats/medium/wallet.txt").delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        try (var file = new FileWriter("/home/abm/MEGA/myprojects/ats/medium/orders/"+signal.getSymbol()+"@"+signal.getEntryPrice()+".json");) {
            file.write(signal.toJSON().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date());
    }
}
