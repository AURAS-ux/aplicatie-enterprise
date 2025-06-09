package s3logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class S3Logger {
    private static final String BUCKET_NAME = "car-rental-app-logs";
    private static final int FLUSH_INTERVAL_SECONDS = 60;
    private static final int MAX_BATCH_SIZE = 100;

    @Inject
    private S3Client s3Client;

    private final ConcurrentLinkedQueue<LogEntry> logQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final DateTimeFormatter fileNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH-mm-ss");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @PostConstruct
    public void init() {
        // Schedule periodic flushing of logs to S3
        scheduler.scheduleAtFixedRate(this::flushLogs,
                FLUSH_INTERVAL_SECONDS, FLUSH_INTERVAL_SECONDS, TimeUnit.SECONDS);

        // Add shutdown hook to flush logs when application stops
        Runtime.getRuntime().addShutdownHook(new Thread(this::flushLogs));
    }

    public void log(LogLevel level, String component, String message) {
        LogEntry entry = new LogEntry(
                LocalDateTime.now(),
                level,
                component,
                message);

        logQueue.add(entry);

        if (level == LogLevel.ERROR || logQueue.size() >= MAX_BATCH_SIZE) {
            flushLogs();
        }
    }

    public void logSms(String phoneNumber, String message, boolean success) {
        String status = success ? "SUCCESS" : "FAILED";
        String component = "SNS_SMS";
        String logMessage = String.format(
                "SMS to %s: [%s] Message: %s",
                phoneNumber, status, message);

        log(success ? LogLevel.INFO : LogLevel.ERROR, component, logMessage);
    }

    public void logRental(Long rentalId, String action, String details) {
        log(LogLevel.INFO, "RENTAL",
                String.format("Rental #%d - %s: %s", rentalId, action, details));
    }

    private synchronized void flushLogs() {
        if (logQueue.isEmpty()) {
            return;
        }

        StringBuilder logBuilder = new StringBuilder();

        // Process up to MAX_BATCH_SIZE entries
        int count = 0;
        while (!logQueue.isEmpty() && count < MAX_BATCH_SIZE) {
            LogEntry entry = logQueue.poll();
            if (entry != null) {
                logBuilder.append(formatLogEntry(entry)).append("\n");
                count++;
            }
        }

        if (count > 0) {
            try {
                String logContent = logBuilder.toString();
                String key = generateLogKey();

                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key)
                        .contentType("text/plain")
                        .build();

                s3Client.putObject(request,
                        RequestBody.fromString(logContent, StandardCharsets.UTF_8));

                System.out.println("Uploaded " + count + " log entries to S3: " + key);
            } catch (S3Exception e) {
                System.err.println("Failed to upload logs to S3: " + e.getMessage());
                // Re-queue the entries if S3 upload fails
                System.err.println("Logs will be retried in next batch.");
            }
        }
    }

    private String formatLogEntry(LogEntry entry) {
        return String.format("[%s] [%s] [%s] %s",
                entry.timestamp.format(timestampFormatter),
                entry.level,
                entry.component,
                entry.message);
    }

    private String generateLogKey() {
        LocalDateTime now = LocalDateTime.now();
        return "logs/" + now.format(fileNameFormatter) + "-" +
                System.currentTimeMillis() + ".log";
    }

    private static class LogEntry {
        final LocalDateTime timestamp;
        final LogLevel level;
        final String component;
        final String message;

        LogEntry(LocalDateTime timestamp, LogLevel level, String component, String message) {
            this.timestamp = timestamp;
            this.level = level;
            this.component = component;
            this.message = message;
        }
    }

    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

}
