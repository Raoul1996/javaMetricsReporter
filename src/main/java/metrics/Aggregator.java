package metrics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Aggregator {
    public static RequestStat aggregate(List<RequestInfo> requestInfos, long durationInMillis) {
        double maxRespTime = Double.MIN_VALUE;
        double minRespTime = Double.MAX_VALUE;
        double avgRespTime = -1;
        double p99RespTime = -1;
        double p999RespTime = -1;
        double sumRespTime = 0;
        long count = 0;
        for (RequestInfo requestInfo : requestInfos) {
            ++count;
            double respTime = requestInfo.getResponseTime();
            if (maxRespTime < respTime) {
                maxRespTime = respTime;
            }
            if (minRespTime > respTime) {
                minRespTime = respTime;
            }
            sumRespTime += respTime;
        }
        if (count != 0) {
            avgRespTime = sumRespTime / count;
        }
        long tps = count / durationInMillis * 1000;
        Collections.sort(requestInfos, new Comparator<RequestInfo>() {
            @Override
            public int compare(RequestInfo o1, RequestInfo o2) {
                double diff = o1.getResponseTime() - o2.getResponseTime();
                return Double.compare(diff, 0.0);
            }
        });
        int idx999 = (int)(count*0.999);
        int idx99 = (int)(count*0.99);
        if (count !=0){
            p999RespTime = requestInfos.get(idx999).getResponseTime();
            p99RespTime = requestInfos.get(idx99).getResponseTime();
        }
        RequestStat reqStats = new RequestStat();
        reqStats.setMaxResponseTime(maxRespTime);
        reqStats.setMinResponseTime(minRespTime);
        reqStats.setAvgResponseTime(avgRespTime);
        reqStats.setP999ResponseTime(p999RespTime);
        reqStats.setP99ResponseTime(p99RespTime);
        reqStats.setCount(count);
        reqStats.setTps(tps);
        return reqStats;
    }
}

