package mongo;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import models.Product;
import models.User;
import org.bson.Document;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class ReactiveMongoDriver {
    private static final String DB_NAME = "rxtest";

    private MongoClient mongoClient;

    public ReactiveMongoDriver() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public static MongoCollection<Document> getCollection(MongoClient mongoClient, String collectionName) {
        return mongoClient.getDatabase(DB_NAME).getCollection(collectionName);
    }

    public Success addRow(MongoCollection<Document> collection, Document doc){
        return collection.insertOne(doc).timeout(1000, TimeUnit.MILLISECONDS).toBlocking().single();
    }

    public static Observable<User> getAllUsers(MongoCollection<Document> collection) {
        return collection.find().toObservable().map(User::new);
    }

    public static Observable<Product> getAllProducts(MongoCollection<Document> collection) {
        return collection.find().toObservable().map(Product::new);
    }

}
