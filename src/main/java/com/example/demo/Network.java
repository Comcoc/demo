package com.example.demo;

import org.eclipse.paho.client.mqttv3.*;

import java.net.InetAddress;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Network {
    public static String nodeId, _temp;
    private static CountDownLatch latch = new CountDownLatch(1);

    public static boolean isLoggedIn() {
        return nodeId != null;
    }

    public static boolean login() {
        MqttClient client = null;

        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String myIp = localhost.getHostAddress();
            String myHostname = localhost.getHostName();
            String response_topic = "registration_confirmation_" + Utils.generateRandomString(8);
            String message = "{\"ip\":\"" + myIp + "\",\"hostname\":\"" + myHostname + "\",\"response_topic\":\"" + response_topic + "\"}";

            client = MqttInstance.connect();
            client.subscribe(response_topic);
            MqttInstance.sendMessage(client, "pre_msg", message);
            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(response_topic))
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

    public static String[] getServers(){
        MqttClient client = null;

        try {
            client = MqttInstance.connect();
            client.subscribe("demande_topic");
            MqttInstance.sendMessage(client, "demande_topic", "topic?");

            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals("demande_topic")) {
                        _temp = message.toString();
                        latch.countDown();  // Release the latch when message is received
                    }
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            // Wait for a message for up to 10 seconds
            latch.await(10, TimeUnit.SECONDS);

            List<String> values = new ArrayList<>();

            if (_temp != null && !_temp.isEmpty()) {
                String[] entries = _temp.split("\\n");

                for (String entry : entries) {
                    String[] parts = entry.split("\\|");
                    if (parts.length > 0) {
                        values.add(parts[0]);
                    }
                }
            }

            return values.toArray(new String[0]);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        } finally {
            MqttInstance.tryDisconnect(client);
        }
    }

    public static boolean updateCapabilities(TopicCapability[] topics) {
        MqttClient client = null;

        if (!isLoggedIn()) {
            System.out.println("updateCapabilities: not logged in!");
            return false;
        }

        try {
            String bloc_fichier = "";
            String message = "{'node_id_envoyeur':'" + nodeId + "','capa_topic':{";

            for (int i = 0; i < topics.length; i++)
                message += "'" + topics[i].topic + "': '" + topics[i].bytes + "'";

            message += "}}";

            client = MqttInstance.connect();
            MqttInstance.sendMessage(client, "envoyer_fichier", message);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        } finally {
            MqttInstance.tryDisconnect(client);
        }
    }

    public static String encryptDataChunk(byte[] data){
        return "";
    }

    public static boolean uploadDataChunk(String topic, String name, byte[] chunk) {
        MqttClient client = null;

        if (!isLoggedIn()) {
            System.out.println("uploadDataChunk: not logged in!");
            return false;
        }

        try {
            String bloc_fichier = encryptDataChunk(chunk);
            String message = "{\"topic_fichier\":\"" + topic + "\",\"nom_fichier\":\"" + name + "\",\"node_id_envoyeur\":\"" + nodeId + "\",\"fichier\":\"" + bloc_fichier + "\",\"taille_fichier\":\"" + bloc_fichier.length() + "\"}";

            client = MqttInstance.connect();
            MqttInstance.sendMessage(client, "envoyer_fichier", message);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        } finally {
            MqttInstance.tryDisconnect(client);
        }
    }
}
