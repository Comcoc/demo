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

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edge Server App");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(createEdgeServersTab(), createFileOperationsTab());

        Scene scene = new Scene(tabPane, 600, 400);
        primaryStage.setScene(scene);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateEdgeServersList, 0, 10, TimeUnit.SECONDS);

        primaryStage.show();

        if (!Network.login()) {
            System.out.println("Broker login failed");
            System.exit(-99);
        }
    }

    private void updateEdgeServersList() {
        List<String> updatedEdgeServers = new ArrayList<>();

        // Simulate fetching edge servers with random storage space
        for (int i = 1; i <= 3; i++) {
            int randomStorageSpace = new Random().nextInt(100) + 1; // Random storage space between 1 and 100 GB
            String edgeServer = "Edge Server " + i + " (" + randomStorageSpace + " GB)";
            updatedEdgeServers.add(edgeServer);
        }

        // Update the UI on the JavaFX Application Thread
        Platform.runLater(() -> {
            edgeServersData.clear();
            edgeServersData.addAll(updatedEdgeServers);
        });
    }

    private Tab createEdgeServersTab() {
        Tab edgeServersTab = new Tab("Edge Servers");
        edgeServersTab.setClosable(false);

        ListView<String> edgeServersList = new ListView<>(edgeServersData);

        VBox edgeServersContent = new VBox(edgeServersList);
        edgeServersTab.setContent(edgeServersContent);

        return edgeServersTab;
    }

    private Tab createFileOperationsTab() {
        Tab fileOperationsTab = new Tab("File Operations");
        fileOperationsTab.setClosable(false);

        TextField topicField = new TextField();
        topicField.setPromptText("Enter Topic");

        TextField fileNameField = new TextField();
        fileNameField.setPromptText("Enter File Name");

        Button downloadButton = new Button("Download File");
        Button uploadButton = new Button("Upload File");

        Label statusLabel = new Label();

        fileOperationsTab.setContent(new VBox(topicField, fileNameField, downloadButton, uploadButton, statusLabel));

        // Set actions for download and upload buttons
        downloadButton.setOnAction(e -> handleDownloadButton());
        uploadButton.setOnAction(e -> handleUploadButton());

        return fileOperationsTab;
    }

    private void handleDownloadButton() {
        // Implement download logic here
    }

    private void handleUploadButton() {
        // Implement upload logic here
    }

    public static void main(String[] args) {
        launch(args);
    }
}
