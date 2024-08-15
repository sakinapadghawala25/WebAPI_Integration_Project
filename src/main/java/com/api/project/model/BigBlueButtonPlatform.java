package com.api.project.model;

import com.api.project.service.ICommunicationPlatform;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.security.MessageDigest;
import java.util.Formatter;

public class BigBlueButtonPlatform implements ICommunicationPlatform {
    private  String serverUrl;
    private  String secret;

    public BigBlueButtonPlatform(String serverUrl, String secret) {
        this.serverUrl = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
        this.secret = secret;
    }

        @Override
        public String sendMessage(String channelId, String message) {
            // Implement BigBlueButton message sending logic here
            System.out.println("BigBlueButton does not support direct messaging via API.");
            return "BigBlueButton does not support direct messaging via API.";
        }

        @Override
        public String startMeeting() {
            // Implement BigBlueButton meeting starting logic here
            String meetingId = "example-meeting-id";
            String meetingName = "Example Meeting";

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                URIBuilder builder = new URIBuilder(serverUrl + "/api/create");
                builder.addParameter("name", meetingName);
                builder.addParameter("meetingID", meetingId);
                builder.addParameter("attendeePW", "ap");
                builder.addParameter("moderatorPW", "mp");

                String queryString = builder.build().getQuery();
                String checksum = createChecksum("create" + queryString + secret);

                URI uri = builder.addParameter("checksum", checksum).build();
                System.out.println("Constructed URI: " + uri);
                HttpGet request = new HttpGet(uri);

                try (CloseableHttpResponse response = client.execute(request)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    String responseBody = EntityUtils.toString(response.getEntity());


                    if (statusCode == 200 && responseBody.contains("<returncode>SUCCESS</returncode>")) {
                        return "BigBlueButton meeting started: " + serverUrl + "/bigbluebutton/api/join?meetingID=" + meetingId + "&fullName=Guest&password=ap";
                    } else {
                        return "Failed to start BigBlueButton meeting: " + responseBody;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error starting BigBlueButton meeting";
            }
        }

    private String createChecksum(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(input.getBytes()));
    }

    private String byteArray2Hex(final byte[] hash) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }
}
