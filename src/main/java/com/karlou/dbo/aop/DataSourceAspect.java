package com.karlou.dbo.aop;

import com.karlou.dbo.annotation.DataSource;
import com.karlou.dbo.route.DynamicDataSourceRoute;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {
    @Pointcut("@annotation(com.karlou.dbo.annotation.DataSource)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource == null) {
            DynamicDataSourceRoute.setDataSource("infocarriers");
        } else {
            DynamicDataSourceRoute.setDataSource(dataSource.name());
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSourceRoute.clearDataSource();
        }
    }
}
