package jmsemail;

import com.google.gson.Gson;
import ejbs.Rental.Rental;
import ejbs.Rental.RentalServiceRemote;
import enums.MailType;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import managedbeans.RentManagedBean;
import status.Status;

@Named
@RequestScoped
public class EmailBean {

    @Resource(lookup = "mail/EmailSession")
    private Session jms;
    @EJB
    private RentalServiceRemote rentService;
    @Inject
    private RentManagedBean rentManagedBean;

    private String recipientEmail="auraspaltaneamarian@gmail.com";
    private String subject;
    private String body;

    public EmailBean(){
        System.out.println("Initializing EmailBean...");
    }

    @PostConstruct
    public void init(){
        System.out.println("Initializing EmailBean...");
    }

    public void sendEmail(Rental rental, MailType type) {
        switch (type) {
            case RECEIVED_REQUEST:
                this.body = "Hello dear customer. Thank you for choosing us on your journey :)\nWe want to inform you that the request for " +
                        "return of rental no. " + rental.getRental_id() + " for car " + rental.getCar().getBrand() + " " + rental.getCar().getModel() +
                        " has been successfully recorded.\n\n\n BR, Rental Team";
                this.subject="Return request has been received ⏳";
                rental.setStatus(Status.IN_PROGRESS);
                break;

            case ACCEPTED_REQUEST:
                this.body = "Hello dear customer. We are happy to announce you that your return request has been accepted for " +
                        "rental no. " + rental.getRental_id() + " for car " + rental.getCar().getBrand() + " " + rental.getCar().getModel() +
                        ".\n\n\n BR, Rental Team";
                this.subject="Return request has been aproved ✅";
                rental.setStatus(Status.COMPLETED);
                break;

            case DENIED_REQUEST:
                this.body = "Hello dear customer. We regret to announce you that your return request has been denied for " +
                        "rental no. " + rental.getRental_id() + " for car " + rental.getCar().getBrand() + " " + rental.getCar().getModel() +
                        ". Please contact a member of our team for additional information at rentacar@ty.com.\n\n\n BR, Rental Team";
                this.subject="Return request has been rejected ❌";
                rental.setStatus(Status.DENIED);
                break;

            default:
                throw new IllegalArgumentException("Unknown MailType: " + type);
        }

        try {
            MimeMessage message = new MimeMessage(jms);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject,"UTF-8");
            message.setText(body);

            Gson gson = new Gson();
            rentService.updateRental(gson.toJson(rental));
            Transport.send(message);
            rentManagedBean.RefreshRents();
            System.out.println("Sent message successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
