package com.paul.usercenter.controller;
//用于编写restful风格的api，返回值默认为json类型

/*
  控制层封装请求，便于前端调用
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.paul.usercenter.common.BaseResponse;
import com.paul.usercenter.common.ErrorCode;
import com.paul.usercenter.common.ResultUtils;
import com.paul.usercenter.exception.BusinessException;
import com.paul.usercenter.model.domain.User;
import com.paul.usercenter.model.domain.request.UserLoginRequest;
import com.paul.usercenter.model.domain.request.UserRegisterRequest;
import com.paul.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.paul.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.paul.usercenter.contant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 36000)
@CrossOrigin(origins = {"http://localhost:5173/"})
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long>userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        return new BaseResponse<>(0,result,"ok");
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User>userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String checkPassword = userLoginRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
//        return new BaseResponse<>(0,user,"ok");
        return ResultUtils.success(user);
    }


    @PostMapping("/current")  //获取当前用户登录信息
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        long userid = currentUser.getId();
        User user = userService.getById(userid);
//        return userService.getSafetyUser(user);
        return ResultUtils.success(user);
    }



    @PostMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {

        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
            //非管理员返回空列表
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) { //不为空，空格，
            queryWrapper.like("username", userName); //允许包含输入条件 模糊查询
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    List<User> userList = userService.searchUsersByTags(tagNameList);
    return ResultUtils.success(userList);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> userUpdate( User user , HttpServletRequest request) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        int result = userService.updateUser(user,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (isAdmin(request)) {
            return null;
        }
        if (isAdmin(request)) {
            return null;
        }
        if (id <= 0) {

            return null;
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(@RequestBody HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 判断是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        //判断是否为管理员，这样在delete和search中不需要重复书写
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user == null || user.getUserRole() != ADMIN_ROLE;
    }
}
