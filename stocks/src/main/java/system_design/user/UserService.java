package system_design.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import system_design.util.Currency;
import system_design.api.StockMarketStubApi;
import system_design.company.CompanyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService {

    private final UserInfoRepository userRepository;
    private final CompanyService companyService;
    private final StockMarketStubApi stockMarketStubApi;

    public UserInfo createUser(UserInfo user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalStateException("user with this id already exists");
        }
        userRepository.save(user);
        return user;
    }

    public UserInfo getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User with id " + userId + " doesn't exists"));
    }

    public void makeDeposit(long userId, Currency currency, double count) {
        final UserInfo user = getUser(userId);
        final double oldCount = user.getCurrenciesMap().getOrDefault(currency.name(), 0.0);
        final double newCount = oldCount + count;
        final Map<String, Double> currencies = new HashMap<>(user.getCurrenciesMap());
        currencies.put(currency.name(), newCount);
        user.setCurrencies(currencies);
        userRepository.save(user);
    }


    public Map<String, Double> getCurrenciesInfos(long userId) {
        final UserInfo user = getUser(userId);
        return user.getCurrenciesMap();
    }

    public List<StockInfo> getStocksInfos(long userId) {
        final UserInfo user = getUser(userId);
        List<StockInfo> stockInfos = new ArrayList<>();
        for (var it : user.getStocksMap().entrySet()) {
            final String symbol = it.getKey();
            final Long value = it.getValue();
            final Currency currency = companyService.findCompany(symbol).getCurrency();
            final double currentPrice = companyService.getPrice(symbol);
            stockInfos.add(new StockInfo(symbol, value, currentPrice, currency));
        }
        return stockInfos;
    }

    public Double getAllStocksCostWithCurrency(long userId, Currency currency) {
        final List<StockInfo> stocksInfos = getStocksInfos(userId);
        return stocksInfos.stream().map(x -> x.getCount() * x.getPriceOne() * stockMarketStubApi.exchangeCurrencyCoef(x.getCurrency(), currency))
                .reduce(0.0, Double::sum);
    }

    public void buyStocks(long userId, String companySymbol, long count) {
        final UserInfo user = getUser(userId);
        final double currentPrice = companyService.getPrice(companySymbol);
        final Currency currency = companyService.findCompany(companySymbol).getCurrency();
        final Double userCurrencyCount = user.getCurrenciesMap().getOrDefault(currency.name(), 0.0);
        if (userCurrencyCount < count * currentPrice) {
            throw new IllegalStateException("you haven't enough money");
        }
        companyService.decreaseStocksToCompany(companySymbol, count);
        final Map<String, Double> currencies = new HashMap<>(user.getCurrenciesMap());
        final Map<String, Long> stocks = new HashMap<>(user.getStocksMap());
        currencies.put(currency.name(), userCurrencyCount - currentPrice * count);
        user.setCurrencies(currencies);
        stocks.put(companySymbol, user.getStocksMap().getOrDefault(companySymbol, 0L) + count);
        user.setStocks(stocks);
        userRepository.save(user);

    }

    public void soldStocks(long userId, String companySymbol, long count) {
        final UserInfo user = getUser(userId);
        if (user.getStocksMap().getOrDefault(companySymbol, 0L) < count) {
            throw new IllegalStateException("you haven't enough stocks");
        }
        companyService.increaseStocksToCompany(companySymbol, count);

        final double currentPrice = companyService.getPrice(companySymbol);
        final Currency currency = companyService.findCompany(companySymbol).getCurrency();
        final Double userCurrencyCount = user.getCurrenciesMap().getOrDefault(currency.name(), 0.0);

        final Map<String, Double> currencies = new HashMap<>(user.getCurrenciesMap());
        final Map<String, Long> stocks = new HashMap<>(user.getStocksMap());

        currencies.put(currency.name(), userCurrencyCount + currentPrice * count);
        stocks.put(companySymbol, user.getStocksMap().getOrDefault(companySymbol, 0L) - count);
        user.setStocks(stocks);
        user.setCurrencies(currencies);
        userRepository.save(user);
    }

    private int foo() {
        return 5;
    }

}
