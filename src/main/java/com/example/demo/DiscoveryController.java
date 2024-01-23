package com.example.demo;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DiscoveryController {

    @FXML
    private Button discoverButton;

    @FXML
    private ListView<String> topicList;

    @FXML
    private ListView<String> dataList;

    @FXML
    private Button downloadButton;

    @FXML
    private Button uploadButton;

    @FXML
    private ProgressBar loadingProgressBar;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        // Add a selection listener to enable/disable buttons based on topic selection
        topicList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateDataList();
            }
        });

        // Add a selection listener to enable/disable buttons based on data selection
        dataList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateButtonStates();
            }
        });
    }

    @FXML
    private void onDiscoverButtonClick(ActionEvent event) {
        // Call your MQTT discovery function here
        // Once you receive the response, update the topicList
        updateTopicListWithDiscoveryResult();
    }

    @FXML
    private void onDownloadButtonClick(ActionEvent event) {
        // Implement download logic here
        String selectedTopic = topicList.getSelectionModel().getSelectedItem();
        String selectedData = dataList.getSelectionModel().getSelectedItem();
        if (selectedTopic != null && selectedData != null) {
            // Open a file dialog or perform the download logic based on the selected topic and data
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Data");
            File file = fileChooser.showSaveDialog(primaryStage);
            // Implement logic to download data to the selected file
        }
    }

    @FXML
    private void onUploadButtonClick(ActionEvent event) {
        // Implement upload logic here
        String selectedTopic = topicList.getSelectionModel().getSelectedItem();
        if (selectedTopic != null) {
            // Open a file dialog or perform the upload logic based on the selected topic
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Data File");
            File file = fileChooser.showOpenDialog(primaryStage);
            // Implement logic to upload data from the selected file to the topic
        }
    }

    private void updateTopicListWithDiscoveryResult() {
        // Replace this with your logic to get the topics from the discovery response
        // For example, if you have a list of topics, you can do something like:
        topicList.getItems().clear();
        topicList.getItems().addAll("Topic1", "Topic2", "Topic3");
        // Clear data list when a new topic is selected
        dataList.getItems().clear();
        // Enable/disable buttons based on whether a topic is selected
        updateButtonStates();
    }

    private void updateDataList() {
        String selectedTopic = topicList.getSelectionModel().getSelectedItem();
        if (selectedTopic != null) {
            // Print the selected topic to the console
            System.out.println("Selected Topic: " + selectedTopic);

            // Simulate fetching data titles with a Task and ProgressBar
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // Simulate fetching data titles (replace this with your actual logic)
                    // For demo purposes, using a dummy list
                    List<String> dummyDataTitles = Arrays.asList("Title1", "Title2", "Title3");

                    // Update the UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        dataList.getItems().clear();
                        dataList.getItems().addAll(dummyDataTitles);
                    });

                    return null;
                }
            };

            //loadingProgressBar.progressProperty().bind(task.progressProperty());

            // Execute the task in a new thread
            new Thread(task).start();
        }
    }



    private void updateButtonStates() {
        boolean isDataSelected = !dataList.getSelectionModel().isEmpty();
        downloadButton.setDisable(!isDataSelected);
        uploadButton.setDisable(!isDataSelected);
    }
}