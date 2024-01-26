package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainApplication extends Application {
    private ObservableList<String> edgeServersData = FXCollections.observableArrayList();
    private ObservableList<String> storageTopicData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edge Server App");

        ListView<String> edgeServersList = new ListView<>(edgeServersData);
        ListView<String> storageTopicList = new ListView<>(storageTopicData);
        TextField topicField = new TextField();
        topicField.setPromptText("Enter Topic");
        TextField fileNameField = new TextField();
        fileNameField.setPromptText("Enter File Name");
        Button downloadButton = new Button("Download File");
        Button uploadButton = new Button("Upload File");
        Label statusLabel = new Label();

        VBox content = new VBox(edgeServersList, storageTopicList, topicField, fileNameField, downloadButton, uploadButton, statusLabel);

        Scene scene = new Scene(content, 600, 400);
        primaryStage.setScene(scene);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateEdgeServersList, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::updateStorageTopicList, 0, 1, TimeUnit.SECONDS);

        primaryStage.show();

        if (!Network.login()) {
            System.out.println("Broker login failed");
            System.exit(-99);
        }

        TopicCapability[] topics = new TopicCapability[]{
                new TopicCapability("food", 999999), new TopicCapability("temperature", 123456),
        };

        if (!Storage.init(topics)) {
            System.out.println("Storage init failed");
            System.exit(-98);
        }
    }

    private void updateEdgeServersList() {
        List<String> updatedEdgeServers = new ArrayList<>();
        //List<String> updatedEdgeServers = Arrays.asList(Network.getServers());

        for (int i = 1; i <= 3; i++) {
            int randomStorageSpace = new Random().nextInt(100) + 1; // Random storage space between 1 and 100 GB
            String edgeServer = "Edge Server " + i + " (" + randomStorageSpace + " GB)";
            updatedEdgeServers.add(edgeServer);
        }

        Platform.runLater(() -> {
            edgeServersData.clear();
            edgeServersData.addAll(updatedEdgeServers);
        });
    }

    private void updateStorageTopicList() {
        List<String> updatedTopicList = new ArrayList<>();
        TopicCapability[] topics = Storage.getTopics();

        if (topics == null)
            return;

        for (int i = 0; i < topics.length; i++)
            updatedTopicList.add(topics[i].topic + ": " + topics[i].bytes + " bytes available");

        Platform.runLater(() -> {
            storageTopicData.clear();
            storageTopicData.addAll(updatedTopicList);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
