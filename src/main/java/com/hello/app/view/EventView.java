package com.hello.app.view;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

public class EventView {
  private Scanner scanner;

  public EventView() {
    this.scanner = new Scanner(System.in);
  }

  private static String handleEventStatusMessage (boolean eventStatus) {
    if (eventStatus == true) {
      return " (evento finalizado).";
    } else {
      return "";
    }
  }

  public void displayEvents(List<Document> eventList) {
    int i = 0;
    for (Document event: eventList) {
      String name = event.getString("name");
      String date = event.getString("date");
      LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);

      LocalDateTime now = LocalDateTime.now();
      boolean isLateEvent = dateTime.isBefore(now);

      String stringDate = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      String stringTime = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

      System.out.println(i + " - " + name + " - " + stringDate + " as " + stringTime + handleEventStatusMessage(isLateEvent));
      i++;
    }
  }

  public String inputEventOption() {
    System.out.println("Eventos da cidade");
    System.out.println("Digite o número desejado:");
    System.out.println("1 - Listar eventos");
    System.out.println("2 - Meus eventos confirmados");
    System.out.println("3 - Cadastrar eventos");
    System.out.println("4 - Sair");
    return this.scanner.nextLine();
  }

  public String inputEventData (String message) {
    System.out.println(message);
    return scanner.nextLine();
  }
}
