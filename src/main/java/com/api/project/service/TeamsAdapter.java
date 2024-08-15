package com.api.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.w3c.dom.events.Event;
import java.time.LocalDateTime;

@Service
public class TeamsAdapter {

    @Autowired
    private TAuthenticationService tAuthenticationService;

    public String scheduleMeeting(String meetingTitle, String startTime, String attendees) {
        String accessToken = tAuthenticationService.getAccessToken();
        String url = "https://graph.microsoft.com/v1.0/me/events";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String meetingDetails = "{"
                + "\"subject\": \"" + meetingTitle + "\","
                + "\"start\": {\"dateTime\": \"" + startTime + "\", \"timeZone\": \"UTC\"},"
                + "\"end\": {\"dateTime\": \"" + getEndTime(startTime) + "\", \"timeZone\": \"UTC\"},"
                + "\"attendees\": [" + getAttendees(attendees) + "]"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(meetingDetails, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            return "Meeting scheduled successfully.";
        } else {
            return "Failed to schedule meeting: " + response.getBody();
        }
    }

    private String getEndTime(String startTime) {
        // Logic to calculate end time, assuming 1 hour duration
        // This can be adjusted based on requirements
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = start.plusHours(1);
        return end.toString();
    }

    private String getAttendees(String attendees) {
        StringBuilder attendeesList = new StringBuilder();
        for (String email : attendees.split(",")) {
            attendeesList.append("{\"emailAddress\": {\"address\": \"" + email.trim() + "\", \"name\": \"" + email.trim() + "\"}, \"type\": \"required\"},");
        }
        return attendeesList.toString();
    }
}
