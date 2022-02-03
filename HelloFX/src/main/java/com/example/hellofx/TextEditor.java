package com.example.hellofx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;


public class TextEditor extends Application {

    Stage stage;
    BorderPane root = new BorderPane();
    TextArea fileTextArea = new TextArea();
    Label statusLabel = new Label();
    StringBuilder fileContent;
    FileUtilities fileUtilities = new FileUtilities();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setScreen();
        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Text Reader");
        stage.setScene(scene);
        stage.show();

    }
    public void setScreen() {
        Menu fileMenu = new Menu("File");
        Menu toolsMenu = new Menu("Tools");
        Menu helpMenu = new Menu("Help");

        MenuItem openFileMenuItem = new MenuItem("Open");
        MenuItem exitFileMenuItem = new MenuItem("Exit");
        MenuItem closeFileMenuItem = new MenuItem("Close");
        MenuItem searchToolsMenuItem = new MenuItem("Search");
        MenuItem replaceToolsMenuItem = new MenuItem("Replace");
        MenuItem wordCountToolsMenuItem = new MenuItem("Word Count");
        MenuItem aboutHelpMenuItem = new MenuItem("About");

        openFileMenuItem.setOnAction(new OpenFileHandler());
        searchToolsMenuItem.setOnAction(new searchToolsHandler());
        replaceToolsMenuItem.setOnAction(new repalceToolsHandler());
        wordCountToolsMenuItem.setOnAction(new wordCountToolsHandler());
        aboutHelpMenuItem.setOnAction(new aboutHanlder());

        closeFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fileTextArea.clear();
                fileContent.setLength(0);
                statusLabel.setText("");
                root.setCenter(null);
            }
        });
        exitFileMenuItem.setOnAction(event -> Platform.exit());

        // set status bar width and color
        statusLabel.setPrefWidth(this.stage.getMaxWidth());
        statusLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        // add menu items to menus and menus to menubar
        MenuBar menuBar = new MenuBar();
        fileMenu.getItems().addAll(openFileMenuItem, closeFileMenuItem, new SeparatorMenuItem(), exitFileMenuItem);
        toolsMenu.getItems().addAll(searchToolsMenuItem,replaceToolsMenuItem, new SeparatorMenuItem(),wordCountToolsMenuItem);
        helpMenu.getItems().addAll(aboutHelpMenuItem);
        menuBar.getMenus().addAll(fileMenu,toolsMenu,helpMenu);

        //set root children
        root.setTop(menuBar);
        root.setBottom(statusLabel);

    }

    private class OpenFileHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.setInitialDirectory(new File("/Users/lawrenceli/Desktop"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = null;
            if((file = fileChooser.showOpenDialog(stage)) != null) {
                fileContent = fileUtilities.readFile(file.getAbsolutePath());
                fileTextArea.clear();
                fileTextArea.appendText(fileContent.toString());
                fileTextArea.setWrapText(true);
                fileTextArea.positionCaret(0);
                statusLabel.setText(String.valueOf(file));
                root.setCenter(fileTextArea);
            }
        }
    }

    private class searchToolsHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

        }
    }

    private class repalceToolsHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

        }
    }

    private class wordCountToolsHandler implements EventHandler<ActionEvent> {
        String[] words;
        int wordCount = 0;
        @Override
        public void handle(ActionEvent event) {
            words = fileContent.toString().split(" ");
            wordCount = words.length;
            statusLabel.setText("Words Count: " + wordCount);
        }
    }

    private class aboutHanlder implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

        }
    }
}
