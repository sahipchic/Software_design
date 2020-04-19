package system_design.controller;

import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system_design.command.EnterClientCommand;
import system_design.command.ExitClientCommand;

@Slf4j
@RestController
@RequestMapping("/gate")
@RequiredArgsConstructor
public class GateController {
    private final CommandGateway commandGateway;

    @GetMapping("/{subscriptionId}/enter")
    public CompletableFuture<String> enter(@PathVariable("subscriptionId") Integer subscriptionId) throws Exception {
        return commandGateway
                .send(new EnterClientCommand(subscriptionId))
                .thenApply(x -> "Client with id " + subscriptionId + " enter");
    }

    @GetMapping("/{subscriptionId}/exit")
    public CompletableFuture<String> exit(@PathVariable("subscriptionId") Integer subscriptionId) throws Exception {
        return commandGateway.send(new ExitClientCommand(subscriptionId))
                .thenApply(x -> "Client with id " + subscriptionId + " exit");
    }
}
