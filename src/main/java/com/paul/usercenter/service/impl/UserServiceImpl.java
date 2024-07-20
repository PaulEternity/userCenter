package com.paul.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paul.usercenter.common.ErrorCode;
import com.paul.usercenter.exception.BusinessException;
import com.paul.usercenter.service.UserService;
import com.paul.usercenter.model.domain.User;
import com.paul.usercenter.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 30420
 * @author Paul
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-04-21 19:22:23
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "paul";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户过短");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (planetCode.length() > 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编号过长");
        }

        //校验账户包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount); //本段用到了查询数据库，该校验往后放，
        long count = userMapper.selectCount(queryWrapper); // 如果前面的判断出现错误可以省去一次调用数据库
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }

        //编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode); //本段用到了查询数据库，该校验往后放，
        count = userMapper.selectCount(queryWrapper); // 如果前面的判断出现错误可以省去一次调用数据库
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编号重复");
        }

        //加密
        final String SALT = "Paul";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //向用户数据库插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数据保存错误");
        }
        return user.getId();
    }


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 6) {
            return null;
        }

        //校验账户包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        //加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return null;
        }


        return user;
    }

    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }


    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 6) {
            return null;
        }

        //校验账户包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //检测用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }

        //用户脱敏 隐藏敏感信息，防止数据库中的字段泄露
        User safetyUser = getSafetyUser(user);


        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return safetyUser;

        /**
         * 连接到服务器后，得到一个session(1)状态（匿名会话），返回给前端
         * 当用户登录成功后，得到登录成功的session(2)并给session设置值（用户信息等）返回给前端一个
         * 设置cookie的命令，前端接收到命令行，设置cookie，保存到浏览器内
         * 前端再次请求后端时，必须是相同的域名，在请求头中带上cookie
         * 后端拿到前端传来的cookie，找到对应session
         * 后端从session中可以取出基于session存储的变量（登录信息、登录名等）
         */

    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 0;
    }


//    @Override
//    public User getSafetyUser(User originUser) {
//        User user = new User();
//        user.setId(originUser.getId());
//        user.setUserAccount(originUser.getUserAccount());
//        user.setUserName(originUser.getUserName());
//        user.setAvatarUrl(originUser.getAvatarUrl());
//        user.setGender(originUser.getGender());
//        user.setPassword(originUser.getPassword());
//        user.setPhone(originUser.getPhone());
//        user.setEmail(originUser.getEmail());
//        user.setUserStatus(originUser.getUserStatus());
//        user.setCreatTime(originUser.getCreatTime());
//        user.setUpdateTime(originUser.getUpdateTime());
//        user.setIsDelete(originUser.getIsDelete());
//        user.setUserRole(originUser.getUserRole());
//        return user;
//
//    }


}




