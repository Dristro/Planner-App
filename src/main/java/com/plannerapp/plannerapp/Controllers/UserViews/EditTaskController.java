package com.plannerapp.plannerapp.Controllers.UserViews;

import com.plannerapp.plannerapp.Scenes.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditTaskController implements Initializable {

    public TextField taskName_fld;
    public TextField taskDescription_fld;
    public DatePicker taskAimDate_fld;
    public Button cancel_btn;
    public Button update_btn;
    private static final String DATABASE_URL = "jdbc:sqlite:PlannerAppDB.db";
    private final SceneManager sceneManager = new SceneManager();
    public Text edit_task_lbl;
    private String header_username;
    private String selectedTaskName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    // Setting the values on the Edit Tasks window
    public void setTaskName(String username, String taskName){
        this.selectedTaskName = taskName;
        taskName_fld.setText(taskName);
    }
    public void setTaskDescription(String username, String taskName){
        try{
            Connection conn = establishConnection();
            String selectQuery = "SELECT * FROM Tasks WHERE Username = ? AND Task = ?";
            try(PreparedStatement selectStatement = conn.prepareStatement(selectQuery)){
                selectStatement.setString(1, username);
                selectStatement.setString(2, taskName);

                try(ResultSet resultSet = selectStatement.executeQuery()){
                    if(resultSet.next()){
                        taskDescription_fld.setText(resultSet.getString("Task Description"));
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setTaskAimDate(String username, String taskName){
        try{
            Connection conn = establishConnection();
            String selectQuery = "SELECT * FROM Tasks WHERE Username = ? AND Task = ?";
            try(PreparedStatement selectStatement = conn.prepareStatement(selectQuery)){
                selectStatement.setString(1, username);
                selectStatement.setString(2, taskName);

                try(ResultSet resultSet = selectStatement.executeQuery()){
                    if(resultSet.next()){
                        String tempDate = (resultSet.getString("Task Aim Date"));
                        DateTimeFormatter formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate date = LocalDate.parse(tempDate, formattedDate);
                        taskAimDate_fld.setValue(date);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void set_Header(String username){
        this.header_username = username;
    }

    public void onCancel(ActionEvent event){
        cancel_edit();
    }
    public void cancel_edit(){
        Stage stage = (Stage) edit_task_lbl.getScene().getWindow();
        sceneManager.closeStage(stage);
    }

    public void onUpdate(ActionEvent event){
        update_edit(header_username, selectedTaskName,taskName_fld.getText(), taskDescription_fld.getText(), taskAimDate_fld.getValue().toString());
    }
    public void update_edit(String username, String selectedTask, String taskName, String taskDescription, String taskAimDate){
        try{
            Connection conn = establishConnection();
            String updateQuery = "UPDATE Tasks SET Task = ?, 'Task Description' = ?, 'Task Aim Date' = ? WHERE Username = ? AND Task = ?";

            try(PreparedStatement updateStatement = conn.prepareStatement(updateQuery)){
                updateStatement.setString(1, taskName);
                updateStatement.setString(2, taskDescription);
                updateStatement.setString(3, taskAimDate);
                updateStatement.setString(4, username);
                updateStatement.setString(5, selectedTask);

                int rowsEffected = updateStatement.executeUpdate();
                if(rowsEffected > 0){
                    cancel_edit();
                }
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
