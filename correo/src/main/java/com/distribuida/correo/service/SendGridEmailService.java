package com.distribuida.correo.service;

import com.distribuida.correo.modelo.EmailDetails;
import com.distribuida.correo.modelo.Emailinfo;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;


import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {

    @Value("${spring.sendgrid.api-key}")
    private String key;

    public String sendEmail(EmailDetails emailDetails) throws IOException {

        Emailinfo fromInfo = emailDetails.getFromAddress();
        Email  fromEmail = setEmail(fromInfo.getName(), fromInfo.getEmailAdrress() );



        Emailinfo toInfo = emailDetails.getToAddress();
        Email  toEmail = setEmail(toInfo.getName(), toInfo.getEmailAdrress() );

        Content content = new Content("text/plain", emailDetails.getEmailBody());

        Mail mail = new Mail(fromEmail,emailDetails.getSubject(), toEmail, content);

        SendGrid grid = new SendGrid(key);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response= grid.api(request);

        return response.getBody();


    }

    private Email setEmail(String name, String emailAddres){

        Email email = new Email();
        email.setEmail(emailAddres);
        email.setName(name);

        return email;
    }

}
