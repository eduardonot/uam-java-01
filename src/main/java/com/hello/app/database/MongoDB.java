package com.hello.app.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class MongoDB {

  public MongoCollection<Document> start(String collectionName) {
    try {
      MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
      MongoDatabase database = mongoClient.getDatabase("uam");

      MongoCollection<Document> collection = database.getCollection(collectionName);

      return collection;
    } catch (Exception e) {
      throw new Error("Erro ao conectar com o Banco de Dados.\n" + e);
    }
  }
}