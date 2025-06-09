package sns;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import s3logger.S3Logger;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class SNSClientService {
    private SnsClient snsClient;
    private Map<NotificationType, String> topicArns = new HashMap<>();

    @Inject
    private S3Logger s3Logger;

    @PostConstruct
    public void initialize() {
        snsClient = SnsClient.builder()
                .region(Region.US_EAST_1)
                .build();

        initializeTopics();
    }

    private void initializeTopics() {
        for (NotificationType type : NotificationType.values()) {
            String topicName = "car-rental-" + type.name().toLowerCase();
            String topicArn = getOrCreateTopic(topicName);
            topicArns.put(type, topicArn);
        }
    }

    private String getOrCreateTopic(String topicName) {
        try {
            ListTopicsResponse listTopicsResponse = snsClient.listTopics();
            for (Topic topic : listTopicsResponse.topics()) {
                if (topic.topicArn().contains(topicName)) {
                    return topic.topicArn();
                }
            }

            CreateTopicResponse createTopicResponse = snsClient.createTopic(
                    CreateTopicRequest.builder().name(topicName).build());
            return createTopicResponse.topicArn();
        } catch (SnsException e) {
            throw new RuntimeException("Failed to create SNS topic: " + topicName, e);
        }
    }

    public void sendSMS(String phoneNumber, String message) {
        try {
            PublishRequest request = PublishRequest.builder()
                    .phoneNumber(phoneNumber)
                    .message(message)
                    .build();

            snsClient.publish(request);
            s3Logger.logSms(phoneNumber, message, true);

        } catch (SnsException e) {
            throw new RuntimeException("Failed to send SMS notification", e);
        }
    }

    public void publishToTopic(NotificationType type, String message) {
        String topicArn = topicArns.get(type);
        if (topicArn == null) {
            throw new IllegalArgumentException("No topic configured for type: " + type);
        }

        try {
            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .build();

            snsClient.publish(request);
        } catch (SnsException e) {
            throw new RuntimeException("Failed to publish to topic: " + type, e);
        }
    }

    public String subscribeToTopic(NotificationType type, String endpoint, Protocol protocol) {
        String topicArn = topicArns.get(type);
        if (topicArn == null) {
            throw new IllegalArgumentException("No topic configured for type: " + type);
        }

        try {
            SubscribeRequest request = SubscribeRequest.builder()
                    .topicArn(topicArn)
                    .protocol(protocol.toString().toLowerCase())
                    .endpoint(endpoint)
                    .build();

            SubscribeResponse response = snsClient.subscribe(request);
            return response.subscriptionArn();
        } catch (SnsException e) {
            throw new RuntimeException("Failed to subscribe to topic: " + type, e);
        }
    }
}