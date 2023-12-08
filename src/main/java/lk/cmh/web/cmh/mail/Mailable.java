package lk.cmh.web.cmh.mail;

import io.rocketbase.mail.EmailTemplateBuilder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@Getter
public abstract class Mailable implements Runnable {

    private final MailServiceProvider mailServiceProvider = MailServiceProvider.getInstance();
    private final EmailTemplateBuilder.EmailTemplateConfigBuilder emailTemplateBuilder = EmailTemplateBuilder.builder();
    private final JavaMailSender javaMailSender;

    public Mailable(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void run() {
        try {
            if (javaMailSender == null) {
                throw new IllegalStateException("JavaMailSender is not set. Make sure to set it before invoking the run method.");
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(new InternetAddress("noreply@cmh.lk"));
            buildMessage(mimeMessageHelper);
            if(message.getRecipients(Message.RecipientType.TO).length > 0) {
                javaMailSender.send(message);
                System.out.println("MailServiceProvider: Message sent successfully...");
            } else {
                System.out.println("MailServiceProvider: No recipients found...");
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void buildMessage(MimeMessageHelper mimeMessageHelper) throws MessagingException;
}
