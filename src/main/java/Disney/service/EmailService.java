package Disney.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    private final Email from = new Email("kessler1459@gmail.com");
    private final String SENDGRID_KEY="apikey";

    public void sendEmail(String subject,String content,String to) throws IOException {
        Email toEmail=new Email(to);
        Content emailContent=new Content("text/plain", content);
        Mail mail = new Mail(from, subject, toEmail, emailContent);
        SendGrid sg = new SendGrid(System.getenv(SENDGRID_KEY));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }
}
