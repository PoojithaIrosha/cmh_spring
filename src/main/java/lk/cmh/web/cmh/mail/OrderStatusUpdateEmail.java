package lk.cmh.web.cmh.mail;

import io.rocketbase.mail.model.HtmlTextEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class OrderStatusUpdateEmail extends Mailable {

    private final String to;
    private final String firstName;
    private final long orderNumber;
    private final String productName;
    private final String status;

    public OrderStatusUpdateEmail(JavaMailSender javaMailSender, String to, String firstName, long orderNumber, String productName, String status) {
        super(javaMailSender);
        this.to = to;
        this.firstName = firstName;
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.status = status;
    }

    @Override
    public void buildMessage(MimeMessageHelper messageHelper) throws MessagingException {
        messageHelper.setTo(new InternetAddress(to));
        messageHelper.setSubject("Order Status Updated - CMH");

        HtmlTextEmail build = getEmailTemplateBuilder()
                .header()
                .text("Ceylon Market Hub")
                .and()
                .text("Hello, " + firstName + "!").h1().center().and()
                .text("").and()
                .text("Your order status has been updated.").and()
                .text("Order Number: " + orderNumber).and()
                .text("Product Name: " + productName).and()
                .text("Status: " + status).and()
                .html("If you have any questions, feel free to <a href=\"mailto:info@cmh.lk\">email our customer success team</a>. (We're lightning quick at replying.)").and()
                .text("Cheers,\n" +
                        "The CMH Team").and()
                .copyright("CMH").url("https://www.cmh.lk").suffix(". All rights reserved.").and()
                .footerText("CMH \n" +
                        "108 Panvila Rd, Wattegama, Kandy\n" +
                        "Sri lanka").and()
                .build();

        messageHelper.setText(build.getHtml(), true);
    }
}
