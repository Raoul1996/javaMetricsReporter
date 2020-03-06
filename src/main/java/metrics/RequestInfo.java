package metrics;

public class RequestInfo {
    private String apiName;
    private double responseTime;
    private long timestamp;

    public RequestInfo() {

    }

    public RequestInfo(String apiName, double responseTime,long timestamp) {
        this.apiName = apiName;
        this.responseTime = responseTime;
        this.timestamp = timestamp;
    }

    public String getApiName() {
        return this.apiName;
    }

    public double getResponseTime() {
        return this.responseTime;
    }
}
