package models;

import lombok.RequiredArgsConstructor;
import org.bson.Document;

@RequiredArgsConstructor
public class User {
    public final int id;
    public final String name;
    public final String login;
    public final String currency;

    public User(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"), doc.getString("login"), doc.getString("currency"));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
