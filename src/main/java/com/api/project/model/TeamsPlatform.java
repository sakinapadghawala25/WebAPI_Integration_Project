package com.api.project.model;

import com.api.project.service.ICommunicationPlatform;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TeamsPlatform implements ICommunicationPlatform {

    private final String token;
    private final String apiUrl = "https://graph.microsoft.com/v1.0/";

    public TeamsPlatform(String token) {
        this.token = token;
    }
    @Override
    public String sendMessage(String channelId, String message) {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            URIBuilder builder = new URIBuilder(apiUrl + "teams/" + channelId + "/sendActivityNotification");
            URI uri = builder.build();

            HttpPost post = new HttpPost(uri);
            post.setHeader("Authorization", "Bearer " + token);
            post.setHeader("Content-type", "application/json");

           /* Map<String, String> messageMap = new HashMap<>();
            messageMap.put("body", message);*/
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("topic", "New message");
            messageMap.put("activityType", "message");
            messageMap.put("activityTitle", "Message from Adapter");
            messageMap.put("text", message);
            messageMap.put("recipient", channelId);

            String json = new ObjectMapper().writeValueAsString(messageMap);
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                if (statusCode == 201) {
                    System.out.println("Message sent successfully: " + responseBody);
                } else {
                    System.out.println("Failed to send message: " + responseBody);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return message;
    }
       @Override
        public String startMeeting() {
        // Implement Microsoft Teams meeting starting logic here
            String url = apiUrl + "me/onlineMeetings";
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(url);
                post.setHeader("Authorization", "Bearer " + token);
                post.setHeader("Content-type", "application/json");

                Map<String, Object> meetingMap = new HashMap<>();
                meetingMap.put("startDateTime", "2023-12-31T23:59:59Z"); // Example date, adjust accordingly
                meetingMap.put("endDateTime", "2024-01-01T01:59:59Z"); // Example date, adjust accordingly
                meetingMap.put("subject", "Example Meeting");


                String json = new ObjectMapper().writeValueAsString(meetingMap);
                post.setEntity(new StringEntity(json));

                try (CloseableHttpResponse response = client.execute(post)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    String responseBody = EntityUtils.toString(response.getEntity());
                    if (statusCode == 201) {
                        Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);
                        String meetingUrl = (String) responseMap.get("joinUrl");
                        return "Teams meeting started: " + meetingUrl;
                    } else {
                        return "Failed to start Teams meeting: " + responseBody;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error starting Teams meeting";
            }
        }

}
