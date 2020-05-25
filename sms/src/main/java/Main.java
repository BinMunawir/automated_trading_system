import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {

//        Stock stock = new Stock("F");
//        stock.initChart("d", new Date().getTime() - 1_814_400 *1000, 0);
//        System.out.println("start signals");
//        stock.generateSignals();
//        System.out.println("end");

        new Manager().generateSignals();

//        String a = Files.readString(Paths.get("/home/abm/MEGA/myprojects/ats/sms/src/main/java/data.json"));
//        new JSONParser().parse(a);
    }
}
