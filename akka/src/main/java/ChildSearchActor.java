import akka.actor.UntypedActor;
import model.ChildRequest;
import model.ChildResponse;
import model.SearchAggregator;
import org.json.JSONArray;
import searchers.Google;
import searchers.SearchClient;
import searchers.Yandex;

public class ChildSearchActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof ChildRequest) {
            ChildRequest request = (ChildRequest) o;
            SearchAggregator searchAggregator = request.getSearchAggregator();
            String query = request.getQuery();
            SearchClient searchClient;
            switch (searchAggregator) {
                case GOOGLE:
                    searchClient = new Google(0);
                    break;
                case YANDEX:
                    searchClient = new Yandex(request.getTimeout());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected search aggregator: " + searchAggregator);
            }
            JSONArray results = searchClient.search(query);
            ChildResponse response = new ChildResponse(searchAggregator, query, results);
            getSender().tell(response, getSelf());
        } else {
            unhandled(o);
        }
    }
}