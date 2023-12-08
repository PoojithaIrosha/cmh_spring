package lk.cmh.web.cmh.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class MailServiceProvider {

    private static MailServiceProvider mailServiceProvider;
    private Authenticator authenticator;
    private ThreadPoolExecutor executor;
    private BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    private Environment env;

    public static MailServiceProvider getInstance() {
        if(mailServiceProvider == null) {
            mailServiceProvider = new MailServiceProvider();
        }
        return mailServiceProvider;
    }

    public void start() {
        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("mailtrap.user"), env.getProperty("mailtrap.password"));
            }
        };

        executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, blockingQueue, new ThreadPoolExecutor.AbortPolicy());
        executor.prestartAllCoreThreads();
        log.info("MailServiceProvider: Running...");
    }

    public void sendMail(Mailable mailable) {
        blockingQueue.offer(mailable);
    }

    public void shutDown() {
        if (executor != null) {
            executor.shutdown();
            System.out.println("MailServiceProvider: Shutting down...");
        }
    }
}
