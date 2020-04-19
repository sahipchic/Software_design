package system_design.command;

import java.time.Instant;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CreateSubscriptionCommand {
    @TargetAggregateIdentifier
    Integer subscriptionId;
    String owner;
    Instant startDate;
    Instant finishDate;
}
