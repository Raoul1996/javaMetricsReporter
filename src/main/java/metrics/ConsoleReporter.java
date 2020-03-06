package metrics;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class ConsoleReporter {
    private  MetricsStorage metricsStorage;
    private ScheduledExecutorService executor;

    public ConsoleReporter(MetricsStorage metricsStorage){
        this.metricsStorage = metricsStorage;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }
    public void  startReportedReport(long periodInSeconds, final long durationInSeconds){
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long durationInMillis = durationInSeconds * 1000;
                long endTimeInMillis= System.currentTimeMillis();
                long startTimeInMillis = endTimeInMillis - durationInMillis;
                Map<String, List<RequestInfo>> requestInfos = metricsStorage.getRequestInfos(startTimeInMillis,endTimeInMillis);
                Map<String,RequestStat> stats =  new HashMap<>();

                for (Map.Entry<String,List<RequestInfo>> entry: requestInfos.entrySet()){
                    String apiName = entry.getKey();
                    List<RequestInfo> requestInfosPerApi = entry.getValue();

                    RequestStat requestStat = Aggregator.aggregate(requestInfosPerApi,durationInMillis);
                    stats.put(apiName,requestStat);
                }
                System.out.println("Time Span:[" + startTimeInMillis+","+endTimeInMillis+"]");
                Gson gson = new Gson();
                System.out.println(gson.toJson(stats));
            }
        },0,periodInSeconds, TimeUnit.SECONDS);
    }
}

