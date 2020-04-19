package system_design.event;

import java.time.Duration;

import lombok.Value;

@Value
public class SubscriptionRenewedEvent {
    Integer subscriptionId;
    Duration duration;
}
