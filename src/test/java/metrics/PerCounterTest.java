package metrics;

import java.util.ArrayList;
import java.util.List;

public class PerCounterTest {
    public static void main(String[] args) {
        ConsoleReporter consoleReporter = new ConsoleReporter();
        consoleReporter.startReportedReport(60, 60);

        List<String> emailToAdresses = new ArrayList<>();
        emailToAdresses.add("xxx@xxx.com");

        EmailReporter emailReporter = new EmailReporter(emailToAdresses);

        emailReporter.statDailyReport();

        MetricsCollector collector = new MetricsCollector();
        collector.recordRequest(new RequestInfo("register", 123, 10234));
        collector.recordRequest(new RequestInfo("register", 223, 11234));
        collector.recordRequest(new RequestInfo("register", 323, 12334));
        collector.recordRequest(new RequestInfo("login", 23, 12434));
        collector.recordRequest(new RequestInfo("login", 1223, 14234));

        try{
            Thread.sleep(100000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
