package metrics;

import java.util.*;

public abstract class ScheduledReporter {
    private static long MAX_STAT_DURATION_IN_MILLIS = 10 * 60 * 1000;
    protected MetricsStorage metricsStorage;
    protected Aggregator aggregator;
    protected StatViewer viewer;

    public ScheduledReporter(MetricsStorage metricsStorage, Aggregator aggregator, StatViewer viewer) {
        this.metricsStorage = metricsStorage;
        this.aggregator = aggregator;
        this.viewer = viewer;
    }

    protected void doStatAndReport(long startTimeInMillis, long endTimeInMillis) {
        Map<String, RequestStat> stats = doStat(startTimeInMillis, endTimeInMillis);
        viewer.output(stats, startTimeInMillis, endTimeInMillis);
    }

    private Map<String, RequestStat> doStat(long startTimeInMillis, long endTimeInMillis) {
        Map<String, List<RequestStat>> segmentStats = new HashMap<>();
        while (startTimeInMillis < endTimeInMillis) {
            long segmentEndTimeInMillis = startTimeInMillis + MAX_STAT_DURATION_IN_MILLIS;
            if (segmentEndTimeInMillis > endTimeInMillis) {
                segmentEndTimeInMillis = endTimeInMillis;
            }
            Map<String, List<RequestInfo>> requestInfos = metricsStorage.getRequestInfos(startTimeInMillis, segmentEndTimeInMillis);
            if (requestInfos == null || requestInfos.isEmpty()) {
                continue;
            }
            Map<String, RequestStat> segmentStat = aggregator.aggregate(requestInfos, segmentEndTimeInMillis - startTimeInMillis);
            addStat(segmentStats, segmentStat);
        }
        return aggregateStat(segmentStats, endTimeInMillis - startTimeInMillis);

    }

    private void addStat(Map<String, List<RequestStat>> segmentStates, Map<String, RequestStat> segmentStat) {
        for (Map.Entry<String, RequestStat> entry : segmentStat.entrySet()) {
            String apiName = entry.getKey();
            RequestStat stat = entry.getValue();
            List<RequestStat> statList = segmentStates.putIfAbsent(apiName, new ArrayList<>());
            if (statList != null) {
                statList.add(stat);
            }
        }
    }

    private Map<String, RequestStat> aggregateStat(Map<String, List<RequestStat>> segmentStats, long durationInMillis) {
        Map<String, RequestStat> aggregateStats = new HashMap<>();
        for (Map.Entry<String, List<RequestStat>> entry : segmentStats.entrySet()) {
            String apiName = entry.getKey();
            List<RequestStat> apiStats = entry.getValue();
            double maxRespTime = Double.MIN_VALUE;
            double minRespTime = Double.MAX_VALUE;
            long count = 0;
            double sumRespTime = 0;
            for (RequestStat stat: apiStats){
                if (stat.getMaxResponseTime()>maxRespTime){
                    maxRespTime = stat.getMaxResponseTime();
                }
                if (stat.getMinResponseTime()<minRespTime){
                    minRespTime = stat.getMinResponseTime();
                }
                count += stat.getCount();
                sumRespTime += (stat.getCount()* stat.getAvgResponseTime());
            }
            RequestStat aggregateStat = new RequestStat();
            aggregateStat.setMaxResponseTime(maxRespTime);
            aggregateStat.setMinResponseTime(minRespTime);
            aggregateStat.setCount(count);
            aggregateStat.setTps(count/durationInMillis*1000);
            aggregateStats.put(apiName,aggregateStat);
        }
        return aggregateStats;
    }
}
