package com.paul.usercenter.service;
import java.util.Date;

import com.paul.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 *
 * @author Paul
 */


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void  testAddUse(){
        User user = new User();
//        user.setId(0L);
         user.setId(0L);
        user.setUserAccount("");
// rest of fields
        user.setUserAccount("");
        user.setUserName("Paul");
        user.setAvatarUrl("https://www.indonesia.travel/content/dam/indtravelrevamp/en/destinations/bali-nusa-tenggara/west-nusa-tenggara/lombok/lombok1.jpg");
        user.setGender(0);
        user.setPassword("xxx");
        user.setPhone("123456");
        user.setEmail("556234");
        user.setUserStatus(0);
        user.setCreatTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);

        user.setGender(0);
        user.setUserName("Paul");
        boolean result;
        result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);

    }

    @Test
    void userRegister() {
        String userAccount = "Paul";
        String userPassword = "123456";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "Pa";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "Paul";
        userPassword = "123456";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "John123";
        checkPassword = "password123";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "Paul";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertTrue(result > 0);

    }
}