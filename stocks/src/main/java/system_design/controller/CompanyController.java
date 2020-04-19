package system_design.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import system_design.company.Company;
import system_design.company.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompanyController {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final CompanyService companyService;

    @GetMapping("/")
    public List<Company> companies() {
        return companyService.findAll();
    }

    @GetMapping("/{symbol}/add")
    public String addCompany(@PathVariable("symbol") String symbol) throws JsonProcessingException {
        final Company company = companyService.addCompany(symbol.toUpperCase());
        return MAPPER.writeValueAsString(company);
    }

    @GetMapping("/{symbol}")
    public String getCompany(@PathVariable("symbol") String symbol) throws JsonProcessingException {
        return MAPPER.writeValueAsString(companyService.findCompany(symbol.toUpperCase()));
    }

    @GetMapping("/{symbol}/increaseStocks")
    public String increaseStockToCompany(@PathVariable("symbol") String symbol, @RequestParam("stocksCount") Long stocksCount) throws JsonProcessingException {
        companyService.increaseStocksToCompany(symbol.toUpperCase(), stocksCount);
        return "OK";
    }

    @GetMapping("/{symbol}/decreaseStocks")
    public String decreaseStockToCompany(@PathVariable("symbol") String symbol, @RequestParam("stocksCount") Long stocksCount) throws JsonProcessingException {
        companyService.decreaseStocksToCompany(symbol.toUpperCase(), stocksCount);
        return "OK";
    }

    @GetMapping("/{symbol}/price")
    public double getStocksPrice(@PathVariable("symbol") String symbol) {
        return companyService.getPrice(symbol.toUpperCase());
    }
}
