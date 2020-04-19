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
import system_design.util.Currency;
import system_design.user.UserInfo;
import system_design.user.UserService;

@RestController()
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final UserService userService;

    @GetMapping("/{userId}/create")
    public String createUser(@PathVariable("userId") long userId, @RequestParam("name") String name) throws JsonProcessingException {
        return MAPPER.writeValueAsString(userService.createUser(new UserInfo(userId, name)));
    }

    @GetMapping("/{userId}/addCurrency")
    public void createUser(@PathVariable("userId") long userId,
                           @RequestParam("currency") Currency currency,
                           @RequestParam("count") long count) throws JsonProcessingException {
        userService.makeDeposit(userId, currency, count);
    }

    @GetMapping("/{userId}/buyStocks")
    public void buyStocks(@PathVariable("userId") long userId,
                          @RequestParam("companySymbol") String companySymbol,
                          @RequestParam("count") long count) throws JsonProcessingException {
        userService.buyStocks(userId, companySymbol.toUpperCase(), count);

    }

    @GetMapping("/{userId}/soldStocks")
    public void soldStocks(@PathVariable("userId") long userId,
                           @RequestParam("companySymbol") String companySymbol,
                           @RequestParam("count") long count) throws JsonProcessingException {
        userService.soldStocks(userId, companySymbol.toUpperCase(), count);
    }

    @GetMapping("/{userId}/getStocks")
    public String getStocks(@PathVariable("userId") long userId) throws JsonProcessingException {
        return MAPPER.writeValueAsString(userService.getStocksInfos(userId));
    }

    @GetMapping("/{userId}/getCurrencies")
    public String getCurrencies(@PathVariable("userId") long userId) throws JsonProcessingException {
        return MAPPER.writeValueAsString(userService.getCurrenciesInfos(userId));
    }

    @GetMapping("/{userId}/getStocksCostWithCurrency")
    public Double getStocksCostWithCurrency(@PathVariable("userId") long userId, @RequestParam("currency") Currency currency) {
        return userService.getAllStocksCostWithCurrency(userId, currency);
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") long userId) throws JsonProcessingException {
        return MAPPER.writeValueAsString(userService.getUser(userId));
    }

}
