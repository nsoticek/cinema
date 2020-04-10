package com.company;

public class Room {

    private String name;
    private int seats;

    public Room(String name) {
        this.name = name;
    }

    public Room(String name, int seats) {
        this.name = name;
        this.seats = seats;
    }

    @Override
    public String toString() {
        return name;
    }
}
