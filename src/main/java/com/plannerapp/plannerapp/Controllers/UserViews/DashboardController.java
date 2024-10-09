package com.plannerapp.plannerapp.Controllers.UserViews;

import com.plannerapp.plannerapp.Scenes.SceneManager;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    // All Variables (and dx:id's)
    public Button dashboard_btn;
    public Button tasks_btn;
    public Button profile_btn;
    public Button planner_btn;
    public Button logout_btn;
    public Label welcome_lbl;
    public Label date_lbl;
    public ListView current_tasks_listView;
    public Button complete_btn;
    public Button edit_btn;
    public Button delete_btn;
    public ListView today_task_listView;
    public TextField add_task_fld;
    public DatePicker add_task_date;
    public Button add_task_btn;
    private final SceneManager sceneManager = new SceneManager();
    public TextField add_task_description;
    public Label error_lbl;
    public Label task_description_desc_lbl;
    public Label task_date_desc_lbl;
    public Label task_name_desc_lbl;
    public ComboBox task_priority_cBox;

    private String header_username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Show all the Tasks from the DB upon login
        // populate_Tasks_List();
    }

    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";

    //Methods for opening various windows
    public void openTask(){
        //Close the current stage
        Stage stage = (Stage) (welcome_lbl.getScene().getWindow());
        sceneManager.closeStage(stage);
        //Show the dashboard
        sceneManager.showTasks(header_username);
    }
    public void openProfile(){
        Stage stage = (Stage) (welcome_lbl.getScene().getWindow());     //Change from welcome_lbl to other for other controllers
        sceneManager.closeStage(stage);
        //Show the dashboard
        sceneManager.showProfile(header_username);
    }
    public void openPlanner(){
        Stage stage = (Stage) (welcome_lbl.getScene().getWindow());     //Change from welcome_lbl to other for other controllers
        sceneManager.closeStage(stage);
        //Show the dashboard
        sceneManager.showPlanner();
    }
    public void openLogin(){
        Stage stage = (Stage) (welcome_lbl.getScene().getWindow());     //Change from welcome_lbl to other for other controllers
        sceneManager.closeStage(stage);
        //Show the dashboard
        sceneManager.showLogin();
    }
    public void openEdit(){
        //Stage stage = (Stage) (welcome_lbl.getScene().getWindow());
        sceneManager.showEdit(header_username, current_tasks_listView.getSelectionModel().getSelectedItem().toString());
    }

    //Opening the respective windows when clicked
    public void onTasks(ActionEvent event){            // Tasks
        openTask();
    }
    public void onProfile(ActionEvent event){          // Profile
        openProfile();
    }
    public void onPlanner(ActionEvent event){          // Planner
        openPlanner();
    }
    public void onLogout(ActionEvent event){            // Logout
        openLogin();
    }

    // Done with th window opening and closing section
    //Setting the header/top of the dashboard window with the username and date
    public void set_Header(String username){
        welcome_lbl.setText("Welcome " + username);
        date_lbl.setText("12/01/2024");
        this.header_username = username;
    }


    // Setting up the add task part of the window
    private Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
    // Adding tasks
    // Getting task data from the user prompt
    public void getTaskData(){
        // Set these values into the DB
        try {
            String taskName = add_task_fld.getText();
            String taskDescription = add_task_description.getText();
            LocalDate selectedDate = add_task_date.getValue();
            String taskDate = selectedDate.toString();
            if(taskName.isBlank() || taskName.isEmpty())
            {
                error_lbl.setText("Invalid Task Name");
                error_lbl.setVisible(true);
            }
            else {
                addTask(taskName, taskDescription, taskDate);
            }
        } catch (Exception e){
            //e.printStackTrace();
            error_lbl.setText("Invalid input");
            error_lbl.setVisible(true);
        }

    }

    // Setting the values at the ComboBox
    public void set_priority_box_values(){
        ObservableList<String> items = FXCollections.observableArrayList("High", "Medium", "Low");
        task_priority_cBox.setItems(items);
        task_priority_cBox.getSelectionModel().selectFirst();
    }

    // Adding the task to the DB
    public void addTask(String taskName, String taskDescription, String taskDate){
/*
        try(Connection conn = establishConnection()){
            String insertQuery = "INSERT INTO Tasks (Username, Task, \"Task Description\", Priority, \"Task Aim Date\", \"Task Completed Date\") " +
                    "SELECT ?, ?, ?, ?, ?, ? " +
                    "FROM TASKS " +
                    "ORDER BY CASE Priority WHEN 'High' THEN 1 WHEN 'Medium' THEN 2 WHEN 'Low' THEN 3 ELSE 4 END";
            String taskCompletedDefault = "";

            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setString(1, header_username.trim());
            insertStatement.setString(2, taskName);
            insertStatement.setString(3, taskDescription);
            insertStatement.setString(4, task_priority_cBox.getSelectionModel().getSelectedItem().toString());
            insertStatement.setString(5, taskDate.trim());
            insertStatement.setString(6, taskCompletedDefault.trim());


            int rowsEffected = insertStatement.executeUpdate();
            if(rowsEffected>0){
                add_task_fld.setText("");
                add_task_description.setText("");
                add_task_date.setValue(null);
                populate_Tasks_List(header_username);
                populate_today_tasks_listview(header_username);
                error_lbl.setVisible(false);
            }
            else{
                error_lbl.setText("Error adding task");
                error_lbl.setVisible(true);
            }

        } catch (SQLException e){
            e.printStackTrace();
            int error_code = e.getErrorCode();
            System.out.println("SQLite error code: " + error_code);
            error_lbl.setText("Error: try again");
            error_lbl.setVisible(true);
        }*/

        try(Connection conn = establishConnection()){

            String insertSql = "INSERT INTO Tasks (Username, Task, \"Task Description\", Priority, \"Task Aim Date\", \"Task Completed Date\") VALUES (?, ?, ?, ?, ?, ?)";
            String taskCompleteDateDefault = "";
            try(PreparedStatement preparedStatement = conn.prepareStatement(insertSql)){

                preparedStatement.setString(1, header_username.trim());         // Adding to DB - Username
                preparedStatement.setString(2, taskName.trim());                // Adding to DB - Task name
                preparedStatement.setString(3, taskDescription.trim());         // Adding to DB - Task Description
                preparedStatement.setString(4, task_priority_cBox.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(5, taskDate.trim());                // Adding to DB - Task Aim Date
                preparedStatement.setString(6, taskCompleteDateDefault.trim()); // Adding to DB - Task Complete Date

                int rowsEffected = preparedStatement.executeUpdate();
                if(rowsEffected>0){
                    add_task_fld.setText("");
                    add_task_description.setText("");
                    add_task_date.setValue(null);
                    populate_Tasks_List(header_username);
                    populate_today_tasks_listview(header_username);
                    error_lbl.setVisible(false);
                }
                else{
                    error_lbl.setText("Error adding task");
                    error_lbl.setVisible(true);
                }
                // Add a error lbl near the add task point to show any kinds of errors in adding the tasks to hte DB.
            }
        }catch (SQLException e){
            e.printStackTrace();
            int error_code = e.getErrorCode();
            System.out.println("SQLite error code: " + error_code);
            error_lbl.setText("Error: try again");
            error_lbl.setVisible(true);
        }
    }

    // Showing tasks on the List
    public void populate_Tasks_List (String header_username){
        try{

            Connection conn = establishConnection();
            String query = "SELECT Task FROM Tasks WHERE Username = ?";

            try(PreparedStatement preparedStatement = conn.prepareStatement(query)){
                // System.out.println("Header_username: " + header_username);
                preparedStatement.setString(1, header_username);

                try(ResultSet resultSet = preparedStatement.executeQuery()) {

                    ObservableList<String> data = FXCollections.observableArrayList();

                    while(resultSet.next()){
                        String value = resultSet.getString("Task");
                        data.add(value);
                    }
                    current_tasks_listView.setItems(data);

                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Show task properties below selected task
    public void onTask_selected(){
        set_current_task_data(header_username, current_tasks_listView.getSelectionModel().getSelectedItem().toString());
    }

    public void set_current_task_data(String username, String taskName){
        try{
            Connection conn = establishConnection();
            String selectQuery = "SELECT * FROM Tasks WHERE Username = ? AND Task = ?";

            try(PreparedStatement selectStatement = conn.prepareStatement(selectQuery)){
                selectStatement.setString(1, username);
                selectStatement.setString(2, taskName);

                try(ResultSet resultSet = selectStatement.executeQuery()){
                    if(resultSet.next()){
                        task_name_desc_lbl.setText(taskName);
                        task_description_desc_lbl.setText(resultSet.getString("Task Description"));
                        task_date_desc_lbl.setText(resultSet.getString("Task Aim Date"));
                    }
                }
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onComplete_Task(ActionEvent event){
        try {
            complete_Task(header_username, current_tasks_listView.getSelectionModel().getSelectedItem().toString());
            populate_Tasks_List(header_username);
        } catch (Exception e){

        }
    }
    public void complete_Task(String username, String taskName){
        try{
            Connection conn = establishConnection();
            String selectQuery = "SELECT * FROM Tasks WHERE Username = ? AND Task = ?";
            String insertQuery = "INSERT INTO Completed_Tasks (Username, Task_Name, Task_Description, Task_Aim_Date, Task_Completed_Date) VALUES (?, ?, ?, ?, ?)";
            String deleteQuery = "DELETE FROM Tasks WHERE Username = ? AND Task = ?";

            try(PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);){

                selectStatement.setString(1, username);
                selectStatement.setString(2, taskName);
                try(ResultSet resultSet = selectStatement.executeQuery()){
                    if(resultSet.next()){
                        insertStatement.setString(1, resultSet.getString("Username"));
                        insertStatement.setString(2, resultSet.getString("Task"));
                        insertStatement.setString(3, resultSet.getString("Task Description"));
                        insertStatement.setString(4, resultSet.getString("Task Aim Date"));
                        insertStatement.setString(5, resultSet.getString("Task Completed Date"));

                        int rowEffected = insertStatement.executeUpdate();
                        if(rowEffected > 0){
                            deleteStatement.setString(1, username);
                            deleteStatement.setString(2, taskName);
                            deleteStatement.executeUpdate();
                        }
                    }
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onEdit_Task(ActionEvent event){
        try{
            edit_Task(header_username, current_tasks_listView.getSelectionModel().getSelectedItem().toString());
            populate_Tasks_List(header_username);
        } catch (Exception e){

        }
    }
    public void edit_Task(String username, String taskName){
        openEdit();
    }

    public void onDelete_Task(){
        try {
            delete_Task(header_username, current_tasks_listView.getSelectionModel().getSelectedItem().toString());
            populate_Tasks_List(header_username);
        } catch (Exception e){

        }
    }
    public void delete_Task(String username, String taskName){
        try{
            Connection conn = establishConnection();
            String selectQuery = "SELECT * FROM Tasks WHERE Username = ? AND Task = ?";
            String insertQuery = "INSERT INTO Recently_Deleted_Tasks (Username, Task_name, Task_Description, Task_Aim_Date, Task_Completed_Date) VALUES (?, ?, ?, ?, ?)";
            String deleteQuery = "DELETE FROM Tasks WHERE Username = ? AND Task = ?";

            try(PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);){

                selectStatement.setString(1, username);
                selectStatement.setString(2, taskName);
                try(ResultSet resultSet = selectStatement.executeQuery()){
                    if(resultSet.next()){
                        insertStatement.setString(1, resultSet.getString("Username"));
                        insertStatement.setString(2, resultSet.getString("Task"));
                        insertStatement.setString(3, resultSet.getString("Task Description"));
                        insertStatement.setString(4, resultSet.getString("Task Aim Date"));
                        insertStatement.setString(5, resultSet.getString("Task Completed Date"));

                        int rowsEffected = insertStatement.executeUpdate();
                        if(rowsEffected>0){
                            deleteStatement.setString(1, username);
                            deleteStatement.setString(2, taskName);
                            deleteStatement.executeUpdate();
                        }
                    }
                }

            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Adding the today's tasks listview
    public void populate_today_tasks_listview(String username){
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate current_date = LocalDate.now();
        String formattedDate = current_date.format(formatter);
        // Now we have the current date and the username

        // Getting the data with said username and current date
        try{
            Connection conn = establishConnection();
            String selectQuery = "SELECT Task FROM Tasks WHERE Username = ? AND \"Task Aim Date\" = ?";
            try(PreparedStatement selectStatement = conn.prepareStatement(selectQuery)){
                selectStatement.setString(1, username);
                selectStatement.setString(2, formattedDate);

                try(ResultSet resultSet = selectStatement.executeQuery()){
                    ObservableList<String> data = FXCollections.observableArrayList();

                    while(resultSet.next()){
                        String value = resultSet.getString("Task");
                        data.add(value);
                    }
                    today_task_listView.setItems(data);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
