package entelect.training.incubator.spring.notification.listener;

import entelect.training.incubator.spring.notification.model.SmsNotification;
import entelect.training.incubator.spring.notification.sms.client.SmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationListener.class);

    private final SmsClient smsClient;

    public NotificationListener(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    @JmsListener(destination = "${notification.queue}")
    public void onBookingNotification(SmsNotification notification) {
        LOGGER.info("Received booking notification for {}", notification.getPhoneNumber());

        smsClient.sendSms(notification.getPhoneNumber(), notification.getMessage());

        LOGGER.info("SMS sent to {}", notification.getPhoneNumber());
    }
}
