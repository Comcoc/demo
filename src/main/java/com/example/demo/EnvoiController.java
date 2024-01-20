package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EnvoiController {
    public TextField donnee;
    public TextField topic;
    @FXML
    private Label Text;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void onEnvoyerButtonClick() {
        Text.setText("Envoi de la donnée: " + donnee.getText() + " sur le topic " + topic.getText());
    }

    public void onRecevoirButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Recep-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        RecepController recepController = fxmlLoader.getController();
        recepController.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
    }
}