package system_design.user;

import lombok.Value;
import system_design.util.Currency;

@Value
public class StockInfo {

    String name;
    Long count;
    Double priceOne;
    Currency currency;
    Double priceAll;

    public StockInfo(String name, Long count, Double priceOne, Currency currency) {
        this.name = name;
        this.count = count;
        this.priceOne = priceOne;
        this.currency = currency;
        priceAll = priceOne * count;
    }
}
