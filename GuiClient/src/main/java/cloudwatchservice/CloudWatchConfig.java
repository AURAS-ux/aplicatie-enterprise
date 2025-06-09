package cloudwatchservice;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class CloudWatchConfig {

    private AmazonCloudWatch cloudWatchClient;

    @PostConstruct
    public void init() {
        this.cloudWatchClient = AmazonCloudWatchClientBuilder.standard()
                .withRegion(Regions.US_EAST_1) 
                .build();
    }

    @Produces
    public AmazonCloudWatch getCloudWatchClient() {
        return cloudWatchClient;
    }
}