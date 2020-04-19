package model;

public class MasterRequest {

    private final String query;
    private final int timeout;

    public MasterRequest(String query, int timeout) {
        this.query = query;
        this.timeout = timeout;
    }

    public String getQuery() {
        return query;
    }

    public int getTimeout() {
        return timeout;
    }
}