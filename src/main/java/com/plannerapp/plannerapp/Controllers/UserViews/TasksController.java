package com.plannerapp.plannerapp.Controllers.UserViews;

import com.plannerapp.plannerapp.Scenes.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TasksController implements Initializable {

    public Button dashboard_btn;
    public Button tasks_btn;
    public Button profile_btn;
    public Button planner_btn;
    public Button logout_btn;
    public Label welcome_lbl;
    public Label date_lbl;
    public ListView completed_tasks_listView;
    public Button completed_activate_btn;
    public Button delete_completed_btn;
    public ListView active_tasks_listView;
    public Button complete_active_btn;
    public Button edit_active_btn;
    public Button delete_active_btn;
    public ListView recently_deleted_listView;
    public Button recover_recently_btn;
    public Button delete_recently_btn;
    public TextField add_task_fld;
    public TextField add_task_description;
    public DatePicker add_task_date;
    public Button add_task_btn;
    public Label error_lbl;

    private String header_username;
    private final SceneManager sceneManager = new SceneManager();
    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Was leading to unwanted behaviour, so not needed for now (the command below)
        //populate_Tasks_List(header_username);

        // Disable the Delete, ... Buttons in the start, so they can only be pressed upon task selection
        complete_active_btn.setDisable(true);
        edit_active_btn.setDisable(true);
        delete_active_btn.setDisable(true);
    }

    //Methods for opening various windows
    public void openDashboard(){
        //Close the current stage
        Stage stage = (Stage) (welcome_lbl.getScene().getWindow());
        sceneManager.closeStage(stage);
        //Show the dashboard
        sceneManager.showDashboard(header_username);
    }
    public void openProfile(){
        Stage stage = (Stage) (welcome_lbl.getScene().getWindow());     //Change from welcome_lbl to other for other controllers
        sceneManager.closeStage(stage);
        //Show the dashboard
        sceneManager.showProfile();
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

    //Opening the respective windows when clicked
    public void onDashboard(ActionEvent event){            // Tasks
        openDashboard();
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
    public void set_Header(String username){
        welcome_lbl.setText("Welcome " + username);
        date_lbl.setText("12/01/2024");
        this.header_username = username;
    }
    //                                                                                   Add task section              ************************************************************

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
    // Adding the task to the DB
    public void addTask(String taskName, String taskDescription, String taskDate){
        try(Connection conn = establishConnection()){

            String insertSql = "INSERT INTO Tasks (Username, Task, \"Task Description\", \"Task Aim Date\", \"Task Completed Date\") VALUES (?, ?, ?, ?, ?)";
            String taskCompleteDateDefault = "";
            try(PreparedStatement preparedStatement = conn.prepareStatement(insertSql)){

                preparedStatement.setString(1, header_username.trim());         // Adding to DB - Username
                preparedStatement.setString(2, taskName.trim());                // Adding to DB - Task name
                preparedStatement.setString(3, taskDescription.trim());         // Adding to DB - Task Description
                preparedStatement.setString(4, taskDate.trim());                // Adding to DB - Task Aim Date
                preparedStatement.setString(5, taskCompleteDateDefault.trim()); // Adding to DB - Task Complete Date

                int rowsEffected = preparedStatement.executeUpdate();
                if(rowsEffected>0){
                    add_task_fld.setText("");
                    add_task_description.setText("");
                    add_task_date.setValue(null);
                    populate_Tasks_List(header_username);
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
                    active_tasks_listView.setItems(data);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //                                                                                   Delete active task section    ************************************************************
    // Deletes and moves active tasks to the deleted table
    public void selected_tasks(){
        active_tasks_listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                complete_active_btn.setDisable(false);          // Enable the buttons if selected
                edit_active_btn.setDisable(false);
                delete_active_btn.setDisable(false);
            } else {
                complete_active_btn.setDisable(true);           // Disable the buttons if not selected
                edit_active_btn.setDisable(true);
                delete_active_btn.setDisable(true);
            }
        });
    }
    public void onDelete_active_task(ActionEvent event){
        delete_active_task(header_username, active_tasks_listView.getSelectionModel().getSelectedItem().toString());
        populate_deleted_tasks(header_username);
        populate_Tasks_List(header_username);
    }
    public void delete_active_task(String username, String taskName) {
        try {
            Connection conn = establishConnection();
            String selectQuery = "SELECT * FROM Tasks WHERE Username = ? AND Task = ?";
            String insertQuery = "INSERT INTO Recently_Deleted_Tasks (Username, Task_name, Task_Description, Task_Aim_Date, Task_Completed_Date) VALUES (?, ?, ?, ?, ?)";
            String deleteQuery = "DELETE FROM Tasks WHERE Username = ? AND Task = ?";

            try (PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
                 PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                 PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery)) {

                // Check if the task exists for the given user
                selectStatement.setString(1, username);
                selectStatement.setString(2, taskName);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Task exists, move it to another table
                        insertStatement.setString(1, resultSet.getString("Username"));
                        insertStatement.setString(2, resultSet.getString("Task"));
                        insertStatement.setString(3, resultSet.getString("Task Description"));
                        insertStatement.setString(4, resultSet.getString("Task Aim Date"));
                        insertStatement.setString(5, resultSet.getString("Task Completed Date"));
                        // Adds the values from the older table to the new one
                        // New table = getValues from Old table .getString(...);

                        int rowsInserted = insertStatement.executeUpdate();

                        if (rowsInserted > 0) {
                            // System.out.println("Task moved to AnotherTable successfully.");          For debugging only
                            // Now, delete the task from the original table
                            deleteStatement.setString(1, username);
                            deleteStatement.setString(2, taskName);

                            int rowsDeleted = deleteStatement.executeUpdate();

                            if (rowsDeleted > 0) {
                                //System.out.println("Task deleted from Tasks table successfully.");
                                //sceneManager.showPopup("Task deleted");
                            } else {
                                //System.out.println("Error deleting task from Tasks table.");
                                //sceneManager.showPopup("Error deleting");
                            }
                        } else {
                            //System.out.println("Error moving task to AnotherTable.");
                            //sceneManager.showPopup("Error moving to recently deleted");
                        }
                    } else {
                        //System.out.println("Task not found for the given user.");
                        //sceneManager.showPopup("Task doesnt belong to user");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populate_deleted_tasks(String username){
        try{
            Connection conn = establishConnection();
            String query = "SELECT Task_name FROM Recently_Deleted_Tasks WHERE Username = ?";
            try(PreparedStatement preparedStatement = conn.prepareStatement(query)){
                // System.out.println("Header_username: " + header_username);
                preparedStatement.setString(1, username);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    ObservableList<String> data = FXCollections.observableArrayList();

                    while(resultSet.next()){
                        String value = resultSet.getString("Task_name");
                        data.add(value);
                    }
                    recently_deleted_listView.setItems(data);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }



    //                                                                                   Delete recently deleted tasks ************************************************************
    public void onDelete_recently_deleted_task(ActionEvent event){
        // First call the method below
        // Then run the populate_recently_deleted listview method
    }
    private void delete_recently_deleted_task(String username, String taskName){
        // Add the code for accessing the DB and deleting the task from the Recently_Deleted table
    }


    //                                                                                                                 ************************************************************

    //                                                                                                                 ************************************************************
}
