package com.plannerapp.plannerapp.Controllers.LoginWindow;

import com.plannerapp.plannerapp.Scenes.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

import javafx.stage.Stage;

public class CreateAccountController {
    public Button create_acc_btn;
    public PasswordField password_fld;
    public TextField newusername_fld;
    public Label error_lbl;
    public Button back_btn;

    private SceneManager sceneManager = new SceneManager();
    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";

    private boolean isInputValid(String username, String password) {
        return username != null && password != null && !username.trim().isEmpty() && !password.trim().isEmpty();
    }

    private Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    private boolean isUsernameUnique(Connection connection, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0;
                }
            }
        }
        return false;
    }

    private boolean addUserToDatabase(String username, String password) {
        if (isInputValid(username, password)) {
            try (Connection connection = establishConnection()) {
                // Check for unique username
                if (isUsernameUnique(connection, username)) {
                    // Insert new user
                    String insertSql = "INSERT INTO Users (username, password) VALUES (?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        preparedStatement.setString(1, username.trim());
                        preparedStatement.setString(2, password.trim());

                        int rowsAffected = preparedStatement.executeUpdate();
                        return rowsAffected > 0;  // Returns true if at least one row was affected (i.e., the user was added).
                    }
                } else {
                    error_lbl.setText("Error: Username already exists.");
                    error_lbl.setVisible(true);
                    return false;
                }
            } catch (SQLException e) {
                handleSQLException(e);
                return false;
            }
        } else {
            error_lbl.setText("Error: Username or Password must be entered");
            error_lbl.setVisible(true);
            return false;
        }
    }

    private void handleSQLException(SQLException e) {
        if ("23000".equals(e.getSQLState()) && e.getErrorCode() == 19) {
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
            error_lbl.setText("Error: Username already exists.");
            error_lbl.setVisible(true);
        } else {
            e.printStackTrace();
        }
    }

    public void userCreate(ActionEvent event) {
        boolean addable = addUserToDatabase(newusername_fld.getText(), password_fld.getText());
        if (addable) {
            closeAndShowLogin();
        }
    }

    public void onBack_btn(ActionEvent event){
        closeAndShowLogin();
    }

    private void closeAndShowLogin() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        sceneManager.closeStage(stage);
        sceneManager.showLogin();
    }
}
