package com.api.project.service;

import com.api.project.model.BBBMeetingRequest;
import com.api.project.util.BigBlueButtonUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BigBBMeetingService {

    private static final String BBB_API_URL = "http://yourserver.com/bigbluebutton/api/";
    private static final String SHARED_SECRET = "ECCJZNJWLPEA3YB6Y2LTQGQD3GJZ3F93";
    public String createBbbMeeting(BBBMeetingRequest request, HttpServletResponse response) throws IOException {

        if (BBB_API_URL == null || SHARED_SECRET == null) {
            throw new IllegalStateException("BBB API URL or Shared Secret is not configured.");
        }

        if (request == null || request.getMeetingID() == null || request.getMeetingName() == null ||
                request.getModeratorPassword() == null || request.getAttendeePassword() == null) {
            throw new IllegalArgumentException("Missing meeting request details");
        }
        HttpClient httpClient = HttpClients.createDefault();
        String createMeetingUrl = buildCreateMeetingUrl(request.getMeetingID(), request.getMeetingName(), request.getModeratorPassword(), request.getAttendeePassword());
        HttpPost httpPost = new HttpPost(createMeetingUrl);

        HttpResponse httpResponse = httpClient.execute(httpPost);
       // String responseBody = EntityUtils.toString(httpResponse.getEntity());

        response.setStatus(httpResponse.getStatusLine().getStatusCode());
        //response.getWriter().write(responseBody);

        return  EntityUtils.toString(httpResponse.getEntity());

    }

    private String buildCreateMeetingUrl(String meetingID, String meetingName, String moderatorPassword, String viewerPassword) {
        String params = String.format("name=%s&meetingID=%s&attendeePW=%s&moderatorPW=%s", meetingName, meetingID, viewerPassword, moderatorPassword);
        String checksum = BigBlueButtonUtil.calculateChecksum("create" + params, SHARED_SECRET);
        return String.format("%s/create?%s&checksum=%s", BBB_API_URL, params, checksum);
    }

    public String buildJoinMeetingUrl(String meetingID, String username, String password) {
        String params = String.format("fullName=%s&meetingID=%s&password=%s", username, meetingID, password);
        String checksum = BigBlueButtonUtil.calculateChecksum("join" + params, SHARED_SECRET);
        return String.format("%s/join?%s&checksum=%s", BBB_API_URL, params, checksum);
    }
}
