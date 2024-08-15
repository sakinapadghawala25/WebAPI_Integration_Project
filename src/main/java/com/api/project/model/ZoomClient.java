package com.api.project.model;

import com.api.project.service.ZoomTokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ZoomClient {

    private static final String ZOOM_API_BASE_URL = "https://api.zoom.us/v2";
    private static final String ZOOM_API_TOKEN = ZoomTokenGenerator.generateToken(); // Replace with your actual Zoom API token

    public void post(String endpoint, Map<String, ?> requestBody) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ZOOM_API_BASE_URL + endpoint);

        httpPost.setHeader("Authorization", "Bearer " + ZOOM_API_TOKEN);
        httpPost.setHeader("Content-Type", "application/json");

        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);
        StringEntity entity = new StringEntity(requestBodyJson, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();

        String responseBody = EntityUtils.toString(response.getEntity());
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("Failed to send message to Zoom. Status code: " + statusCode + ", Response body: " + responseBody);
        }
        if (statusCode == 201) {
            Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);
            String joinUrl = (String) responseMap.get("join_url");
            message(joinUrl);
        }
    }

    public void sendMessage(String channelId, String message) throws IOException {
        String endpoint = "/chat/users/me/messages";
        Map<String, String> requestBody = Map.of(
                "to_channel", channelId,
                "message", message
        );
        post(endpoint, requestBody);
    }

    public String message(String url){
        return "Zoom meeting started successfully: " + url;
    }


    // Method to schedule a meeting
    public void scheduleMeeting(Map<String, Object> meetingDetails) throws IOException {
        String endpoint = "/users/me/meetings"; // Check Zoom API docs for the correct endpoint

        post(endpoint, meetingDetails);
    }
}
