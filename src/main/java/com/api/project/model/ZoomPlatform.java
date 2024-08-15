package com.api.project.model;


import com.api.project.service.ICommunicationPlatform;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ZoomPlatform implements ICommunicationPlatform {

    private final String token;
    private final String apiUrl = "https://api.zoom.us/v2/";
    public ZoomPlatform(String token) {
        this.token = token;
    }

    @Override
    public String sendMessage(String channelId, String message) {
        throw new UnsupportedOperationException("Sending messages is not supported in Zoom.");
    }
    @Override
    public String startMeeting() {
        String url = apiUrl + "users/me/meetings";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Bearer " + token);
            post.setHeader("Content-type", "application/json");

            Map<String, Object> meetingDetails = new HashMap<>();
            meetingDetails.put("topic", "Example Meeting");
            meetingDetails.put("type", 1); // Instant meeting
            meetingDetails.put("settings", new HashMap<>());

            String json = new ObjectMapper().writeValueAsString(new Meeting(meetingDetails.toString()));
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                if (statusCode == 201) {
                    // Parse the response to get the meeting URL
                    // This is a simplified example; you need to handle the response properly
                    Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);
                    String joinUrl = (String) responseMap.get("join_url");
                    return "Zoom meeting started successfully: " + joinUrl;

                } else {
                    return "Failed to start meeting" + responseBody;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error starting meeting";
        }
    }

    private static class Meeting {
        public String topic;

        public Meeting(String topic) {
            this.topic = topic;
        }
    }
}