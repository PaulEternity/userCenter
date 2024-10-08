package com.paul.usercenter.service;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.paul.usercenter.model.domain.User;
import org.junit.Assert;
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
        user.setUserAccount("111");
// rest of fields
//        user.setUserAccount("");
        user.setUserName("Paul");
        user.setAvatarUrl("https://www.indonesia.travel/content/dam/indtravelrevamp/en/destinations/bali-nusa-tenggara/west-nusa-tenggara/lombok/lombok1.jpg");
        user.setGender(0);
        user.setPassword("123456");
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
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "Pa";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "Paul";
        userPassword = "123456";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "Pa ul";
        userPassword = "123456";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userPassword ="123456";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "John123";
        checkPassword = "password123";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);
        userAccount = "Paul";
//        result = userService.userRegister(userAccount,userPassword,checkPassword);
//        Assertions.assertTrue(result > 0);

    }

    @Test
    public void testSearchUsersByTags(){
        List<String> tagNameList = Arrays.asList("java","python");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        Assert.assertNotNull(userList);
    }
}