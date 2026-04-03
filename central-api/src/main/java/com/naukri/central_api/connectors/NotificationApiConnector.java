package com.naukri.central_api.connectors;

import com.naukri.central_api.model.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class NotificationApiConnector extends RestApi{

    @Value("${notification.api.baseurl}")
    String baseUrl;
    /**
     * when this method get triggered we will be calling invite recruiter endpoint
     * @return recruiter*/

    public void callInviteRecruiterEndpoint(AppUser recruiter, String token){

        String url = baseUrl + "/company/invite-recruiter/" + token;
        this.makePutCall(url, recruiter, new HashMap<>());

    }

    public void callAcceptInvitationEndpoint(List<AppUser> mailDetails){
        String url = baseUrl + "/company/accept-invitation";
        this.makePutCall(url, mailDetails, new HashMap());
    }
}
