package system_design.event;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class ClientEnteredEvent {
    @TargetAggregateIdentifier
    Integer subscriptionId;
}
