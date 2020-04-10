package com.company;

public class Movie {

    private String name;

    public Movie(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return " | " + name + " | ";
    }
}
