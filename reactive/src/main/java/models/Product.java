package models;

import lombok.RequiredArgsConstructor;
import org.bson.Document;

@RequiredArgsConstructor
public class Product {
    public final int id;
    public final String name;
    public final int amount;
    public final String currency;

    public Product(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"), doc.getInteger("amount"), doc.getString("currency"));
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
