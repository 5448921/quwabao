package com.gedoumi;

import com.alibaba.fastjson.JSON;
import com.gedoumi.common.enums.SmsStatus;
import com.gedoumi.common.enums.SmsType;
import com.gedoumi.sys.entity.SysSms;
import com.gedoumi.sys.service.SysSmsService;
import com.gedoumi.util.SmsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml","classpath*:applicationContext-shiro.xml" })
public class SysSmsTest {

    Logger logger = LoggerFactory.getLogger(SysSmsTest.class);


    @Resource
    private SysSmsService smsService;

    @Test
    public void testAdd(){
        SysSms sms = new SysSms();
        sms.setCode(SmsUtil.generateCode());
        Date now = new Date();
        sms.setCreateTime(now);
        String phone = "18620885643";
        String phoneNumber = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        sms.setMobilePhone(phone);
        logger.info("phoneNumber={}",phoneNumber);
        sms.setSmsStatus(SmsStatus.Enable.getValue());
        sms.setSmsType(SmsType.Login.getValue());
        sms.setUpdateTime(now);
        smsService.add(sms);
        logger.info("sms = {}", JSON.toJSON(sms));
    }

    @Test
    public void testFind(){
        SysSms query = new SysSms();
        query.setMobilePhone("18620885643");
        query.setSmsType(SmsType.Login.getValue());
        query.setSmsStatus(SmsStatus.Enable.getValue());
        query.setCode("414254");
        List<SysSms> data = smsService.findByQuery(query);
        logger.info("size = {}", data.size());
        logger.info("data = {}", JSON.toJSON(data));
    }


    @Test
    public void testDayCount(){

        int count = smsService.getCurrentDayCount("18620885643");
        logger.info("count = {}", count);
    }





}
