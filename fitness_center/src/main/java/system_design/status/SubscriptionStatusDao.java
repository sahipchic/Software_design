package system_design.status;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionStatusDao implements Serializable {

    @Id
    private Integer subscriptionId;

    private String owner;
    private Instant startDate;
    private Instant finishDate;

}
