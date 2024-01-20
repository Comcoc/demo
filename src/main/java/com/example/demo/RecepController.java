package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RecepController {
    public TextField topicR;
    @FXML
    private Label TextRecep;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    protected void onRecepDonneeButtonClick() {
        TextRecep.setText("Données sur " + topicR.getText() + " : ");

    }

    public void onEnvoyerDonneeButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        EnvoiController envoiController = fxmlLoader.getController();
        envoiController.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
    }
}
