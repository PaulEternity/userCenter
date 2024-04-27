package com.paul.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.paul.usercenter.service.UserService;
import com.paul.usercenter.model.domain.User;
import com.paul.usercenter.mapper.UserMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 * @author 30420
 * @author Paul
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-04-21 19:22:23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            return -1;
        }

        //校验账户包含特殊字符
        String regex = "^[a-zA-Z0-9_]+$";
        Pattern pattern = Pattern.compile(regex);

        boolean isMatch = pattern.matcher(userAccount).matches();
        if (isMatch) {
            System.out.println("账户名合法");
        } else {
            System.out.println("账户名包含特殊字符");
            return -1;
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount); //本段用到了查询数据库，该校验往后放，
        long count = userMapper.selectCount(queryWrapper);              // 如果前面的判断出现错误可以省去一次调用数据库
        if (count > 0) {
            return -1;
        }
        //加密
        final String SALT = "Paul";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //向用户数据库插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }


}




