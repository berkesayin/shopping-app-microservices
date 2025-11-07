package dev.berke.app.notification.infrastructure.email;

import dev.berke.app.consumer.model.BasketItem;
import dev.berke.app.consumer.model.PaymentMethod;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConfirmationEmail {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendOrderConfirmationEmail(
            String customerName,
            String destinationEmail,
            String reference,
            PaymentMethod paymentMethod,
            List<BasketItem> productsToPurchase,
            BigDecimal totalPrice
    ) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        mimeMessageHelper.setFrom("online.shopping.app@gmail.com");
        final String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("reference", reference);
        variables.put("paymentMethod", paymentMethod);
        variables.put("productsToPurchase", productsToPurchase);
        variables.put("totalPrice", totalPrice);

        Context context = new Context();
        context.setVariables(variables);
        mimeMessageHelper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = springTemplateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate, true);

            mimeMessageHelper.setTo(destinationEmail);
            javaMailSender.send(mimeMessage);
            log.info(String.format
                    ("INFO - Email sent to %s with template %s, ", destinationEmail, templateName ));
        } catch (MessagingException exp) {
            log.warn("WARNING - Cannot send email to {}", destinationEmail);
        }
    }

}