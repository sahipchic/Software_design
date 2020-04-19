package system_design.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import system_design.util.Currency;
import system_design.company.Company;
import system_design.api.StockMarketStubApi;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminController {



    @GetMapping("/{company}/setStockPrice")
    public String setStockPriceForCompany(@PathVariable("company") String company,
                                                 @RequestParam("price") double price ) {
        StockMarketStubApi.mapCompanyNameToStockPrice.put(company, price);
        return "Ok";
    }

    @GetMapping("/{company}/add")
    public String setStockPriceForCompany(@PathVariable("company") String company,
                                          @RequestParam("name") String name,
                                          @RequestParam("currency") Currency currency) {
        StockMarketStubApi.mapCompanyNameToCompany.put(company, new Company(company, name, currency, 0L));
        return "Ok";
    }
}
