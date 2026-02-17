package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.SmsNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationPublisher.class);

    private final JmsTemplate jmsTemplate;
    private final String queueName;

    public NotificationPublisher(JmsTemplate jmsTemplate,
                                 @Value("${notification.queue}") String queueName) {
        this.jmsTemplate = jmsTemplate;
        this.queueName = queueName;
    }

    public void publishBookingNotification(SmsNotification notification) {
        LOGGER.info("Publishing booking notification to queue '{}' for {}", queueName, notification.getPhoneNumber());
        jmsTemplate.convertAndSend(queueName, notification);
        LOGGER.info("Notification published successfully");
    }
}
