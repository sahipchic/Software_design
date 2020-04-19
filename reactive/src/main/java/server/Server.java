package server;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import models.Product;
import models.User;
import mongo.ReactiveMongoDriver;
import org.bson.Document;
import rx.Observable;
import rx.Subscriber;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Server {
private static final BigDecimal RATE = new BigDecimal("2");
private static MongoClient client = new ReactiveMongoDriver().getMongoClient();
private static final String USERS_COLLECTION = "user";
private static final String PRODUCTS_COLLECTION = "product";


public static void run() throws InterruptedException{
    MongoCollection<Document> userCollection = ReactiveMongoDriver.getCollection(client, USERS_COLLECTION);
    MongoCollection<Document> productCollection = ReactiveMongoDriver.getCollection(client, PRODUCTS_COLLECTION);

    HttpServer
            .newServer(8080)
            .start((req, resp) -> {
                Observable<User> responseUser;
                Observable<Product> responseProduct;
                String path = req.getDecodedPath().substring(1);
                Map<String, List<String>> queryParameters = req.getQueryParameters();
                switch (path) {
                    case "add-user":
                        resp.writeString(addUser(queryParameters, userCollection));
                    case "add-product":
                        resp.writeString(addProduct(queryParameters, productCollection));
                    case "users":
                        responseUser = ReactiveMongoDriver.getAllUsers(userCollection);
                        responseUser.subscribe(System.out::println);
                        resp.setStatus(HttpResponseStatus.OK);
                        return resp.writeString(responseUser.map(User::toString).reduce((a, b) -> a + "\n" + b));
                    case "products":
                        responseProduct = ReactiveMongoDriver.getAllProducts(productCollection);
                        responseProduct.subscribe(System.out::println);
                        resp.setStatus(HttpResponseStatus.OK);
                        return resp.writeString(responseProduct.map(Product::toString).reduce((a, b) -> a + "\n" + b));
                    default:
                        throw new IllegalArgumentException("ERROR");
                }

            })
            .awaitShutdown();

    Thread.sleep(1000);
}

private static Observable<String> addUser(Map<String, List<String>> queryParameters, MongoCollection<Document> userCollection){
    int id = Integer.valueOf(queryParameters.get("id").get(0));
    String name = queryParameters.get("name").get(0);
    String login = queryParameters.get("login").get(0);
    String currency = queryParameters.get("currency").get(0);

    Document doc = new Document("id", id)
            .append("name", name)
            .append("login", login)
            .append("currency", currency);

    final String[] status = {null};

    userCollection.insertOne(doc).subscribe(new Subscriber<Success>() {
        @Override
        public void onCompleted() {
            status[0] = "Completed";
            System.out.println("Completed");
        }

        @Override
        public void onError(final Throwable e) {
            status[0] = "Failed";
            System.out.println("Failed: " + e.toString());
        }

        @Override
        public void onNext(final Success success) {
            status[0] = "Inserted";
            System.out.println("Inserted");
        }
    });

    try {
        Thread.sleep(1000);

    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    if (status[0].equals("Completed")){
        return Observable.just("New user was added");
    }

    return Observable.just("Error");
}

private static Observable<String> addProduct(Map<String, List<String>> queryParameters, MongoCollection<Document> productCollection){
    int id = Integer.valueOf(queryParameters.get("id").get(0));
    String name = queryParameters.get("name").get(0);
    int amount = Integer.valueOf(queryParameters.get("amount").get(0));
    String currency = queryParameters.get("currency").get(0);
    System.out.println(id + name + amount + currency);
    Document doc = new Document("id", id)
            .append("name", name)
            .append("amount", amount)
            .append("currency", currency);

    final String[] status = {null};

    productCollection.insertOne(doc).subscribe(new Subscriber<Success>() {
        @Override
        public void onCompleted() {
            status[0] = "Completed";
            System.out.println("Completed");
        }

        @Override
        public void onError(final Throwable e) {
            status[0] = "Failed";
            System.out.println("Failed: " + e.toString());
        }

        @Override
        public void onNext(final Success success) {
            status[0] = "Inserted";
            System.out.println("Inserted");
        }
    });

    try {
        Thread.sleep(1000);

    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    if (status[0].equals("Completed")){
        return Observable.just("New product was added");
    }

    return Observable.just("Error");
}

private static Document createDocument(List<String> columns, List<Object> values){
    return new Document(columns.get(0), values.get(0))
            .append(columns.get(1), values.get(1))
            .append(columns.get(2), values.get(2))
            .append(columns.get(3), values.get(3));
}
}
