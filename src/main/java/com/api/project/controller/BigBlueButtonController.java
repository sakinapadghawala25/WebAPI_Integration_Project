package com.api.project.controller;

import com.api.project.model.BBBMeetingRequest;
import com.api.project.service.BigBBMeetingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/bigbluebutton")
public class BigBlueButtonController {


    @Autowired
    private BigBBMeetingService bigBBMeetingService;


    @PostMapping("/createmeetings")
    public String createBBBMeeting(@RequestBody BBBMeetingRequest request, HttpServletResponse response) throws IOException {
        return bigBBMeetingService.createBbbMeeting(request, response);
    }

    @GetMapping("/join/{meetingID}/{username}/{password}")
    public void joinBBBMeeting(@PathVariable String meetingID, @PathVariable String username, @PathVariable String password, HttpServletResponse response) throws IOException {
        String joinMeetingUrl = bigBBMeetingService.buildJoinMeetingUrl(meetingID, username, password);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(joinMeetingUrl);
    }
}
