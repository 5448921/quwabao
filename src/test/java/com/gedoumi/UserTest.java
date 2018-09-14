package com.gedoumi;

import com.gedoumi.common.enums.UserStatus;
import com.gedoumi.common.enums.UserType;
import com.gedoumi.common.exception.BusinessException;
import com.gedoumi.common.utils.Base64Img;
import com.gedoumi.user.dao.UserDao;
import com.gedoumi.user.entity.User;
import com.gedoumi.user.entity.UserImage;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml","classpath*:applicationContext-shiro.xml" })
public class UserTest {

    Logger logger = LoggerFactory.getLogger(UserTest.class);

    @Resource
    private UserDao userDao;

    @Resource
    private UserService userService;

    @Resource
    private UserTreeService userTreeService;

    @Test
    public void testFind(){
        User user = userDao.findByToken("6113f047-7d84-4599-a6d8-2ee28f3b4439");
        logger.info("sysuser = {}", user);
        User user2 = userDao.findByMobilePhone("18620885643");
        logger.info("sysuser2 = {}", user2);
        logger.info("uuid={}", UUID.randomUUID());
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setMobilePhone("13688004659");
//        user.setUsername(User.PREFIX);
        String salt = new Md5Hash(user.getMobilePhone()).toString();
        user.setPassword(new Md5Hash(User.PWD_INIT, salt).toString());
        user.setUserStatus(UserStatus.Enable.getValue());
        Date now = new Date();
        user.setUpdateTime(now);
        user.setRegisterTime(now);
        user.setLastLoginTime(now);
        user.setUserType(UserType.Level_0.getValue());
        try {
            userService.addUser(user);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAdd2(){
        String m = "13600001000";
        User parent = userService.getByMobilePhone("13718156454");
        for (int i=0;i<10;i++){
            String mobile = Long.parseLong(m)+i + "";
            User user = new User();
            user.setMobilePhone(mobile);
            String salt = new Md5Hash(user.getMobilePhone()).toString();
            user.setPassword(new Md5Hash(User.PWD_INIT, salt).toString());
            user.setUserStatus(UserStatus.Enable.getValue());
            Date now = new Date();
            user.setUpdateTime(now);
            user.setRegisterTime(now);
            user.setLastLoginTime(now);
            user.setUserType(UserType.Level_0.getValue());
            user.setRegInviteCode("50d9b598");
            try {
                userService.addUser(user);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
//        user.setUsername(User.PREFIX);

    }

    @Test
    public void testUpdate(){
        String m = "13688004650";
        for (int i=0;i<10;i++){
            String mobile = Long.parseLong(m)+i + "";
            User user = userService.getByMobilePhone(mobile);
            String salt = new Md5Hash(user.getMobilePhone()).toString();
            user.setPassword(new Md5Hash(User.PWD_INIT, salt).toString());
            userService.update(user);
        }
//        user.setUsername(User.PREFIX);

    }

    @Test
    public void testAddFindTree(){
        UserTree userTree = new UserTree();
        User parent = userService.getById(2L);
        User child = userService.getById(4L);
        userTree.setParent(parent);
        userTree.setChild(child);
        userTreeService.add(userTree);
    }

    @Test
    public void testFindTree(){
        User child = userService.getById(4L);
        UserTree userTree = userTreeService.findByChild(child);
        logger.info("{}", Json.toJson(userTree));

        User parent = userService.getById(1L);
        List<UserTree> userTrees = userTreeService.findByParent(parent);
        logger.info("{}", Json.toJson(userTrees));

    }

    @Test
    public void testGetUserImg(){

        UserImage userImage = userService.getUserImg(100L);
        Base64Img.generateImage(userImage.getUserImage(),"file:///C:\\Users\\sai_m\\Pictures\\222.jpg");
    }


}
