module com.plannerapp.plannerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.plannerapp.plannerapp to javafx.fxml;
    exports com.plannerapp.plannerapp;
    exports com.plannerapp.plannerapp.Scenes;
    exports com.plannerapp.plannerapp.Controllers.LoginWindow;
    exports com.plannerapp.plannerapp.Controllers.UserViews;

}