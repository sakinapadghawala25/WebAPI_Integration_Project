package com.api.project.model;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackClient {

    private static final String token =  "YOUR_SLACK_OAUTH_TOKEN";
    private static final String base_url = "https://slack.com/api/";

    public String sendMessage(String channelId, String message) throws IOException {
            String endpoint = "chat.postMessage";
            URL url = new URL(base_url + endpoint);

            JsonObject payload = new JsonObject();
            payload.addProperty("channelId", channelId);
            payload.addProperty("message", message);

            return post(url, payload.toString());
        }

        private String post(URL url, String payload) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));

            if (connection.getResponseCode() == 200) {
                return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } else {
                throw new IOException("Failed to send message to Slack. Status code: " + connection.getResponseCode());
            }
        }


}
