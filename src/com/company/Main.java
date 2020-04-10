package com.company;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String messageRegister = "Wie ist dein Vorname: ";
        String messageRegister1 = "Wie ist dein Vorname: ";
        String messageEmail = "Wie lautet deine Email-Adresse: ";

        Cinema cinema = initCinema();
        Customer customer = regNewCustomer(messageRegister, messageRegister1, messageEmail);

        while (true) {
            mainMenu(cinema, customer);
        }
    }

    private static Cinema initCinema() {
        Cinema cinema = new Cinema("Cineplexx");
        getShowsFromDb(cinema);
        return cinema;
    }

    private static void mainMenu(Cinema cinema, Customer customer) {
        String message1 = "\nTreffen Sie eine Auswahl: ";
        String messageMenu = "1.Vorstellungen anzeigen \n2.Ticket kaufen " +
                "\n3.Ticket stornieren \n4.Gekaufte Tickets ansehen";
        String messageSelectShow = "Welche Vorstellung möchtest du besuchen: ";
        String messageSeats = "Wieviel sitze: ";
        String messageCancelTicket = "Welches Ticket möchten Sie stornieren: ";
        String error = "Da ist etwas schiefgelaufen!";

        // User input for menu
        int userInput = getUserInput(message1, messageMenu);

        switch (userInput) {
            case 1: // Show all shows
                cinema.printShows();
                break;
            case 2: // Tickets sale
                cinema.printShows();
                int userShowSelection = getUserInput(messageSelectShow);
                int userInputSeats = getUserInput(messageSeats);
                boolean isRoomFull = cinema.checkSeats(userShowSelection, userInputSeats);
                if(!isRoomFull)
                customer.buyTicket(userShowSelection, userInputSeats);
                break;
            case 3: // Cancel ticket
                customer.printTickets();
                int userInputCancel = getUserInput(messageCancelTicket);
                customer.cancelTicket(userInputCancel);
                break;
            case 4: // print bought tickets
                customer.printTickets();
                break;
            default:
                System.out.println(error);
        }
    }

    private static Customer regNewCustomer(String messageRegister, String messageRegister1, String messageEmail) {
        Scanner scanner = new Scanner(System.in);

        // Get email from user and check if exists in DB;
        System.out.println(messageEmail);
        String email = scanner.nextLine();
        Customer customer = new Customer(email);
        boolean isExisting = customer.checkIfExists();

        // If not create new user
        if(!isExisting) {
            System.out.println(messageRegister1);
            String firstName = scanner.nextLine();
            System.out.println(messageRegister);
            String lastName = scanner.nextLine();

            customer = new Customer(email, firstName, lastName);
            customer.insertCustomerToDb();
        }
        return customer;
    }

    private static int getUserInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        int userInput = scanner.nextInt();
        return userInput;
    }

    private static int getUserInput(String message1, String message2) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message1);
        System.out.println(message2);
        int userInput = scanner.nextInt();
        return userInput;
    }

    private static void getShowsFromDb(Cinema cinema) {
        Statement stmt = null;
        Connection conn = DbHelper.getConnectionToDb();
        String query = "SELECT shows.date, shows.time, movies.title, rooms.name, rooms.seats " +
                "FROM shows INNER JOIN movies ON movies.id=shows.movie INNER JOIN rooms ON rooms.id=shows.room";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                String movieTitle = rs.getString("title");
                String roomName = rs.getString("name");
                int roomSeats = rs.getInt("seats");

                Movie movie = new Movie(movieTitle);
                Room room = new Room(roomName, roomSeats);
                Show show = new Show(date, time, movie, room);

                cinema.setShow(show);
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
