package cloudwatchservice;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.io.Serializable;

@Interceptor
@MonitorPerformance
public class CloudWatchInterceptor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private transient CloudWatchMonitor monitor;  // transient since it's likely not serializable

    @AroundInvoke
    public Object logMethodExecution(InvocationContext context) throws Exception {
        long startTime = System.currentTimeMillis();
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();

        try {
            Object result = context.proceed();

            if (monitor != null) {
                long executionTime = System.currentTimeMillis() - startTime;
                monitor.recordMethodExecutionTime(className, methodName, executionTime);
            }

            return result;
        } catch (Exception e) {
            if (monitor != null) {
                long executionTime = System.currentTimeMillis() - startTime;
                monitor.recordMethodExecutionTime(className, methodName, executionTime);
            }
            throw e;
        }
    }
}