package com.api.project.service;

import com.api.project.model.ZoomClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class ZoomAdapter {

    public boolean sendMessage(String channelId, String message) {
        try {
            ZoomClient zoomClient = new ZoomClient();
            zoomClient.sendMessage(channelId, message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean scheduleMeeting(Map<String, Object> meetingDetails) {
        try {
            ZoomClient zoomClient = new ZoomClient();
            zoomClient.scheduleMeeting(meetingDetails);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
