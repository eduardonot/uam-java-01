package com.hello.app.controller;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.hello.app.database.MongoDB;
import com.hello.app.model.EventModel.Category;
import com.hello.app.view.EventView;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class EventController {
  private MongoDB database;
  MongoCollection<Document> cityCollection;
  static EventView eventView = new EventView();
  static PersonController personController = new PersonController();

  public EventController() {
    this.database = new MongoDB();
    this.cityCollection = this.collection();
  }

  private MongoCollection<Document> collection() {
    return this.database.start("city");
  }

  public Document getOne(String id) {
    return this.cityCollection.find(
      Filters.eq("_id", new ObjectId(id))
    ).first();
  }

  public Document getEvents () {
    Document document = this.cityCollection.find().first();
    
    Object events = document.get("events");
    
    if (events instanceof List) {
      List<Document> eventList = (List<Document>) events;

      eventList.sort((e1, e2) -> {
        LocalDateTime date1 = LocalDateTime.parse((String) e1.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        LocalDateTime date2 = LocalDateTime.parse((String) e2.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        return date1.compareTo(date2);
      });

      eventView.displayEvents(eventList);
      String selectedOption = eventView.inputEventData("Digite o número do evento que gostaria de participar:");
      int option = Integer.parseInt(selectedOption);
      try {
        String confirmSelection = eventView.inputEventData("\nConfirmar seleção de " + eventList.get(option).getString("name") + "?\nDigite S/N:").toLowerCase();
        switch (confirmSelection) {
          case "s":
            return eventList.get(option);
          default:
            break;
        }
      } catch (Exception e) {
        System.out.println("Evento não encontrado.");
      }
    }
    return document;
  }

  private boolean isValidCategory(String value) {
    for (Category category: Category.values()) {
      if (category.name().equals(value.toUpperCase())) {
        return true;
      }
    }
    return false;
  }

  public void setEvent(String name, String description, String address, String category, String eventDate, String eventTime) {
    boolean isValidName = name.length() > 0 && name.length() < 50;
    boolean isValidDescription = description.length() >= 0 && description.length() < 100;
    boolean isValidAddress = address.length() > 0 && address.length() < 100;
    boolean isValidDate = eventDate.length() == 10;
    boolean isValidTime = eventTime.length() == 5;
    boolean isValidCategory = isValidCategory(category);

    if (isValidName && isValidDescription && isValidAddress && isValidDate && isValidTime && isValidCategory) {
      try {
        // convert date to right format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime date = LocalDate.parse(eventDate, dateFormatter).atStartOfDay();
        
        // convert time to right format
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime time = LocalTime.parse(eventTime, timeFormatter).atDate(date.toLocalDate());

        // join date and time
        DateTimeFormatter correctDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String dateTimeString = correctDateTime.format(time);

        Document eventDocument = new Document("name", name)
          .append("description", description)
          .append("address", address)
          .append("category", category)
          .append("date", dateTimeString);
        
        Document city = cityCollection.find().first();
        String cityName = city.getString("name");
        cityCollection.updateOne(Filters.eq("name", cityName), Updates.addToSet("events", eventDocument));
      } catch (Exception e) {
        throw new Error("Erro sua requisição.\n" + e);
      }
    } else {
      throw new Error("Evento com dados inválidos.\n");
    }
  }
}
