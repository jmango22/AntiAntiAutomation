/**
 * Created by mengje on 5/11/17.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MailHandlerServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(MailHandlerServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            MimeMessage message = new MimeMessage(session, req.getInputStream());

            String subject = message.getSubject();
            Address sender = message.getSender();
            Address email = message.getRecipients(MimeMessage.RecipientType.TO)[0];
            Date time = message.getSentDate();

            Document doc = Jsoup.parse(message.getContent().toString());
            String text = doc.body().text();
            String html = doc.body().html();

            log.info("Sender email: "+sender.toString());
            log.info("Sender name: "+message.getFrom()[0].toString());
            log.info("Message subject: "+subject);
            log.info("Sent to email: "+email.toString());
            log.info("Message")
            log.info("Message to: "+);

            RegisterMe registerMe = new RegisterMe();
            registerMe.activate(sender.toString(), message.getFrom()[0].toString(), email.toString(), text, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
