package system_design.history;

import java.time.Instant;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import system_design.event.ClientEnteredEvent;
import system_design.event.ClientExitedEvent;

@Component
@RequiredArgsConstructor
public class SubscriptionHistoryProjection {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;

    @EventHandler
    public void on(ClientEnteredEvent event, @Timestamp Instant timestamp) {
        subscriptionHistoryRepository.save(new SubscriptionHistoryDao(event.getSubscriptionId(), timestamp,
                SubscriptionHistoryDao.Action.ENTER));
    }

    @EventHandler
    public void on(ClientExitedEvent event, @Timestamp Instant timestamp) {

        subscriptionHistoryRepository.save(new SubscriptionHistoryDao(event.getSubscriptionId(), timestamp,
                SubscriptionHistoryDao.Action.EXIT));

    }

    @QueryHandler(queryName = "gateHistory")
    public List<SubscriptionHistoryDao> getHistory() {
        return subscriptionHistoryRepository.findAll();
    }
}
