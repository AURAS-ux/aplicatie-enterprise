package jmsemail;

import ejbs.Rental.Rental;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Named
@RequestScoped
public class EmailBean {

    @Resource(lookup = "mail/EmailSession")
    private Session jms;

    private String recipientEmail="auraspaltaneamarian@gmail.com";
    private String subject="Your return request has been recorded";
    private String body;

    public EmailBean(){
        System.out.println("Initializing EmailBean...");
    }

    @PostConstruct
    public void init(){
        System.out.println("Initializing EmailBean...");
    }

    public void sendEmail(Rental rental) {
        this.body = "Hello dear customer. Thank you for choosing us on your journey :)\nWe want to infor you that the request for " +
                "return of rental no."+rental.getRental_id() + " for car " + rental.getCar().getBrand() + " " + rental.getCar().getModel() +
                " has been successfully recorded.\n\n\n BR, Rental Team";
        try {
            MimeMessage message = new MimeMessage(jms);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            System.out.println("Preparring message to recipient " + recipientEmail);

            Transport.send(message);
            System.out.println("Sent message successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
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
