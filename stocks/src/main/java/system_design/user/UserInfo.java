package system_design.user;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Wither;

@Entity
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Wither
public class UserInfo {
    @JsonIgnore
    private final static ObjectMapper MAPPER = new ObjectMapper();

    @Id
    private long id;
    @Description("Имя")
    private String name;
    @Description("валюты и их кол-во")
    private String currencies;
    @Description("Акции и их кол-во")
    private String stocks;

    public UserInfo(long id, @NonNull String name) {
        this.id = id;
        this.name = name;
        setCurrencies(new HashMap<>());
        setStocks(new HashMap<>());

    }

    @JsonIgnore
    @SneakyThrows
    public Map<String, Double> getCurrenciesMap() {
        return MAPPER.readValue(currencies, HashMap.class);
    }

    @SneakyThrows
    public void setCurrencies(Map<String, Double> currencies) {
        this.currencies = MAPPER.writeValueAsString(currencies);
    }

    @JsonIgnore
    @SneakyThrows
    public Map<String, Long> getStocksMap() {
        final HashMap<String, Integer> hashMap = MAPPER.readValue(stocks, HashMap.class);
        return hashMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().longValue()));
    }

    public void setStocks(Map<String, Long> currencies) {
        try {
            this.stocks = MAPPER.writeValueAsString(currencies);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
