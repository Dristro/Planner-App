package com.plannerapp.plannerapp.Controllers.LoginWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.plannerapp.plannerapp.Scenes.SceneManager;
import javafx.stage.Stage;

import java.sql.*;


public class LoginController {
    private final SceneManager sceneManager = new SceneManager();
    @FXML
    public Button create_acc_btn;
    @FXML
    public Button login_btn;
    @FXML
    public PasswordField password_fld;
    @FXML
    public TextField username_fld;
    @FXML
    public Label error_lbl;
    private boolean loginFlag;

    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";

/*
    private boolean isCorrectCredentials(String username, String password){
        //To add DB functionality
        if(Objects.equals(username, "roxy") && Objects.equals(password, "1234")) {
            return true;
        }
        else{
            return false;
        }
    }
*/

    private boolean validateLogin(String username, String password) {
        try (Connection con = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM Users WHERE Username = ? AND Password = ?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next(); // true if a matching row is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onLogin(ActionEvent event){

        //if(isCorrectCredentials(username_fld.getText(), password_fld.getText())){
        if(validateLogin(username_fld.getText(), password_fld.getText())){
            //Not needed later, once other functionality is added
            error_lbl.setText("You can login");
            error_lbl.setVisible(true);
            //Updating the loginFlag
            this.loginFlag = true;

            //Closing the login window, copy this line of code for other controllers to close their current windows
            Stage stage = (Stage) error_lbl.getScene().getWindow();
            sceneManager.closeStage(stage);


            //Showing the dashboard window
            sceneManager.showDashboard(username_fld.getText());
        }
        else{
            error_lbl.setText("Invalid Username or Password");
            error_lbl.setVisible(true);
        }
    }

    public void onCreateAccount(ActionEvent event){
        //Closing the login window, copy this line of code for other controllers to close their current windows
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        sceneManager.closeStage(stage);

        //Showing the dashboard window
        sceneManager.showCreateAccount();
    }

    // Comment after the first commit onto git

}