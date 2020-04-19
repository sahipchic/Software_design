package system_design.history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import system_design.controller.ReportController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ComputeStatisticsTest {



    @Test
    public void getAvgDuration() {
        final List<SubscriptionHistoryDao> subscriptionHistories = List.of(
                new SubscriptionHistoryDao(1L, 1, Instant.parse("2007-12-03T10:15:30.00Z"), SubscriptionHistoryDao.Action.ENTER),
                new SubscriptionHistoryDao(2L, 1, Instant.parse("2007-12-03T10:16:30.00Z"), SubscriptionHistoryDao.Action.EXIT),
                new SubscriptionHistoryDao(3L, 2, Instant.parse("2007-12-03T10:15:30.00Z"), SubscriptionHistoryDao.Action.ENTER),
                new SubscriptionHistoryDao(4L, 2, Instant.parse("2007-12-03T10:16:00.00Z"), SubscriptionHistoryDao.Action.EXIT)
        );

        Assertions.assertEquals(45.0, ReportController.getAvgDuration(subscriptionHistories));
    }

    @Test
    public void getAvgDurationWIthEmptyList() {
        final List<SubscriptionHistoryDao> subscriptionHistories = List.of(
        );

        Assertions.assertEquals(0.0, ReportController.getAvgDuration(subscriptionHistories));
    }

    @Test
    public void getStatisticsWithEmptyList() {
        final List<SubscriptionHistoryDao> subscriptionHistories = List.of(
       );

        Assertions.assertEquals(Collections.emptyMap(), ReportController.getStatistics(subscriptionHistories));
    }
    @Test
    public void getStatistics() {
        final List<SubscriptionHistoryDao> subscriptionHistories = List.of(
                new SubscriptionHistoryDao(1, Instant.parse("2007-12-03T10:15:30.00Z"), SubscriptionHistoryDao.Action.ENTER),
                new SubscriptionHistoryDao(1, Instant.parse("2007-12-03T10:16:30.00Z"), SubscriptionHistoryDao.Action.EXIT),
                new SubscriptionHistoryDao(2, Instant.parse("2007-12-03T10:15:30.00Z"), SubscriptionHistoryDao.Action.ENTER),
                new SubscriptionHistoryDao(2, Instant.parse("2007-12-03T10:16:00.00Z"), SubscriptionHistoryDao.Action.EXIT),
                new SubscriptionHistoryDao(2, Instant.parse("2007-03-03T10:15:30.00Z"), SubscriptionHistoryDao.Action.ENTER),
                new SubscriptionHistoryDao(2, Instant.parse("2007-03-03T10:16:00.00Z"), SubscriptionHistoryDao.Action.EXIT)
        );

        Assertions.assertEquals(Map.of(LocalDate.parse("2007-03-03"), 1L, LocalDate.parse("2007-12-03"), 2L), ReportController.getStatistics(subscriptionHistories));
    }
}