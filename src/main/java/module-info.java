module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.paho.client.mqttv3;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}