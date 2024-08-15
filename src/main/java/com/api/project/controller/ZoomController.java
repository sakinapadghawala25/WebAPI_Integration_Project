package com.api.project.controller;


import com.api.project.service.ZoomAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/zoom")
public class ZoomController {

    private final ZoomAdapter zoomAdapter;

    @Autowired
    public ZoomController(ZoomAdapter zoomAdapter) {
        this.zoomAdapter = zoomAdapter;
    }


    @PostMapping("/message")
    public ResponseEntity<String> sendMessageToZoom(
            @RequestParam String channelId,
            @RequestParam String message
    ) {
        try {
            // Call a method in your ZoomAdapter to send the message to the specified channel
            boolean messageSent = zoomAdapter.sendMessage(channelId, message);

            if (messageSent) {
                return ResponseEntity.ok("Message sent to Zoom");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message to Zoom");
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during the process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/meetings")
    public ResponseEntity<String> scheduleMeeting(@RequestBody Map<String, Object> meetingDetails) {
        boolean result = zoomAdapter.scheduleMeeting(meetingDetails);
        if (result) {
            return ResponseEntity.ok("Meeting scheduled successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to schedule meeting");
        }
    }
}