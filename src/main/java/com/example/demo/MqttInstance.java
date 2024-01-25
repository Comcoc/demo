package com.example.demo;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttInstance {
    private static final String BROKER_URL = "tcp://172.17.0.1:1883";
    private static final int QOS = 2; // Quality of Service (0, 1, or 2)
    private static final String BROKER_USERNAME = "jetson";
    private static final String BROKER_PASSWORD = "jetson";

    public static MqttClient connect() throws MqttException {
        MqttClient mqttClient = new MqttClient(BROKER_URL, MqttClient.generateClientId());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        connOpts.setUserName(BROKER_USERNAME);
        connOpts.setPassword(BROKER_PASSWORD.toCharArray());

        mqttClient.connect(connOpts);

        return mqttClient;
    }

    public static void sendMessage(MqttClient client, String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(QOS);
        client.publish(topic, mqttMessage);
    }

    public static boolean tryDisconnect(MqttClient client) {
        try {
            client.disconnect();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
