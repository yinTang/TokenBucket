package cn.com.bucket;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yintang
 */
@Aspect
@Component
public class ControllerAspectj {

    @Around("execution(public * cn.com.bucket.controller.TestController.*(..))")
    public Object round(ProceedingJoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            //获得获取令牌
            Shaping.tryAcquire(request.getRequestURI());
           return jp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return throwable.getMessage();
        }
    }
}

