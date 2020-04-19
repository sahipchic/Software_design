package system_design.history;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionHistoryDao implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private Integer subscriptionId;
    private Instant timestamp;
    private Action action;

    public SubscriptionHistoryDao(Integer subscriptionId, Instant timestamp, Action action) {
        this.subscriptionId = subscriptionId;
        this.timestamp = timestamp;
        this.action = action;
    }

    public enum Action {
        EXIT, ENTER
    }
}
