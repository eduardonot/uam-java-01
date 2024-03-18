package com.hello.app.view;
import java.util.Scanner;

import com.hello.app.model.PersonModel;

public class PersonView {
  private Scanner scanner;

  public PersonView() {
    this.scanner = new Scanner(System.in);
  }

  public void greetPerson (PersonModel person) {
    System.out.println("Olá, " + person.getName() + ", seja bem vindo!");
  }

  public String inputPersonData (String message) {
    System.out.println(message);
    return scanner.nextLine();
  }
}
