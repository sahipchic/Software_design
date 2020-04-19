package system_design.command;

import java.time.Duration;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class RenewSubscriptionCommand {
    @TargetAggregateIdentifier
    Integer subscriptionId;
    Duration duration;
}
