package system_design.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import system_design.command.CreateSubscriptionCommand;
import system_design.command.RenewSubscriptionCommand;
import system_design.status.SubscriptionStatusDao;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @GetMapping("/{subscriptionId}/create")
    public CompletableFuture<String> createSubscription(@PathVariable("subscriptionId") Integer subscriptionId,
                                                        @RequestParam("owner") String owner,
                                                        @RequestParam("durationDays") Integer durationDays) {
        final Instant startDate = Instant.now();
        final Instant finishDate = startDate.plus(durationDays, DAYS);

        return commandGateway.send(new CreateSubscriptionCommand(subscriptionId, owner, startDate, finishDate))
                .thenApply(ign -> String.format("Subscription with id %s was created.", subscriptionId));
    }

    @GetMapping("/{subscriptionId}/renew")
    public CompletableFuture<String> renewSubscription(@PathVariable("subscriptionId") Integer subscriptionId,
                                                       @RequestParam("durationDays") Integer durationDays) {
        return commandGateway.send(new RenewSubscriptionCommand(subscriptionId, Duration.of(durationDays, DAYS)))
                .thenApply(ign -> String.format("Subscription with id %s was renewed for %s days.", subscriptionId, durationDays));
    }

    @GetMapping("/{subscriptionId}")
    public CompletableFuture<SubscriptionStatusDao> findOne(@PathVariable("subscriptionId") Integer subscriptionId) {
        return queryGateway.query("findOne", subscriptionId, ResponseTypes.instanceOf(SubscriptionStatusDao.class));
    }
}
