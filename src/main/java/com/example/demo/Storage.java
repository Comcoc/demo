package com.example.demo;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Storage {
    private static TopicCapability[] topics;

    public static TopicCapability[] getTopics() {
        return topics;
    }

    private static void listenThread() {
        MqttClient client = null;

        try {
            client = MqttInstance.connect();

            for (int i = 0; i < topics.length; i++)
                client.subscribe(topics[i].topic);
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("storage " + topic + " " + message.toString());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            while (true)
                Thread.sleep(999999);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            MqttInstance.tryDisconnect(client);
        }
    }

    public static boolean init(TopicCapability[] _topics) {
        topics = _topics;

        Thread listenThread = new Thread(Storage::listenThread);
        listenThread.start();

        return Network.updateCapabilities(topics);
    }
}