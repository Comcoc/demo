package com.example.demo;

import org.eclipse.paho.client.mqttv3.*;

import java.net.InetAddress;
import java.util.Timer;

public class Network {
    public static String nodeId;

    public static boolean login() {
        MqttClient client = null;

        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String myIp = localhost.getHostAddress();
            String myHostname = localhost.getHostName();
            String message = "{\"ip\":\"" + myIp + "\",\"hostname\":\"" + myHostname + "\"}";

            client = MqttInstance.connect();
            client.subscribe("registration_confirmation");
            MqttInstance.sendMessage(client, "pre_msg", message);
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals("registration_confirmation"))
                        nodeId = message.toString();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            while (nodeId == null)
                Thread.sleep(100);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        } finally {
            MqttInstance.tryDisconnect(client);
        }
    }
}
