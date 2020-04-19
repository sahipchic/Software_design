import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import model.MasterRequest;
import model.MasterResponse;
import model.SearchAggregator;
import model.ChildResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.OutputStreamWriter;

public class ActorsTest {

    @Test
    public void testPositive() {
        OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
        ActorSystem system = ActorSystem.create("system");
        ResultHolder resultHolder = new ResultHolder();
        ActorRef master = system.actorOf(Props.create(MasterSearchActor.class, outputWriter, resultHolder), "master");
        String query = "social network";
        String childResult = "[{\"link\":\"https://vk.com\",\"description\":\"Description about VK\",\"title\":\"VK\"},{\"link\":\"https://ok.ru\"," +
                "\"description\":\"Description about OK\",\"title\":\"Odnoklassniki\"},{\"link\":\"https://vc.ru\",\"description\":\"Description about VC\"," +
                "\"title\":\"VC\"}]";
        master.tell(new MasterRequest(query, 0), ActorRef.noSender());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MasterResponse response = resultHolder.getResult();
        Assert.assertNull(response.getError());
        Assert.assertEquals(2, response.getChildResponses().size());
        for (ChildResponse childResponse : response.getChildResponses()) {
            Assert.assertEquals(query, childResponse.getQuery());
            if (childResponse.getSearchAggregator().equals(SearchAggregator.GOOGLE)) {
                Assert.assertEquals(childResult, childResponse.getResults().toString());
            } else if (childResponse.getSearchAggregator().equals(SearchAggregator.YANDEX)) {
                Assert.assertEquals(childResult, childResponse.getResults().toString());
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void testTimeout() {
        OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
        ActorSystem system = ActorSystem.create("system");
        ResultHolder resultHolder = new ResultHolder();
        ActorRef master = system.actorOf(Props.create(MasterSearchActor.class, outputWriter, resultHolder), "master");
        String query = "social network";
        String childResult = "[{\"link\":\"https://vk.com\",\"description\":\"Description about VK\",\"title\":\"VK\"},{\"link\":\"https://ok.ru\"," +
                "\"description\":\"Description about OK\",\"title\":\"Odnoklassniki\"},{\"link\":\"https://vc.ru\",\"description\":\"Description about VC\"," +
                "\"title\":\"VC\"}]";
        master.tell(new MasterRequest(query, 10000), ActorRef.noSender());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MasterResponse response = resultHolder.getResult();
        Assert.assertEquals("Master actor timed out.", response.getError());
        Assert.assertEquals(1, response.getChildResponses().size());
        for (ChildResponse workerResponse : response.getChildResponses()) {
            Assert.assertEquals(query, workerResponse.getQuery());

            if (workerResponse.getSearchAggregator().equals(SearchAggregator.GOOGLE)) {
                Assert.assertEquals(childResult, workerResponse.getResults().toString());
            } else if (workerResponse.getSearchAggregator().equals(SearchAggregator.YANDEX)) {
                Assert.fail();
            } else {
                Assert.fail();
            }
        }
    }
}