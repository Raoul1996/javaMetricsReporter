package metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisMetricsStorage implements MetricsStorage {
    @Override
    public void saveRequestInfo(RequestInfo requestInfo) {

    }

    @Override
    public List<RequestInfo> getRequestInfos(String apiName, long startTimeInMillis, long endTimeInMillis) {
        List<RequestInfo> requestInfos = new ArrayList<>();
        requestInfos.add(new RequestInfo("register", 123, 10234));

        return requestInfos;
    }

    @Override
    public Map<String, List<RequestInfo>> getRequestInfos(long startTimeInMillis, long endTimeInMillis) {
        return null;
    }
}
