package searchers;

import model.SearchResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class StubServer {

    public StubServer() {

    }

    static JSONArray getResults(URL url) {
        String[] titles = new String[]{"VK", "Odnoklassniki", "VC"};
        String[] descs = new String[]{"Description about VK", "Description about OK", "Description about VC"};
        String[] links = new String[]{"https://vk.com", "https://ok.ru", "https://vc.ru"};
        JSONArray results = new JSONArray();
        for (int i = 0; i < 3; i++) {
            SearchResult result = new SearchResult(titles[i], links[i], descs[i]);
            JSONObject jsonObject = new JSONObject(result);
            results.put(jsonObject);
        }
        return results;
    }
}
