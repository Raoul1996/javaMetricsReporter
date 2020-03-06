package metrics;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;

public class Aggregator {
    public Map<String, RequestStat> aggregate(Map<String, List<RequestInfo>> requestInfos, long durationInMillis) {
        Map<String, RequestStat> requestStatStats = new HashMap<>();
        for (Map.Entry<String, List<RequestInfo>> entry : requestInfos.entrySet()) {
            String apiName = entry.getKey();
            List<RequestInfo> requestInfosPerApi = entry.getValue();

            RequestStat requestStat = doAggregate(requestInfosPerApi, durationInMillis);
            requestStatStats.put(apiName, requestStat);
        }
        return requestStatStats;
    }

    private RequestStat doAggregate(List<RequestInfo> requestInfos, long durationInMillis) {
        List<Double> respTimes = new ArrayList<>();
        for (RequestInfo requestInfo : requestInfos) {
            double requestTime = requestInfo.getResponseTime();
            respTimes.add(requestTime);
        }
        RequestStat reqStats = new RequestStat();
        reqStats.setMaxResponseTime(max(respTimes));
        reqStats.setMinResponseTime(min(respTimes));
        reqStats.setAvgResponseTime(avg(respTimes));
        reqStats.setP999ResponseTime(percentile999(respTimes));
        reqStats.setP99ResponseTime(percentile99(respTimes));
        reqStats.setCount(respTimes.size());
        reqStats.setTps((long) tps(respTimes.size(), durationInMillis / 1000));
        return reqStats;
    }

    private double tps(double respTimes, long durationInSecond) {
        return respTimes / durationInSecond;
    }

    private double percentile999(List<Double> respTimes) {
        return percentile(respTimes, 0.999);
    }

    private double percentile99(List<Double> respTimes) {
        return percentile(respTimes, 0.99);
    }

    private double percentile(List<Double> respTimes, double v) {
        Collections.sort(respTimes, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                double diff = o1 - o2;
                return Double.compare(diff, 0.0);
            }
        });
        int idx = (int) (respTimes.size() * v);
        if (respTimes.size() != 0) {
            return respTimes.get(idx);
        } else {
            return 0;
        }
    }

    private double min(List<Double> respTimes) {
        double minRespTime = Double.MAX_VALUE;
        for (double respTime : respTimes) {
            if (minRespTime > respTime) {
                minRespTime = respTime;
            }
        }
        return minRespTime;
    }

    private double sum(List<Double> respTimes) {
        double sumRespTime = 0;
        for (double respTime : respTimes) {
            sumRespTime += respTime;
        }
        return sumRespTime;
    }

    private double avg(List<Double> respTimes) {
        return sum(respTimes) / respTimes.size();
    }

    private double max(List<Double> respTimes) {
        double maxRespTime = Double.MIN_VALUE;

        for (double respTime : respTimes) {
            if (maxRespTime < respTime) {
                maxRespTime = respTime;
            }
        }
        return maxRespTime;
    }
}

