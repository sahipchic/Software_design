import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import model.*;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MasterSearchActor extends UntypedActor {

    private static final int SEARCH_ENGINE_COUNT = SearchAggregator.values().length;
    private final ActorRef childRouter;
    private OutputStreamWriter outputWriter;
    private List<ChildResponse> responses;
    private ResultHolder resultHolder;

    public MasterSearchActor(OutputStreamWriter outputWriter, ResultHolder resultHolder) {
        this.outputWriter = outputWriter;
        this.resultHolder = resultHolder;
        childRouter = getContext().actorOf(new RoundRobinPool(SEARCH_ENGINE_COUNT).props(Props.create(ChildSearchActor.class)),
                "childRouter");
        responses = new ArrayList<>();
        getContext().setReceiveTimeout(Duration.create("5 seconds"));
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof MasterRequest) {
            MasterRequest request = (MasterRequest) o;
            for (int i = 0; i < SEARCH_ENGINE_COUNT; i++) {
                childRouter.tell(new ChildRequest(SearchAggregator.values()[i], request.getQuery(), request.getTimeout()), getSelf());
            }
        } else if (o instanceof ChildResponse) {
            ChildResponse response = (ChildResponse) o;
            responses.add(response);
            if (responses.size() == SEARCH_ENGINE_COUNT) {
                handleExit(false);
            }
        } else if (o instanceof ReceiveTimeout) {
            handleExit(true);
            getContext().system().terminate();
        } else {
            unhandled(o);
        }
    }

    private void handleExit(boolean fail) throws IOException {
        MasterResponse response = new MasterResponse(responses);
        if (fail) {
            response.setError("Master actor timed out.");
        }
        outputWriter.write(response.toString());
        outputWriter.flush();
        resultHolder.setResult(response);
        context().stop(self());
    }
}
