package dev.berke.app.email;

import dev.berke.app.kafka.payment.PaymentMethod;
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
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConfirmationEmail {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendPaymentSuccessEmail(
            String customerName,
            String destinationEmail,
            BigDecimal totalPrice,
            PaymentMethod paymentMethod
    ) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        mimeMessageHelper.setFrom("online.shopping.app@gmail.com");
        final String templateName = EmailTemplates.PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("totalPrice", totalPrice);
        variables.put("paymentMethod", paymentMethod);

        Context context = new Context();
        context.setVariables(variables);
        mimeMessageHelper.setSubject(EmailTemplates.PAYMENT_CONFIRMATION.getSubject());

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