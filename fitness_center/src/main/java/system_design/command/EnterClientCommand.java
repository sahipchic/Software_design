package system_design.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class EnterClientCommand {
    @TargetAggregateIdentifier
    Integer subscriptionId;
}
