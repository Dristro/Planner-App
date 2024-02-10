package com.plannerapp.plannerapp.Controllers.UserViews;

import com.plannerapp.plannerapp.Scenes.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    public Button dashboard_btn;
    public Button tasks_btn;
    public Button profile_btn;
    public Button planner_btn;
    public Button logout_btn;
    public Label welcome_lbl;
    public Label date_lbl;
    public Button edit_username_btn;
    public TextField new_username_fld;
    public Label current_username_lbl;
    public Button edit_password_btn;
    public TextField new_password_fld;
    public Label current_password_lbl;
    public Button edit_email_btn;
    public TextField new_email_fld;
    public Label current_email_lbl;
    public Button delete_account_btn;
    public TextField password_fld;
    public TextField username_fld;

    private String header_username;
    private final SceneManager sceneManager = new SceneManager();
    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Was leading to unwanted behaviour, so not needed for now (the command below)
        //populate_Tasks_List(header_username);

        // Disable the Delete, ... Buttons in the start, so they can only be pressed upon task selection
        //complete_active_btn.setDisable(true);
        //edit_active_btn.setDisable(true);
        //delete_active_btn.setDisable(true);
    }

    // Various methods for window

    // Methods for opening various windows
    public void openDashboard(){
        //Close the current stage
        Stage stage = (Stage) (welcome_lbl.getScene().getWindow());
        sceneManager.closeStage(stage);
        //Show the dashboard
        sceneManager.showDashboard(header_username);
    }
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

    // Opening the respective windows when clicked
    public void onDashboard(ActionEvent event){            // Tasks
        openDashboard();
    }
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

    // Done with the window opening and closing section
    // Utility functions
    public void set_Header(String username){
        welcome_lbl.setText("Welcome " + username);
        date_lbl.setText("12/01/2024");
        this.header_username = username;
    }
    private Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    // Set the user's current fields
    public void setCurrentFields(){
        try{
            Connection conn = establishConnection();
            String usernameQuery = "SELECT Username FROM Users WHERE Username = ?";
            String passwordQuery = "SELECT Password FROM Users WHERE Username = ?";
            String emailQuery = "SELECT Email FROM Users WHERE Username = ?";

            try(PreparedStatement usernameStatement = conn.prepareStatement(usernameQuery);
                PreparedStatement passwordStatement = conn.prepareStatement(passwordQuery);
                PreparedStatement emailStatement = conn.prepareStatement(emailQuery);){

                usernameStatement.setString(1, header_username);
                passwordStatement.setString(1, header_username);
                emailStatement.setString(1, header_username);

                try(ResultSet usernameResult = usernameStatement.executeQuery();
                    ResultSet passwordResult = passwordStatement.executeQuery();
                    ResultSet emailResult = emailStatement.executeQuery();){

                    if(usernameResult.next()){
                        this.current_username_lbl.setText(usernameResult.getString("Username"));
                    }
                    if(passwordResult.next()){
                        this.current_password_lbl.setText(passwordResult.getString("Password"));
                    }
                    if(emailResult.next()){
                        this.current_email_lbl.setText(emailResult.getString("Email"));
                    }
                }
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }


    // Edit current username to a new one
    public void onEdit_username(ActionEvent event){
        if(new_username_fld.getText().isEmpty()){
            System.out.println("No username provided");
            return;
        }
        editUsername(header_username, new_username_fld.getText().toString());
        setCurrentFields();
    }
    public void editUsername(String currentUsername, String newUsername){
        try{
            Connection conn = establishConnection();
            String updateUsersQuery = "UPDATE Users SET Username = ? WHERE Username = ?";
            String updateTasksQuery = "UPDATE Tasks SET Username = ? WHERE Username = ?";
            String updateRecentlyQuery = "UPDATE Recently_Deleted_Tasks SET Username = ? WHERE Username = ?";
            String updateCompletedQuery = "UPDATE Completed_Tasks SET Username = ? WHERE Username = ?";


            try(PreparedStatement updateUsersStatement = conn.prepareStatement(updateUsersQuery);
                PreparedStatement updateTasksStatement = conn.prepareStatement(updateTasksQuery);
                PreparedStatement updateRecentlyStatement = conn.prepareStatement(updateRecentlyQuery);
                PreparedStatement updateCompletedStatement = conn.prepareStatement(updateCompletedQuery);){

                updateUsersStatement.setString(1, newUsername);
                updateUsersStatement.setString(2, currentUsername);
                updateTasksStatement.setString(1, newUsername);
                updateTasksStatement.setString(2, currentUsername);
                updateRecentlyStatement.setString(1, newUsername);
                updateRecentlyStatement.setString(2, currentUsername);
                updateCompletedStatement.setString(1, newUsername);
                updateCompletedStatement.setString(2, currentUsername);


                int usersRowsEffected = updateUsersStatement.executeUpdate();
                int tasksRowsEffected = updateTasksStatement.executeUpdate();
                int recentlyRowsEffected = updateRecentlyStatement.executeUpdate();
                int completedRowsEffected = updateCompletedStatement.executeUpdate();

                header_username = newUsername;
                if((usersRowsEffected>0) && (tasksRowsEffected>0) && (recentlyRowsEffected>0) && (completedRowsEffected>0)){
                    System.out.println("Done");
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Edit current email to a new one
    public void onEdit_email(ActionEvent event){
        if(new_email_fld.getText().isEmpty()){
            System.out.println("No email provided");
            return;
        }
        editEmail(header_username, new_email_fld.getText().toString());
        setCurrentFields();
    }
    public void editEmail(String username, String newEmail){
        try{
            Connection conn = establishConnection();
            String updateUsersQuery = "UPDATE Users SET Email = ? WHERE Username = ?";


            try(PreparedStatement updateUsersStatement = conn.prepareStatement(updateUsersQuery)){

                updateUsersStatement.setString(1, newEmail);
                updateUsersStatement.setString(2, username);


                int usersRowsEffected = updateUsersStatement.executeUpdate();

                if(usersRowsEffected>0){
                    System.out.println("Done");
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Edit current password to a new one
    public void onEdit_password(ActionEvent event){
        if(new_password_fld.getText().isEmpty()){
            System.out.println("No password provided");
            return;
        }
        editPassword(header_username, new_password_fld.getText().toString());
        setCurrentFields();
    }
    public void editPassword(String username, String newPassword){
        try{
            Connection conn = establishConnection();
            String updateQuery = "UPDATE Users SET Password = ? WHERE Username = ?";

            try(PreparedStatement updateStatement = conn.prepareStatement(updateQuery)){
                updateStatement.setString(1, newPassword);
                updateStatement.setString(2, username);

                int rowsEffected = updateStatement.executeUpdate();
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Delete the user's account and all the data related to it
    public void onDelete_account(ActionEvent event){
        deleteAccount(username_fld.getText(), password_fld.getText());
        onLogout(event);
    }
    public void deleteAccount(String username, String password){
        if(isCredentialCorrect(username, password)){
            try{
                Connection conn = establishConnection();
                String deleteUsers = "DELETE FROM Users WHERE Username = ? AND Password = ?";
                String deleteRecentlyDeleted = "DELETE FROM Recently_Deleted_Tasks WHERE Username = ?";
                String deleteCompletedTasks = "DELETE FROM Completed_Tasks WHERE Username = ?";
                String deleteTasks = "DELETE FROM Tasks WHERE Username = ?";

                try(PreparedStatement usersStatement = conn.prepareStatement(deleteUsers);
                    PreparedStatement recentlyStatement = conn.prepareStatement(deleteRecentlyDeleted);
                    PreparedStatement completedStatement = conn.prepareStatement(deleteCompletedTasks);
                    PreparedStatement tasksStatement = conn.prepareStatement(deleteTasks)){

                    usersStatement.setString(1, username);
                    usersStatement.setString(2, password);
                    recentlyStatement.setString(1, username);
                    completedStatement.setString(1, username);
                    tasksStatement.setString(1, username);

                    int usersRowsEffected = usersStatement.executeUpdate();
                    int recentlyRowsEffected = recentlyStatement.executeUpdate();
                    int completedRowsEffected = completedStatement.executeUpdate();
                    int tasksRowsEffected = tasksStatement.executeUpdate();

                    System.out.println("Done");

                }



            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private boolean isCredentialCorrect(String username, String password){
        try{
            Connection conn = establishConnection();
            String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
            try(PreparedStatement statement = conn.prepareStatement(query)){
                statement.setString(1, username);
                statement.setString(2, password);

                try(ResultSet resultSet = statement.executeQuery()){
                    if(resultSet.next()){
                        return true;
                    }
                    return false;
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}
