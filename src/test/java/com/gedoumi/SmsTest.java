package com.gedoumi;

import com.gedoumi.util.CipherUtils;
import com.gedoumi.util.NumberUtil;
import com.gedoumi.util.SmsUtil;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by md on 11/2/17.
 */

public class SmsTest {

    static Logger logger = LoggerFactory.getLogger(SmsTest.class);



    @Test
    public void testSend(){
        String mobile = "18620885643";
        String content = "验证码：371469(3分钟内有效)，请勿告知他人。用于您本次登录拿云体育网上服务平台、拿云APP，避免因使用其他应用造成信息泄露。【北京拿云智成科技有限责任公司】";
        try {
            SmsUtil.send(mobile,content);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testS(){
        for (int i=0;i<100;i++) {
            int seed = NumberUtil.randomInt(9900,10050);
            System.out.println(seed);
        }

    }

    @Test
    public void testTT(){
        String salt = new Md5Hash("13059149139").toString();
        String pswd = new Md5Hash("111111", salt).toString();
        System.out.println(pswd);

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        int hour_of_day = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour_of_day);

        cal.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        hour_of_day = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour_of_day);



    }


}
