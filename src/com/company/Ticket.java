package com.company;

public class Ticket {

    private Customer customer;
    private Show show;
    private int bookedSeats;

    public Ticket(Customer customer, Show show, int bookedSeats) {
        this.customer = customer;
        this.show = show;
        this.bookedSeats = bookedSeats;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Show getShow() {
        return show;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }
}
