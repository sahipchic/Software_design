package system_design.company;

import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;
import system_design.util.Currency;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
@EqualsAndHashCode
@ToString
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @Description("Под каким символом торгуется акция компании")
    private String symbol;
    @Description("Название компании")
    private String name;
    @Description("Валюта в которой торгуются акции компании")
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Description("Количество акций")
    private Long stocksCount;
}
