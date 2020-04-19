package model;

public class ChildRequest {

    private final SearchAggregator searchAggregator;
    private final String query;
    private final int timeout;

    public ChildRequest(SearchAggregator searchAggregator, String query, int timeout) {
        this.searchAggregator = searchAggregator;
        this.query = query;
        this.timeout = timeout;
    }

    public SearchAggregator getSearchAggregator() {
        return searchAggregator;
    }

    public String getQuery() {
        return query;
    }

    public int getTimeout() {
        return timeout;
    }
}