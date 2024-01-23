package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class DiscoveryController {

    @FXML
    private Button discoverButton;

    @FXML
    private ListView<String> topicList;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void onDiscoverButtonClick(ActionEvent event) {
        // Call your MQTT discovery function here
        // Once you receive the response, update the topicList
        updateTopicListWithDiscoveryResult();
    }

    private void updateTopicListWithDiscoveryResult() {
        // Replace this with your logic to get the topics from the discovery response
        // For example, if you have a list of topics, you can do something like:
        topicList.getItems().clear();
        topicList.getItems().addAll("Topic1", "Topic2", "Topic3");
    }
}
