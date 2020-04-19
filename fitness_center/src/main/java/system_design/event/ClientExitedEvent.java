package system_design.event;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Revision("1")
public class ClientExitedEvent {
    @TargetAggregateIdentifier
    Integer subscriptionId;
}
