package com.hello.app.model;
import java.time.LocalDateTime;

public class EventModel {
    /*
        Deve ser possível cadastrar eventos, definindo um horário (dentre outros atributos)
        Estes eventos devem ter, obrigatoriamente, os atributos: nome, endereço, categoria, horário e descrição
        Você deve delimitar as categorias para criação de eventos (festas, eventos esportivos, shows, entre outros exemplos)
     */
    public enum Category {
        FESTAS,
        EVENTOS_ESPORTIVOS,
        SHOWS,
        OUTROS
    }

    private String name;
    private String description;
    private String address;
    private Category category;
    private LocalDateTime date;

    public EventModel (String name, String description, String address, Category category, LocalDateTime date) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
    }

    public String getName () {
        return this.name;
    }

    public String getDescription () {
        return this.description;
    }

    public String getAddress () {
        return this.address;
    }

    public Category getCategory () {
        return this.category;
    }

    public LocalDateTime getDate () {
        return this.date;
    }
}
