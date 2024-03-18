package com.hello.app;

import org.bson.Document;
import com.hello.app.model.PersonModel;
import com.hello.app.view.PersonView;
import com.hello.app.controller.PersonController;
import com.hello.app.model.CityModel;
import com.hello.app.model.EventModel;
import com.hello.app.view.CityView;
import com.hello.app.view.EventView;
import com.hello.app.controller.CityController;
import com.hello.app.controller.EventController;

public class Main {
    static PersonView personView = new PersonView();
    static PersonController personController = new PersonController();
    static PersonModel user;

    static CityView cityView = new CityView();
    static CityController cityController = new CityController();
    static CityModel city;

    static EventView eventView = new EventView();
    static EventController eventController = new EventController();
    static EventModel event;

    private static PersonModel handlePerson() {
      String name = personView.inputPersonData("Insira seu nome:");
      String stringAge = personView.inputPersonData("Insira sua idade:");
      int age = Integer.parseInt(stringAge);
      String gender = personView.inputPersonData("Insira seu genero:");

      PersonModel person = new PersonModel(name, gender, age);
      Document personResponse = personController.setPerson(person);
      String personId = personResponse.get("_id").toString();
      person.setId(personId);
      return person;
    }

    private static CityModel handleCity() {
      String name = cityView.inputCityData("Insira a cidade onde mora:");

      CityModel city = new CityModel(name);
      cityController.setCity(city);
      return city;
    }

    private static void setEvent() {
      String name = eventView.inputEventData("Nome do evento:");
      String description = eventView.inputEventData("Descrição:");
      String address = eventView.inputEventData("Local:");
      String category = eventView.inputEventData("Categoria: (FESTAS, EVENTOS_ESPORTIVOS, SHOWS, OUTROS)");
      String date = eventView.inputEventData("Data do evento no formato dd/mm/aaaa:");
      String time = eventView.inputEventData("Horario do evento no formato hh:mm");

      eventController.setEvent(name, description, address, category, date, time);
    }
    private static void openMainMenu() {
      String opt = eventView.inputEventOption();
      switch (opt) {
        case "1":
          System.out.println("Opção selecionada: Todos os eventos");
          Document selectedEvent = eventController.getEvents();
          personController.updatePersonEvent(user.getId(), selectedEvent);
          break;
        case "2":
          System.out.println("Opção selecionada: Meus eventos confirmados");
          personController.getPersonEvents(user.getId());
          break;
        case "3":
          System.out.println("Opção selecionada: Cadastrar eventos");
          setEvent();
          break;
        case "4":
          System.exit(0);
          break;
        default:
          System.out.println(opt + " é uma opção inválida.");
          break;
      }
      openMainMenu();
    }

    public static void main(String[] args) {

        // USER HANDLER
        boolean isUserSignedUp = personController.isUserSignedUp();
        if (!isUserSignedUp) {
          user = handlePerson();
          PersonView personView = new PersonView();
          personView.greetPerson(user);
        } else {
          Document docUser = personController.getPerson();
          String userId = docUser.get("_id").toString();
          user = personController.documentToPerson(docUser);
          user.setId(userId);
          personView.greetPerson(user);
        }

        // CITY HANDLER
        boolean doesCityExists = cityController.doesCityExists();
        if (!doesCityExists) {
          city = handleCity();
        }

        openMainMenu();
    }
}
