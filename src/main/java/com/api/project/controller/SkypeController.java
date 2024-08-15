package com.api.project.controller;

import com.api.project.model.SkypeMeetingRequest;
import com.api.project.model.SkypeMeetingResponse;
import com.api.project.service.SkypeMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/skype")
public class SkypeController {

    @Autowired
    private SkypeMeetingService skypeMeetingService;

    @PostMapping("/schedule")
    public SkypeMeetingResponse scheduleSkypeMeeting(@RequestBody SkypeMeetingRequest meetingRequest) throws IOException {
        return skypeMeetingService.scheduleSkypeMeeting(meetingRequest);
    }

}
