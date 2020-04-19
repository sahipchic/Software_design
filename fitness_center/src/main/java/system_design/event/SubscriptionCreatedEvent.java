package system_design.event;

import java.time.Instant;

import lombok.Value;
import org.axonframework.serialization.Revision;

@Value
@Revision("1")
public class SubscriptionCreatedEvent {
    Integer subscriptionId;
    String owner;
    Instant startDate;
    Instant finishDate;
}
