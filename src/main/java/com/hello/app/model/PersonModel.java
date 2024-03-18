package com.hello.app.model;

public class PersonModel {
    /*
        O sistema deve prover um espaço para cadastro do usuário.
        Você deve definir os atributos do usuário, que devem ser no mínimo 3
    */
    private String name;
    private String gender;
    private String id;
    private int age;

    public PersonModel(String name, String gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getId() {
      return this.id;
    }

    public String getName () {
        return this.name;
    }

    public String getGender () {
        return this.gender;
    }

    public int getAge () {
        return this.age;
    }
}
