package com.hello.app.view;
import java.util.Scanner;

public class CityView {
  private Scanner scanner;

  public CityView() {
    this.scanner = new Scanner(System.in);
  }

  public String inputCityData (String message) {
    System.out.println(message);
    return scanner.nextLine();
  }
}
