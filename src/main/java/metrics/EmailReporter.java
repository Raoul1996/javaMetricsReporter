package metrics;

import java.util.*;

public class EmailReporter implements StatViewer {
    private static final Long DAY_HOURS_IN_SECONDS = 86400L;

    private MetricsStorage metricsStorage;
    private Aggregator aggregator;
    private StatViewer viewer;
    public EmailReporter(MetricsStorage metricsStorage,Aggregator aggregator,StatViewer viewer){
        this.metricsStorage = metricsStorage;
        this.aggregator =aggregator;
        this.viewer =viewer;
    }
    public void statDailyReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.MINUTE, 0);
        calendar.add(Calendar.SECOND, 0);
        calendar.add(Calendar.MILLISECOND, 0);
        Date firstTime = calendar.getTime();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long durationInMills = DAY_HOURS_IN_SECONDS * 1000;
                long endTimeInMills = System.currentTimeMillis();
                long startTimeInMills = endTimeInMills - durationInMills;
                Map<String, List<RequestInfo>> requestInfos = metricsStorage.getRequestInfos(startTimeInMills, endTimeInMills);
                Map<String, RequestStat> requestStats = aggregator.aggregate(requestInfos, durationInMills);
                viewer.output(requestStats, startTimeInMills, endTimeInMills);
            }
        }, firstTime, DAY_HOURS_IN_SECONDS * 1000);
    }

    @Override
    public void output(Map<String, RequestStat> requestStats, long startTimeInMills, long endTimeInMills) {

    }
}

