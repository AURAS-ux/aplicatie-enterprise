package s3logger;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import s3logger.S3Logger.LogLevel;

import java.io.Serializable;

@Interceptor
@LogToS3
public class S3LoggingInterceptor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private transient S3Logger s3Logger;

    @AroundInvoke
    public Object logMethodCall(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        String component = className + "." + methodName;

        s3Logger.log(LogLevel.INFO, component,
                "Method started with parameters: " + formatParameters(context.getParameters()));

        try {
            Object result = context.proceed();

            s3Logger.log(LogLevel.INFO, component, "Method completed successfully");

            return result;
        } catch (Exception e) {
            s3Logger.log(LogLevel.ERROR, component,
                    "Method failed with exception: " + e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }
    }

    private String formatParameters(Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(formatParameter(parameters[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    private String formatParameter(Object param) {
        if (param == null) {
            return "null";
        }

        String stringValue = param.toString();
        if (stringValue.length() > 100) {
            return stringValue.substring(0, 97) + "...";
        }
        return stringValue;
    }
}
