package metrics;

public class RequestStat {
    private double maxResponseTime;
    private double minResponseTime;
    private double avgResponseTime;
    private double p999ResponseTime;
    private double p99ResponseTime;
    private long count;
    private long tps;

    public void setMaxResponseTime(double maxRespTime) {
        this.maxResponseTime = maxRespTime;
    }

    public double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMinResponseTime(double minRespTime) {
        this.minResponseTime = minRespTime;
    }

    public double getMinResponseTime() {
        return minResponseTime;
    }

    public void setAvgResponseTime(double avgRespTime) {
        this.avgResponseTime = avgRespTime;
    }

    public void setP999ResponseTime(double p999RespTime) {
        this.p999ResponseTime = p999RespTime;
    }

    public void setP99ResponseTime(double p99RespTime) {
        this.p99ResponseTime = p99RespTime;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setTps(long tps) {
        this.tps = tps;
    }

    public long getTps() {
        return tps;
    }

    public long getCount() {
        return count;
    }

    public double getP99ResponseTime() {
        return p99ResponseTime;
    }

    public double getP999ResponseTime() {
        return p999ResponseTime;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }
}
