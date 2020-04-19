package system_design.aggregate;


import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import system_design.command.CreateSubscriptionCommand;
import system_design.command.EnterClientCommand;
import system_design.command.ExitClientCommand;
import system_design.command.RenewSubscriptionCommand;
import system_design.event.ClientEnteredEvent;
import system_design.event.ClientExitedEvent;
import system_design.event.SubscriptionCreatedEvent;
import system_design.event.SubscriptionRenewedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@AllArgsConstructor
@Aggregate
@ToString
@Getter
@NoArgsConstructor
public class Subscription {

    @AggregateIdentifier
    private Integer subscriptionId;

    private Instant finishDate;

    @CommandHandler
    public Subscription(CreateSubscriptionCommand command) {
        apply(new SubscriptionCreatedEvent(command.getSubscriptionId(), command.getOwner(), command.getStartDate(), command.getFinishDate()));
        apply(new ClientExitedEvent(command.getSubscriptionId()));
    }

    @CommandHandler
    public void handle(RenewSubscriptionCommand command) {
        apply(new SubscriptionRenewedEvent(command.getSubscriptionId(), command.getDuration()));
    }

    @CommandHandler
    public void handle(EnterClientCommand command) {
        if (finishDate.isBefore(Instant.now())) {
            throw new IllegalStateException("Subscription has expired");
        }
        apply(new ClientEnteredEvent(command.getSubscriptionId()));
    }

    @CommandHandler
    public void handle(ExitClientCommand command) {
        if (finishDate.isBefore(Instant.now())) {
            throw new IllegalStateException("Subscription has expired");
        }
        apply(new ClientExitedEvent(command.getSubscriptionId()));
    }

    @EventSourcingHandler
    public void handle(SubscriptionCreatedEvent event) {
        this.subscriptionId = event.getSubscriptionId();
        this.finishDate = event.getFinishDate();
    }

    @EventSourcingHandler
    public void handle(SubscriptionRenewedEvent event) {
        this.finishDate = this.finishDate.plus(event.getDuration());
    }
}
