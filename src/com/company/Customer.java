package com.company;

import java.sql.*;
import java.util.ArrayList;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;
    private boolean isExisting = false;
    private ArrayList<Ticket> tickets = new ArrayList<Ticket>();

    public Customer(String email) {
        this.email = email;
    }

    public Customer(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void buyTicket(int userShowSelection, int seats) {
        String sqlCommand = "INSERT INTO `tickets`(`customer`, `cinema_show`, `seats`) " +
                "VALUES ('" + this.email + "', '" + userShowSelection + "', '" + seats + "')";
        executeUpdate(sqlCommand);
        System.out.println("Ticket gebucht");
    }

    public void cancelTicket(int userInputCancel) {
        /**TODO userInputCancel sollte anders gelöst werden, da nicht immer die ID == i aus der Schleife ist
         * */
        String sqlCommand = "DELETE FROM `tickets` WHERE `id` = " + userInputCancel;
        executeUpdate(sqlCommand);
        System.out.println("Ticket storniert!");
    }

    public void printTickets() {
        tickets.clear();
        String sqlCommand = "SELECT shows.date, shows.time, shows.movie, shows.room, tickets.seats" +
        " FROM tickets INNER JOIN shows ON shows.id=tickets.cinema_show";
        getUserTicketsFromDb(sqlCommand);

        System.out.println("\n");
        System.out.println("Gekaufte Tickets: ");
        for (int i = 0; i < tickets.size(); i++) {
            // Print ticket informations
            System.out.println((i+1) + "." + tickets.get(i).getShow().getDate() + " " + tickets.get(i).getShow().getTime() +
                    " " + tickets.get(i).getShow().getMovie().toString() + " " + tickets.get(i).getShow().getRoom() +
                    " " + tickets.get(i).getBookedSeats());
        }
    }

    public void insertCustomerToDb() {
        String sqlCommand = "INSERT INTO `customers`(`email`, `first_name`, `last_name`) " +
                "VALUES ('" + this.email + "', '" + this.firstName + "', '" + this.lastName + "')";
        executeUpdate(sqlCommand);
    }

    public boolean checkIfExists() {
        // Check if email already exists in DB
        this.isExisting = false;
        String sqlCommand = "SELECT * FROM `customers` where `email` = '" + this.email + "'";
        checkIfEmailExists(sqlCommand);
        return this.isExisting;
    }

    private void executeUpdate(String sqlCommand) {
        Statement stmt = null;
        Connection conn = DbHelper.getConnectionToDb();
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlCommand);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIfEmailExists(String sqlCommand) {
        Statement stmt = null;
        Connection conn = DbHelper.getConnectionToDb();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                Integer id = rs.getInt("id");
                if (id != null) {
                    this.isExisting = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void getUserTicketsFromDb(String sqlCommand) {
        // Get all tickets from current user
        Statement stmt = null;
        Connection conn = DbHelper.getConnectionToDb();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                String movieTitle = rs.getString("movie");
                String roomName = rs.getString("room");
                int seats = rs.getInt("seats");

                Room room = new Room(roomName);
                Movie movie = new Movie(movieTitle);
                Show show = new Show(date, time, movie, room);
                Ticket ticket = new Ticket(this, show, seats);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
