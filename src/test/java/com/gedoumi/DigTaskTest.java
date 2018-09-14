package com.gedoumi;

import com.gedoumi.common.enums.UserStatus;
import com.gedoumi.task.DigTask;
import com.gedoumi.user.dao.UserDao;
import com.gedoumi.user.entity.User;
import com.gedoumi.user.entity.UserTree;
import com.gedoumi.user.service.UserService;
import com.gedoumi.user.service.UserTreeService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml","classpath*:applicationContext-shiro.xml" })
public class DigTaskTest {

    Logger logger = LoggerFactory.getLogger(DigTaskTest.class);

    @Resource
    private DigTask digTask;

    @Test
    public void testDig(){
        digTask.runDig();
    }

    @Test
    public void testUnFrozen(){
        digTask.runUnFrozen();
    }

    @Test
    public void testUnFrozenReward(){
        digTask.runUnFrozenReward();
    }

    @Test
    public void testAllTask(){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 13);
        cal.set(Calendar.MONTH, 11);

        for (int i=0;i<30;i++) {
            //12.13
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date date = cal.getTime();
            logger.info("{}",date);
            digTask.setDate(date);
            digTask.runReward();
            digTask.setDate(date);
            digTask.runUnFrozen();
            digTask.setDate(date);
            digTask.runDig();
            digTask.setDate(date);
            digTask.runUnFrozenReward();
        }


    }

    @Test
    public void testAllTask2(){

        digTask.runReward();
        digTask.runUnFrozen();
        digTask.runDig();
        digTask.runUnFrozenReward();


    }



}
