package com.api.project.controller;

import com.api.project.model.TMeetingRequest;
import com.api.project.service.TeamsMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/teams")
public class TeamsController {

    @Autowired
    private TeamsMeetingService teamsAdapter;

    @PostMapping("/schedule-meeting" )
    public String scheduleTeamsMeeting(@RequestBody TMeetingRequest meetingRequest) {
        return teamsAdapter.scheduleTeamsMeeting(meetingRequest);
    }

}
