package cloudwatchservice;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import ejbs.Rental.Rental;
import enums.MailType;
import status.Status;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class CloudWatchMonitor {

    private static final String NAMESPACE = "CarRentalApplication";

    @Inject
    private AmazonCloudWatch cloudWatchClient;

    /**
     * Records email notification metrics
     */
    public void recordEmailSent(String recipient, MailType type, Long rentalId, boolean success) {
        try {
            List<Dimension> dimensions = new ArrayList<>();

            // Email type dimension
            dimensions.add(new Dimension()
                    .withName("EmailType")
                    .withValue(type.toString()));

            // Status dimension
            dimensions.add(new Dimension()
                    .withName("Status")
                    .withValue(success ? "Success" : "Failure"));

            // Create the metric
            MetricDatum datum = new MetricDatum()
                    .withMetricName("EmailNotifications")
                    .withUnit(StandardUnit.Count)
                    .withValue(1.0)
                    .withTimestamp(new Date())
                    .withDimensions(dimensions);

            // Send the metric to CloudWatch
            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace(NAMESPACE)
                    .withMetricData(datum);

            cloudWatchClient.putMetricData(request);

            System.out.println("CloudWatch metric recorded for email: " + type + " to " + recipient);
        } catch (Exception e) {
            System.err.println("Error recording CloudWatch metric: " + e.getMessage());
        }
    }

    /**
     * Records rental status change metrics
     */
    public void recordRentalStatusChange(Rental rental, Status newStatus) {
        try {
            List<Dimension> dimensions = new ArrayList<>();

            // Status dimension
            dimensions.add(new Dimension()
                    .withName("Status")
                    .withValue(newStatus.toString()));

            // Create the metric
            MetricDatum datum = new MetricDatum()
                    .withMetricName("RentalStatusChanges")
                    .withUnit(StandardUnit.Count)
                    .withValue(1.0)
                    .withTimestamp(new Date())
                    .withDimensions(dimensions);

            // Send the metric to CloudWatch
            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace(NAMESPACE)
                    .withMetricData(datum);

            cloudWatchClient.putMetricData(request);

            System.out.println("CloudWatch metric recorded for rental status change: " + newStatus);
        } catch (Exception e) {
            System.err.println("Error recording CloudWatch metric: " + e.getMessage());
        }
    }

    /**
     * Records method execution time
     */
    public void recordMethodExecutionTime(String className, String methodName, long executionTimeMs) {
        try {
            List<Dimension> dimensions = new ArrayList<>();

            // Class dimension
            dimensions.add(new Dimension()
                    .withName("Class")
                    .withValue(className));

            // Method dimension
            dimensions.add(new Dimension()
                    .withName("Method")
                    .withValue(methodName));

            // Create the metric
            MetricDatum datum = new MetricDatum()
                    .withMetricName("MethodExecutionTime")
                    .withUnit(StandardUnit.Milliseconds)
                    .withValue((double) executionTimeMs)
                    .withTimestamp(new Date())
                    .withDimensions(dimensions);

            // Send the metric to CloudWatch
            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace(NAMESPACE)
                    .withMetricData(datum);

            cloudWatchClient.putMetricData(request);
        } catch (Exception e) {
            System.err.println("Error recording CloudWatch metric: " + e.getMessage());
        }
    }

    /**
     * Records API usage count
     */
    public void recordApiUsage(String apiName) {
        try {
            List<Dimension> dimensions = new ArrayList<>();

            // API Name dimension
            dimensions.add(new Dimension()
                    .withName("ApiName")
                    .withValue(apiName));

            // Create the metric
            MetricDatum datum = new MetricDatum()
                    .withMetricName("ApiUsage")
                    .withUnit(StandardUnit.Count)
                    .withValue(1.0)
                    .withTimestamp(new Date())
                    .withDimensions(dimensions);

            // Send the metric to CloudWatch
            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace(NAMESPACE)
                    .withMetricData(datum);

            cloudWatchClient.putMetricData(request);
        } catch (Exception e) {
            System.err.println("Error recording CloudWatch metric: " + e.getMessage());
        }
    }
}