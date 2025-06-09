package sns;

/**
 * Supported AWS SNS subscription protocols
 */
public enum Protocol {
    SMS, // For text messages to phone numbers
    EMAIL, // For email notifications
    HTTP, // For HTTP endpoints
    HTTPS, // For HTTPS endpoints
    SQS, // For Amazon SQS queues
    LAMBDA, // For AWS Lambda functions
    APPLICATION // For mobile push notifications
}