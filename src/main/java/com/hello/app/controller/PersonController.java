package com.hello.app.controller;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.hello.app.database.MongoDB;
import com.hello.app.model.EventModel;
import com.hello.app.model.PersonModel;
import com.hello.app.view.PersonView;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class PersonController {
  private MongoDB database;
  private PersonView personView = new PersonView();
  MongoCollection<Document> userCollection;

  public PersonController() {
    this.database = new MongoDB();
    this.userCollection = this.collection();
  }

  private MongoCollection<Document> collection() {
    return this.database.start("user");
  }

  public boolean isUserSignedUp() {
    return this.userCollection.find().iterator().hasNext();
  }

  public Document getPerson () {
    return this.userCollection.find().iterator().next();
  }

  public void getPersonEvents(String id) {
    Bson filter = Filters.eq("_id", new ObjectId(id));
    Document user = this.userCollection.find(filter).first();
    Object userEvents = user.get("events");
    if (userEvents instanceof List) {
      int i = 0;
      List<Document> eventList = (List<Document>) userEvents;

      eventList.sort((e1, e2) -> {
        LocalDateTime date1 = LocalDateTime.parse((String) e1.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        LocalDateTime date2 = LocalDateTime.parse((String) e2.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        return date1.compareTo(date2);
      });

      for (Document event: eventList) {
        String name = event.getString("name");
        String date = event.getString("date");
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        String stringDate = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String stringTime = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
  
        System.out.println(i + " - " + name + " - " + stringDate + " as " + stringTime);
        i++;
      }
    }
  }

  public void updatePersonEvent(String id, Document event) {
    Bson filter = Filters.eq("_id", new ObjectId(id));
    Document user = this.userCollection.find(filter).first();
    Object userEvents = user.get("events");

    if (userEvents instanceof List) {
      List<Document> eventList = (List<Document>) userEvents;
      int eventId = eventList.indexOf(event);
      if (eventId == -1) {
        eventList.add(event);
      } else {
        String selectedOption = personView.inputPersonData("\n\nVocê já confirmou presença nesse evento. Gostaria de desmarcar? (S/N):").toLowerCase();
        switch (selectedOption) {
          case "s":
            eventList.remove(eventId);
            break;
          default:
            break;
        }
      }
      Bson updateOperation = Updates.set("events", eventList);
      this.userCollection.findOneAndUpdate(filter, updateOperation);
    }
  }

  public Document setPerson(PersonModel person) {
    String name = person.getName();
    String gender = person.getGender();
    int age = person.getAge();

    boolean isValidName = name.length() > 0 && name.length() < 150;
    boolean isValidGender = gender.length() > 0 && gender.length() < 15;
    boolean isValidAge = age > 0 && age < 120;

    if (isValidName && isValidGender && isValidAge) {
      try {
        List<EventModel> events = new ArrayList<>();
        Document document = new Document("name", name)
          .append("age", age)
          .append("gender", gender)
          .append("events", events);

          this.userCollection.insertOne(document);
          return document;
        } catch (Exception e) {
        throw new Error("Erro sua requisição.\n" + e);
      }
    } else {
      throw new Error("Usuário com dados inválidos para o sistema.\n");
    }
  }

  public PersonModel documentToPerson(Document document) {
    String name = document.getString("name");
    int age = document.getInteger("age");
    String gender = document.getString("gender");
    PersonModel person = new PersonModel(name, gender, age);
    return person;
  }
}
