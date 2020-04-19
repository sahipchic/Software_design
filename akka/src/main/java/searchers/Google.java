package searchers;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;

public class Google extends SearchClient {
    private final int timeout;

    public Google(int timeout) {
        this.timeout = timeout;
    }

    private static final String G_HOST = "https://google.com";

    @Override
    public JSONArray search(String query) throws MalformedURLException {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = new URL(G_HOST + "/?q=" + query);
        return getResults(url);
    }
}
