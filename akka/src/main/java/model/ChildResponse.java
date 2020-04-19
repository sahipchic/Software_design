package model;

import org.json.JSONArray;

public class ChildResponse {

    private final SearchAggregator searchAggregator;
    private final String query;
    private final JSONArray results;

    public ChildResponse(SearchAggregator searchAggregator, String query, JSONArray results) {
        this.searchAggregator = searchAggregator;
        this.query = query;
        this.results = results;
    }

    public SearchAggregator getSearchAggregator() {
        return searchAggregator;
    }

    public String getQuery() {
        return query;
    }

    public JSONArray getResults() {
        return results;
    }
}