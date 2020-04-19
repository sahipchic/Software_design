package system_design.controller;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import system_design.aggregate.Subscription;
import system_design.command.EnterClientCommand;
import system_design.event.SubscriptionCreatedEvent;
import system_design.event.SubscriptionRenewedEvent;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ManagerControllerTest {

    private AggregateTestFixture<Subscription> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(Subscription.class);
    }

    @Test
    public void testCreateSubmission() {

        final Instant now = Instant.now();
        final Instant finish = now.plusSeconds(60);
        Subscription subscription = new Subscription();
        subscription.handle(new SubscriptionCreatedEvent(1, "alex", now, finish));
        final Duration day = Duration.of(1, ChronoUnit.DAYS);
        subscription.handle(new SubscriptionRenewedEvent(1, day));

        Assertions.assertEquals(finish.plus(day), subscription.getFinishDate());
        Assertions.assertEquals(1, subscription.getSubscriptionId());

    }

    @Test
    public void testExpireSubmissionScenario() {

        final Instant now = Instant.now();
        final Instant finish = now.minusSeconds(60);
        final SubscriptionCreatedEvent createEvent = new SubscriptionCreatedEvent(1, "alex", now, finish);
        fixture.given(createEvent)
                .when(new EnterClientCommand(1))
                .expectException(IllegalStateException.class);
    }

}
