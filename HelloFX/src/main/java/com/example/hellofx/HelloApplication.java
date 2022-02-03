package com.example.hellofx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //set the root container
        StackPane root = new StackPane();
        // set the button
        Button btn = new Button();
        btn.setText("sAY Hello WORDL！");
        btn.setOnAction(new ButtonHanlder());
        // add button to root
        root.getChildren().add(btn);
        Scene scene = new Scene(root, 300, 250);
        stage.setTitle("My JavaFX Application!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class ButtonHanlder implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Hello world!");
        }
    }
}