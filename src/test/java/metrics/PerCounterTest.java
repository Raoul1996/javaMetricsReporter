package metrics;

public class PerCounterTest {
    public static void main(String[] args) {
        MetricsStorage storage = new RedisMetricsStorage();
        Aggregator aggregator = new Aggregator();

        ConsoleViewer consoleViewer = new ConsoleViewer();
        ConsoleReporter consoleReporter = new ConsoleReporter(storage, aggregator, consoleViewer);
        consoleReporter.startReportedReport(60, 60);

        EmailViewer emailViewer = new EmailViewer();

        emailViewer.addToAddress("xxxx@xxx.com");

        EmailReporter emailReporter = new EmailReporter(storage, aggregator, emailViewer);

        emailReporter.statDailyReport();

        MetricsCollector collector = new MetricsCollector(storage);
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
