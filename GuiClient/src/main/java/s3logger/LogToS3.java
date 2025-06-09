package s3logger;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@InterceptorBinding
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
public @interface LogToS3 {

}
