package enums;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class MailTypeProvider {
    public MailType getAcceptedRequest(){
        return MailType.ACCEPTED_REQUEST;
    }

    public MailType getRejectedRequest(){
        return MailType.DENIED_REQUEST;
    }

    public MailType getReceivedRequest(){
        return MailType.RECEIVED_REQUEST;
    }
}
