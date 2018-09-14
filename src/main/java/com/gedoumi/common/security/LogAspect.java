package com.gedoumi.common.security;

import com.alibaba.fastjson.JSON;
import com.gedoumi.api.gateway.ApiResponse;
import com.gedoumi.api.gateway.vo.RechargeVO;
import com.gedoumi.common.enums.LogType;
import com.gedoumi.common.enums.SysLogStatus;
import com.gedoumi.sys.entity.SysLog;
import com.gedoumi.sys.service.SysLogService;
import com.gedoumi.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    static Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Resource
    private SysLogService logService;

    /**
     * 使用Pointcut定义切点
     */
    @Pointcut("@annotation(com.gedoumi.common.annotation.PfcLogAspect)")
    private void myPointcut(){}

//    @Before(value = "myPointcut()")
    public void before(JoinPoint joinPoint){
        logger.info(" in before ");
        Object[] args = joinPoint.getArgs();
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(SessionUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.Recharge.getValue());
        sysLog.setRequestUrl(SessionUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(JSON.toJSONString(args));
        if(args[0] instanceof RechargeVO){
            RechargeVO rechargeVO = (RechargeVO)args[0];
            sysLog.setMobile(rechargeVO.getPfc_account());
            sysLog.setSeq(rechargeVO.getSeq());
        }

        sysLog.setLogStatus(SysLogStatus.Init.getValue());
        logService.add(sysLog);
    }

    @AfterReturning(value = "myPointcut()", returning = "apiResponse")
    public void afterRunning(JoinPoint joinPoint, ApiResponse apiResponse){
        logger.info(" in afterRunning apiResponse = {}", JSON.toJSON(apiResponse));
        Object[] args = joinPoint.getArgs();
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(SessionUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.Recharge.getValue());
        sysLog.setRequestUrl(SessionUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(JSON.toJSONString(args));
        if(args[0] instanceof RechargeVO){
            RechargeVO rechargeVO = (RechargeVO)args[0];
            sysLog.setMobile(rechargeVO.getPfc_account());
            sysLog.setSeq(rechargeVO.getSeq());
        }
        if(apiResponse.getCode() == 0){
            sysLog.setLogStatus(SysLogStatus.Success.getValue());
        }else {
            sysLog.setLogStatus(SysLogStatus.Fail.getValue());
        }

        logService.add(sysLog);
    }
}
