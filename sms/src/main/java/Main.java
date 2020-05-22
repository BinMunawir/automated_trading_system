import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        Chart chart = new Chart("F", "d", 0);
        chart.connectLive();
    }
}
