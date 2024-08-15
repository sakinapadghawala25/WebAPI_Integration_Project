package com.api.project.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class TeamsConsumer {

    private final static String TOPIC = "skype_to_teams";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String GROUP_ID = "teams_group";
    private static final String GRAPH_API_URL = "";

    // Azure AD credentials
    private static final String CLIENT_ID = "your-client-id";
    private static final String CLIENT_SECRET = "your-client-secret";
    private static final String TENANT_ID = "your-tenant-id";
    private static final String MS_GRAPH_SCOPE = "https://graph.microsoft.com/.default";
    private static final String AUTH_URL = "https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/token";



    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            records.forEach(record -> {
                System.out.printf("Consumed record with key %s and value %s%n", record.key(), record.value());
                // Process the message for MS Teams
                try {
                    processTeamsMessage(record.value());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private static void processTeamsMessage(String message) throws IOException {
        String accessToken = getAccessToken();

        if (accessToken != null) {
            OkHttpClient client = new OkHttpClient();

            String teamsMessage = "{ \"body\": { \"content\": \"" + message + "\" } }";

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), teamsMessage);

            Request request = new Request.Builder()
                    .url(GRAPH_API_URL + "/teams/your-team-id/channels/your-channel-id/messages")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Message sent to MS Teams: " + message);
                } else {
                    System.err.println("Failed to send message to MS Teams: " + response.message());
                }
            }
        } else {
            System.err.println("Failed to obtain access token.");
        }
    }

    private static String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("scope", MS_GRAPH_SCOPE)
                .add("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(AUTH_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(responseBody).get("access_token").asText();
            } else {
                System.err.println("Failed to get access token: " + response.message());
                return null;
            }
        }
    }
}
