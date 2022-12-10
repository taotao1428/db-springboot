package com.hewutao.db.aspect;

import com.hewutao.db.exception.BusinessException;
import com.hewutao.db.exception.InternalErrorException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ServiceExceptionAspect {

    @Pointcut("execution(* com.hewutao.db.service.impl.*ServiceImpl.*(*))")
    public void pointcut() {}

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public Object convertException(Throwable ex) throws Throwable {
        if (!(ex instanceof BusinessException)) {
            ex = new InternalErrorException(ex);
        }
        throw ex;
    }
}
