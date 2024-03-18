package com.hello.app.controller;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.hello.app.database.MongoDB;
import com.hello.app.model.CityModel;
import com.hello.app.model.EventModel;
import com.mongodb.client.MongoCollection;

public class CityController {
  private MongoDB database;
  MongoCollection<Document> cityCollection;

  public CityController() {
    this.database = new MongoDB();
    this.cityCollection = this.collection();
  }

  private MongoCollection<Document> collection() {
    return this.database.start("city");
  }

  public boolean doesCityExists() {
    return this.cityCollection.find().iterator().hasNext();
  }

  public Document getCity () {
    return this.cityCollection.find().iterator().next();
  }

  public void setCity(CityModel city) {
    String name = city.getName();
    boolean isValidName = name.length() > 0 && name.length() < 150;
    if (isValidName) {
      try {
        List<EventModel> events = new ArrayList<>();
        Document document = new Document("name", name)
          .append("events", events);
        this.cityCollection.insertOne(document);
      } catch (Exception e) {
        throw new Error("Erro sua requisição.\n" + e);
      }
    } else {
      throw new Error("Cidade com dados inválidos para o sistema.\n");
    }
  }

  public CityModel documentToCity(Document document) {
    String name = document.getString("name");
    return new CityModel(name);
  }
}
