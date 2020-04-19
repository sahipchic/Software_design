package system_design;

import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Container
    private static final GenericContainer simpleWebServer = new FixedHostPortGenericContainer("stocks:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080);


    @Test
    @Order(1)
    public void createCompanies() throws Exception {
        adminCreateCompany("YNDX", "Yandex");
        adminCreateCompany("MFGP", "Micro");
        adminCreateCompany("PUMP", "Petro");
        adminSetPrice("YNDX", 30.0);
        adminSetPrice("MFGP", 5.0);
        adminSetPrice("PUMP", 10.0);



        addCompany("yndx", 200);
        addCompany("pump", 200);
        addCompany("mfgp", 200);
        addCompany("qwerty", 500);
    }

    @Test
    @Order(2)
    public void addStocksToCompanies() throws Exception {
        addStocks("yndx", 200);
        addStocks("pump", 200);
        addStocks("mfgp", 200);

        checkCompanies();
    }

    @Test
    @Order(3)
    public void createUsers() throws Exception {
        createUser(1, "ilyja", 200);
        createUser(2, "masha", 200);
        createUser(3, "vika", 200);
        createUser(1, "ilyja", 500);

        final ArrayNode users = MAPPER.createArrayNode();
        users.add(getUser(1));
        users.add(getUser(2));
        users.add(getUser(3));
        Assertions.assertEquals(MAPPER.readValue(new File("src/test/resources/usersAfterCreate.json"), JsonNode.class), users);
    }

    @Test
    @Order(4)
    public void addMoneyToUsers() throws Exception {
        addMoney(1, "USD", 200, 200);
        addMoney(1, "USDX", 200, 400);
        addMoney(2, "EUR", 40, 200);
        addMoney(3, "USD", 153, 200);


        final ArrayNode users = MAPPER.createArrayNode();
        users.add(getUser(1));
        users.add(getUser(2));
        users.add(getUser(3));
        Assertions.assertEquals(MAPPER.readValue(new File("src/test/resources/usersAfterCreateAddMoney.json"), JsonNode.class), users);
    }

    @Test
    @Order(5)
    public void buyStocks() throws Exception {
        buyStocks(1, "yndx", 1, 200);
        buyStocks(2, "yndx", 1, 500); // не та валюта
        buyStocks(1, "yndx", 1000, 500); // недостаточно средств


        final JsonNode expected = MAPPER.readValue(new File("src/test/resources/usersAfterBoughtStocks.json"), JsonNode.class);
        Assertions.assertEquals(expected, getUser(1));
    }

    private void buyStocks(int id, String symbol, int count, int statusCode) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("user/" + id + "/buyStocks?companySymbol=" + symbol + "&count=" + count);
        Assertions.assertEquals(statusCode, response.statusCode());
    }

    private JsonNode getUser(int id) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("user/" + id);
        return MAPPER.readValue(response.body(), JsonNode.class);
    }

    private void addMoney(int id, String currency, int count, int statusCode) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("user/" + id + "/addCurrency?currency=" + currency + "&count=" + count);
        Assertions.assertEquals(statusCode, response.statusCode());
    }

    private void createUser(int id, String name, int statusCode) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("user/" + id + "/create?name=" + name);
        Assertions.assertEquals(statusCode, response.statusCode());
    }

    private void addCompany(String companySymbol, int statusCode) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("company/" + companySymbol + "/add");
        Assertions.assertEquals(statusCode, response.statusCode());
    }

    private void adminCreateCompany(String companySymbol, String name) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("admin/" + companySymbol + "/add?name=" + name + "&currency=USD");
    }

    private void adminSetPrice(String companySymbol, double price) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("admin/" + companySymbol + "/setStockPrice?price=" + price);
    }

    private void addStocks(String companySymbol, int count) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = makeHttpRequest("company/" + companySymbol + "/increaseStocks?stocksCount=" + count);
        Assertions.assertEquals(200, response.statusCode());
    }

    private void checkCompanies() throws Exception {
        HttpResponse<String> response = makeHttpRequest("company/");
        try (BufferedReader expected = new BufferedReader(new FileReader("src/test/resources/companies.json"))) {
            Assertions.assertEquals(MAPPER.readValue(expected, JsonNode.class), MAPPER.readValue(response.body(), JsonNode.class));
        }
    }

    private HttpResponse<String> makeHttpRequest(String path) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/" + path))
                .GET()
                .build();


        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

}