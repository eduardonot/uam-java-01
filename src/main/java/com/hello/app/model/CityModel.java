package com.hello.app.model;
import java.util.ArrayList;
import java.util.List;

public class CityModel {
    private String name;
    private List<PersonModel> inhabitants;
    private List <EventModel> events;

    public CityModel (String name) {
        this.name = name;
        this.inhabitants = new ArrayList<>();
    }

    public String getName () {
        return this.name;
    }

    public int getInhabitantsAmount () {
        return this.inhabitants.size();
    }

    public List<PersonModel> getInhabitants () {
        return this.inhabitants;
    }

    public void setInhabitants(PersonModel person) {
        this.inhabitants.add(person);
    }

    public void getEvents () {
        for (int i = 0; i < this.events.size(); i++) {
            System.out.println(i + " - " + this.events.get(i));
        }
    }

    public void setNewEvent (EventModel event) {
        this.events.add(event);
    }

}
