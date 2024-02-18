package com.plannerapp.plannerapp;

import java.sql.*;
import java.util.Queue;

public class Task_Algo {

    public static void main(String[] args){
        sort_data();
    }

    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";
    private String header_username;



    private static Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    private static String create_sorted_table() throws SQLException {
        Connection conn = establishConnection();
        String createTable = "CREATE TABLE IF NOT EXISTS Sorted_tasks (Username TEXT, Task TEXT, 'Task Description' TEXT, Priority TEXT,'Task Aim Date' TEXT, 'Task Completed Date' TEXT)";
        Statement statement = conn.createStatement();

        statement.execute(createTable);
        System.out.println("Table created");

        statement.close();
        conn.close();
        return "Sorted_tasks";
    }
    public static void delete_unsorted_table(String tableName) throws SQLException {
        Connection conn = establishConnection();
        String deleteQuery = "DROP TABLE IF EXISTS " + tableName;
        Statement statement = conn.createStatement();

        statement.execute(deleteQuery);
        System.out.println(tableName + ": deleted");
    }

    private static void sort_data() {
        try {
            Connection conn = establishConnection();
            String newTable =  create_sorted_table();

            String selectQuery = "SELECT * FROM Tasks ORDER BY CASE Priority WHEN 'High' THEN 1 WHEN 'Medium' THEN 2 WHEN 'Low' THEN 3 ELSE 4 END";
            String insertQuery = "INSERT INTO " + newTable + " (Username, Task, Task Description, Priority, Task Aim Date, Task Completed Date) VALUES (?, ?, ?, ?, ?, ?)";

            Statement selectStatement = conn.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selectQuery);

            try(PreparedStatement insertStatement = conn.prepareStatement(insertQuery)){
                while(resultSet.next()){
                    insertStatement.setString(1, resultSet.getString("Username"));
                    insertStatement.setString(2, resultSet.getString("Task"));
                    insertStatement.setString(3, resultSet.getString("Task Description"));
                    insertStatement.setString(4, resultSet.getString("Priority"));
                    insertStatement.setString(5, resultSet.getString("Task Aim Date"));
                    insertStatement.setString(6, resultSet.getString("Task Completed Date"));

                    insertStatement.executeUpdate();
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }




}
