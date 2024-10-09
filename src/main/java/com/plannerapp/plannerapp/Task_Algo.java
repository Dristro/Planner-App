package com.plannerapp.plannerapp;

import java.sql.*;


public class Task_Algo {

    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";
    private String header_username;



    private static Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }



}
