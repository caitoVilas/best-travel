package com.bestTravel.aspects;

import com.bestTravel.util.BestTravelUtil;
import com.bestTravel.util.annotations.Notify;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Component
@Aspect
public class NotifyAspect {
    private static final String LINE_FORMAT = "At %s new request with size page %s and order %s";

    @After(value = "@annotation(com.bestTravel.util.annotations.Notify)")
    public void notifyInFile(JoinPoint joinPoint) throws IOException {
        var args = joinPoint.getArgs();
        var size = args[1];
        var order = args[2] == null? "NONE": args[2];
        var text = String.format(LINE_FORMAT, LocalDateTime.now(), size.toString(),
                order.toString());
        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();
        var annotation = method.getAnnotation(Notify.class);
        BestTravelUtil.writeNotification(text, annotation.value());
        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
    }
}
