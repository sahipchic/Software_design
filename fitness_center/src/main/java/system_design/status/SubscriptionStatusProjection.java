package system_design.status;


import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import system_design.event.SubscriptionCreatedEvent;
import system_design.event.SubscriptionRenewedEvent;

@Component
@RequiredArgsConstructor
public class SubscriptionStatusProjection {

    private final SubscriptionStatusRepository subscriptionStatusRepository;

    @EventHandler
    public void handle(SubscriptionCreatedEvent event) {
        subscriptionStatusRepository.save(new SubscriptionStatusDao(event.getSubscriptionId(), event.getOwner(), event.getStartDate(), event.getFinishDate()));
    }

    @EventHandler
    protected void handle(SubscriptionRenewedEvent event) {
        subscriptionStatusRepository.findById(event.getSubscriptionId())
                .map(subscriptionStatusDao -> {
                    subscriptionStatusDao.setFinishDate(subscriptionStatusDao.getFinishDate().plus(event.getDuration()));
                    return subscriptionStatusDao;
                });
    }

    @QueryHandler(queryName = "findAll")
    protected Iterable<SubscriptionStatusDao> findAll() {
        return subscriptionStatusRepository.findAll();
    }

    @QueryHandler(queryName = "findOne")
    protected SubscriptionStatusDao findOne(Integer subscriptionId) {
        return subscriptionStatusRepository.findById(subscriptionId).orElse(null);
    }
}
