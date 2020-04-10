package com.company;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Show {

    private Date date;
    private Time time;
    private Movie movie;
    private Room room;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    public Show(Date date, Time time, Movie movie, Room room) {
        this.date = date;
        this.time = time;
        this.movie = movie;
        this.room = room;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Movie getMovie() {
        return movie;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Show{" +
                "movie=" + movie +
                ", room=" + room +
                '}';
    }
}
