package com.plannerapp.plannerapp;

import com.plannerapp.plannerapp.Scenes.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private SceneManager sceneManager = new SceneManager();

    @Override
    public void start(Stage stage) throws Exception {

        // Opening the login window
        sceneManager.showLogin();

        //Opening the dashboard
        //Dashboard is shown in the LoginController

    }
}
