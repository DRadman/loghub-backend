package net.decodex.loghub.backend.services;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.models.Invitation;
import net.decodex.loghub.backend.domain.models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailjetClient mailjetClient;
    private final CryptoService cryptoService;

    @Value("${app.cms.url}")
    String cmsUrl;

    @Value("${app.name}")
    String appName;

    @Value("${mailjet.email}")
    String email;

    @Value("${mailjet.invitation-template-id}")
    int invitationTemplateId;

    @Value("${mailjet.reset-password-template-id}")
    int resetPasswordTemplateId;


    public void sendInvitationEmail(Invitation invitation) throws MailjetException {
        var request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", email)
                                        .put("Name", appName))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", invitation.getEmail())
                                                .put("Name", invitation.getEmail().split("@")[0])))
                                .put(Emailv31.Message.TEMPLATEID, invitationTemplateId)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, "Invitation to " + appName)
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("organization_name", invitation.getOrganization().getName())
                                        .put("invitation_link", cmsUrl + "/invitation/" + invitation.getInvitationId()))));
        mailjetClient.post(request);
    }

    public void sendForgotPassword(User user) throws MailjetException {
        var hash = cryptoService.encryptStringToBase64(user.getUserId());
        var request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", email)
                                        .put("Name", appName))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", user.getEmail())
                                                .put("Name", user.getFirstName())))
                                .put(Emailv31.Message.TEMPLATEID, resetPasswordTemplateId)
                                .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                .put(Emailv31.Message.SUBJECT, appName + " reset password")
                                .put(Emailv31.Message.VARIABLES, new JSONObject()
                                        .put("username", user.getUsername())
                                        .put("reset_password_link", cmsUrl + "/reset-password/" + hash))));
        mailjetClient.post(request);
    }

}
