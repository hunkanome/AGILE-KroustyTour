package fr.insalyon.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PopUp {
    Stage window;

    public PopUp(String title, String message){
        // Opens another window that displays the current application version
        window = new Stage();
        window.setTitle(title);
        window.setResizable(false);
        window.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        // Add a label to the window containing the version
        Label label = new Label(message);
        label.setAlignment(Pos.BASELINE_CENTER);
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setPrefHeight(100);
        label.setPrefWidth(200);

        // add the label to a pane
        Pane pane = new Pane();
        pane.getChildren().add(label);

        // add the pane to the window
        Scene scene = new Scene(pane);
        window.setScene(scene);
    }

    public void show(){
        window.show();
    }
}
