package com.paul.usercenter.controller;
//用于编写restful风格的api，返回值默认为json类型

/*
  控制层封装请求，便于前端调用
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.paul.usercenter.model.domain.User;
import com.paul.usercenter.model.domain.request.UserLoginRequest;
import com.paul.usercenter.model.domain.request.UserRegisterRequest;
import com.paul.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.paul.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.paul.usercenter.contant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 36000)  //允许所有端口访问
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String checkPassword = userLoginRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }


//    @PostMapping("/current")
//    public User getCurrentUser(HttpServletRequest request) {
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//    }


    @PostMapping("/search")
    public List<User> searchUsers(String userName, HttpServletRequest request) {

        if (isAdmin(request)){
            return new ArrayList<>();
            //非管理员返回空列表
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) { //不为空，空格，
            queryWrapper.like("username", userName); //允许包含输入条件 模糊查询
        }
        List<User> userList =  userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (isAdmin(request)){
            return false;
        }
        if (isAdmin(request)) {
            return false;
        }
        if (id <= 0) {

            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 判断是否为管理员
     *
     */
    private boolean isAdmin(HttpServletRequest request) {
        //判断是否为管理员，这样在delete和search中不需要重复书写
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user == null || user.getUserRole() != ADMIN_ROLE;
    }
}
