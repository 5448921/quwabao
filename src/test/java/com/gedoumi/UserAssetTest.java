package com.gedoumi;

import com.gedoumi.asset.entity.UserAssetDetail;
import com.gedoumi.asset.entity.UserRent;
import com.gedoumi.asset.service.UserAssetService;
import com.gedoumi.common.enums.RentType;
import com.gedoumi.common.enums.TransType;
import com.gedoumi.common.exception.BusinessException;
import com.gedoumi.user.entity.User;
import com.gedoumi.user.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml","classpath*:applicationContext-shiro.xml"})
//        ,"file:src/main/webapp/WEB-INF/pfcmvc-servlet.xml" })
public class UserAssetTest {

    Logger logger = LoggerFactory.getLogger(UserAssetTest.class);

    @Autowired
    private UserAssetService assetService;

    @Autowired
    private UserService userService;



    @Test
    public void testAdd(){
        UserAssetDetail detail = new UserAssetDetail();
        User user = userService.getByMobilePhone("13688004660");
        detail.setUser(user);
        detail.setMoney(new BigDecimal("500000"));
        detail.setProfit(new BigDecimal("0"));
        detail.setProfitExt(new BigDecimal(0));
        detail.setTransType(TransType.NetIn.getValue());
        assetService.addAsset(detail);
    }

    @Test
    public void testBatchAdd(){
        String m = "13688004661";
        for (int i=0;i<10;i++){
            String mobile = Long.parseLong(m)+i + "";
            User user = userService.getByMobilePhone(mobile);
            UserAssetDetail detail = new UserAssetDetail();
            detail.setUser(user);
            detail.setMoney(new BigDecimal("500000"));
            detail.setProfit(new BigDecimal("0"));
            detail.setProfitExt(new BigDecimal(0));
            detail.setTransType(TransType.NetIn.getValue());
            assetService.addAsset(detail);
        }
    }

    @Test
    public void testRent(){
        AtomicInteger atomicInteger = new AtomicInteger();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3,
                6,
                5,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        CountDownLatch latch = new CountDownLatch(2);
        for (int i=0;i<2;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Date now = new Date();
                    UserRent userRent = new UserRent();
                    userRent.setCreateTime(now);
                    userRent.setUpdateTime(now);
                    userRent.setDays(RentType.Rent1.getDays());
                    userRent.setRentAsset(RentType.Rent1.getMoney());
                    userRent.setRentType(RentType.Rent1.getValue());
                    User user = userService.getByMobilePhone("18620880001");
                    userRent.setUser(user);
                    Date endDate = DateUtils.addDays(now,userRent.getDays());
                    userRent.setExpireDate(endDate);
                    userRent.setDigNumber(180);
                    try {
                        assetService.addRent(userRent);
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetTeamRentAsset(){
        User user = userService.getByMobilePhone("18620885643");
        BigDecimal totalAsset = assetService.getTeamRentAsset(user,true).getTotalAsset();
        int totalCount = assetService.getTeamRentAsset(user,true).getTotalCount();
        logger.info("totalAsset = {}, totalCount={}",totalAsset, totalCount);
    }




}
