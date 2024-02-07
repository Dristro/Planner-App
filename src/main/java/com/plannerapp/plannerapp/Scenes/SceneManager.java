package com.plannerapp.plannerapp.Scenes;


import com.plannerapp.plannerapp.Controllers.UserViews.DashboardController;
import com.plannerapp.plannerapp.Controllers.UserViews.TasksController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage = new Stage();

    // Login
    public void showLogin(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }
    // Create Account
    public void showCreateAccount(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/CreateNewAccount.fxml"));
        createStage(loader);
    }
    // Dashboard
    public void showDashboard(String username){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UserViews/Dashboard.fxml"));
        createStage(loader);
        // Setting the welcome header
        DashboardController dashboardController = loader.getController();
        dashboardController.set_Header(username);
        dashboardController.populate_Tasks_List(username);
    }
    // Tasks
    public void showTasks(String username){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UserViews/Tasks.fxml"));
        createStage(loader);

        // Setting the welcome header
        TasksController tasksController = loader.getController();
        tasksController.set_Header(username);
        // Populating the list views
        tasksController.populate_Tasks_List(username);
        tasksController.populate_deleted_tasks(username);
        tasksController.populate_completed_list(username);
    }
    // Profile
    public void showProfile(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UserViews/Profile.fxml"));
        createStage(loader);
    }
    // Planner
    public void showPlanner(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UserViews/Planner.fxml"));
        createStage(loader);
    }


    public void showPopup(String popupText){
        Popup popup = new Popup();
        Label popup_lbl = new Label(popupText);
        popup.getContent().addAll(popup_lbl);
        popup.setX(120);
        popup.setY(50);
        popup.show(stage);
    }

    public void showEdit(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UserViews/Edit.fxml"));
        createStage(loader);
    }


    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Day Planner");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }

}
