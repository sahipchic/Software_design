package system_design.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system_design.history.SubscriptionHistoryDao;

@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final QueryGateway queryGateway;

    public static Map<LocalDate, Long> getStatistics(List<SubscriptionHistoryDao> histories) {
        return histories.stream()
                .filter(x -> x.getAction() == SubscriptionHistoryDao.Action.ENTER)
                .collect(Collectors.groupingBy(x -> LocalDate.ofInstant(x.getTimestamp(), ZoneId.of("UTC")), Collectors.counting()));


    }

    public static double getAvgDuration(List<SubscriptionHistoryDao> histories) {
        final Map<Integer, List<SubscriptionHistoryDao>> collect = histories.stream()
                .collect(Collectors.groupingBy(SubscriptionHistoryDao::getSubscriptionId));
        long duration = 0;
        int cnt = 0;
        for (var it : collect.values()) {
            it.sort(Comparator.comparing(SubscriptionHistoryDao::getId));
            SubscriptionHistoryDao prevObject = null;

            for (var v : it) {
                if (prevObject == null || v.getAction() == SubscriptionHistoryDao.Action.ENTER || prevObject.getAction() == SubscriptionHistoryDao.Action.EXIT) {
                    prevObject = v;
                    continue;
                }
                cnt++;
                duration += v.getTimestamp().getLong(ChronoField.INSTANT_SECONDS) - prevObject.getTimestamp().getLong(ChronoField.INSTANT_SECONDS);
            }
        }
        return cnt == 0 ? 0 : (double) duration / (double) cnt;
    }

    @GetMapping("/avgDuration")
    public CompletableFuture<String> avgDuration() {
        return queryGateway.query("gateHistory", null, ResponseTypes.multipleInstancesOf(SubscriptionHistoryDao.class))
                .thenApply(x -> Double.toString(getAvgDuration(x)));
    }

    @GetMapping("/fullStatistics")

    public CompletableFuture<String> fullStatistics() {
        return queryGateway.query("gateHistory", null, ResponseTypes.multipleInstancesOf(SubscriptionHistoryDao.class))
                .thenApply(this::writeToString);
    }

    @SneakyThrows
    private String writeToString(List<SubscriptionHistoryDao> x) {
        return MAPPER.writeValueAsString(getStatistics(x));
    }
}
