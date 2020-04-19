package system_design.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import system_design.api.StockMarketStubApi;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final StockMarketStubApi stockMarketStubApi;

    public Company addCompany(String symbol) {
        if (companyRepository.findById(symbol).isPresent()) {
            throw new IllegalStateException("Company with symbol " + symbol + " already was added");
        }

        final Optional<Company> companyInfoOpt = stockMarketStubApi.findCompany(symbol);
        if (companyInfoOpt.isEmpty()) {
            throw new IllegalStateException("Company with symbol " + symbol + " don't found");
        }
        Company company = companyInfoOpt.get();
        companyRepository.save(company);
        return company;
    }

    public void increaseStocksToCompany(String companySymbol, Long stocksCount) {
        final Company company = companyRepository.findById(companySymbol)
                .map(x -> x.withStocksCount(x.getStocksCount() + stocksCount))
                .orElseThrow(() -> new IllegalStateException("Company with symbol " + companySymbol + " already was added"));
        companyRepository.save(company);
    }

    public void decreaseStocksToCompany(String companySymbol, Long stocksCount) {
        final Company company = companyRepository.findById(companySymbol)
                .map(x -> {
                    if (x.getStocksCount() < stocksCount) {
                        throw new IllegalStateException("Stock in stock market less than " + stocksCount);
                    }
                    return x.withStocksCount(x.getStocksCount() - stocksCount);
                })
                .orElseThrow(() -> new IllegalStateException("Company with symbol " + companySymbol + " already was added"));
        companyRepository.save(company);
    }

    public Company findCompany(String symbol) {
        return companyRepository.findById(symbol).orElse(null);
    }

    public double getPrice(String symbol) {
        return stockMarketStubApi.getStockPrice(symbol);
    }

    public List<Company> findAll() {
        final ArrayList<Company> companies = Lists.newArrayList(companyRepository.findAll());
        companies.sort(Comparator.comparing(Company::getSymbol));
        return companies;
    }
}
