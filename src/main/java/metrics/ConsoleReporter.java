package metrics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsoleReporter extends ScheduledReporter {
    private ScheduledExecutorService executor;

    public ConsoleReporter(){
        this(new RedisMetricsStorage(),new Aggregator(),new ConsoleViewer());
    }
    public ConsoleReporter(MetricsStorage metricsStorage, Aggregator aggregator, StatViewer viewer) {
        super(metricsStorage,aggregator,viewer);
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void startReportedReport(long periodInSeconds, final long durationInSeconds) {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long durationInMillis = durationInSeconds * 1000;
                long endTimeInMillis = System.currentTimeMillis();
                long startTimeInMillis = endTimeInMillis - durationInMillis;
                doStatAndReport(startTimeInMillis, endTimeInMillis);
            }
        }, 0, periodInSeconds, TimeUnit.SECONDS);
    }
}

