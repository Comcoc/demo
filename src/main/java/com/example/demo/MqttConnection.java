package com.example.demo;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MqttConnection {

    public static void main(String[] args) {

        String topic = "jetson_discovery";
        String content = "Message from MqttPublishSample";
        int qos = 2;
        String broker = "tcp://172.17.0.1:1883";
        String username = "jetson";
        String password = "jetson";

        try {
            org.eclipse.paho.client.mqttv3.MqttClient sampleClient =
                    new org.eclipse.paho.client.mqttv3.MqttClient(broker, MqttClient.generateClientId(), null);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Set username and password
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());

            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected with ID: " + sampleClient.getClientId());

            System.out.println("Publishing message: " + content);
            sampleClient.publish(topic, content.getBytes(), qos, false);
            System.out.println("Message published");

            //sampleClient.disconnect();
            //System.out.println("Disconnected");
        } catch (org.eclipse.paho.client.mqttv3.MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
