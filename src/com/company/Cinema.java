package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cinema {

    private String name;
    private ArrayList<Show> shows = new ArrayList<Show>();

    public Cinema(String name) {
        this.name = name;
    }

    public void setShow(Show show) {
        this.shows.add(show);
    }

    public void printShows() {
        for (int i = 0; i < shows.size(); i++) {
            System.out.println((i+1) + "." + shows.get(i).getDate() + " " + shows.get(i).getTime() + " " +
                    shows.get(i).getMovie().toString() + " " + shows.get(i).getRoom().toString());
        }
    }

    public boolean checkSeats(int userShowSelection, int userInputSeats) {
        /**TODO Die Anzahl der Sitze pro Saal sollten aus der Datenbank kommen. (in der if abfrage)
         * */
        String sqlCommand = "SELECT SUM(`seats`) FROM `tickets` WHERE `cinema_show` = " + userShowSelection;
        int sumSeats = selectDataFromDb(sqlCommand);
        boolean isRoomFull = false;
        if(userInputSeats + sumSeats > 40) {
            int sum = 40 - sumSeats;
            System.out.println("Sie können höchstens" + sum + " Sitze buchen");
            isRoomFull = true;
        }
        return isRoomFull;
    }

    private int selectDataFromDb(String sqlCommand) {
        // Get all tickets from current user
        int sumSeats = 0;
        Statement stmt = null;
        Connection conn = DbHelper.getConnectionToDb();
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                sumSeats = rs.getInt("SUM(`seats`)");
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
        return sumSeats;
    }
}
