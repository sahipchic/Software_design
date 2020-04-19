package searchers;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;

import static searchers.StubServer.getResults;

public class Yandex extends SearchClient {

    private final int timeout;

    public Yandex(int timeout) {
        this.timeout = timeout;
    }

    private static final String YA_HOST = "https://yandex.ru";

    @Override
    public JSONArray search(String query) throws MalformedURLException {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = new URL(YA_HOST + "/?q=" + query);
        return getResults(url);
    }
}