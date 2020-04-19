package system_design.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import system_design.util.Currency;
import system_design.company.Company;

@Component
// класс затычка для добавления компаний на рынок и установку цен на акции
public class StockMarketStubApi {

    public final static Map<String, Double> mapCompanyNameToStockPrice = new HashMap<>();
    public final static Map<String, Company> mapCompanyNameToCompany = new HashMap<>();

    public Optional<Company> findCompany(String symbol) {
        return Optional.ofNullable(mapCompanyNameToCompany.get(symbol));
    }

    public double getStockPrice(String symbol) {
        return mapCompanyNameToStockPrice.get(symbol);
    }

    public double exchangeCurrencyCoef(Currency from, Currency to) {
        if (from == to) {
            return 1;
        }

        if (from == Currency.USD && to == Currency.RUB) {
            return  70;
        } else if (from == Currency.USD && to == Currency.EUR) {
            return  1.1;
        } else if (from == Currency.EUR && to == Currency.RUB) {
            return  80;
        } else if (from == Currency.EUR && to == Currency.USD) {
            return  1/ 1.1;
        } else if (from == Currency.RUB && to == Currency.EUR) {
            return 1.0/80;
        } else if (from == Currency.RUB && to == Currency.USD) {
            return 1.0/70;
        }
        throw new RuntimeException();
    }
}
