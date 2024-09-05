package com.paul.usercenter.controller;
//用于编写restful风格的api，返回值默认为json类型

/*
  控制层封装请求，便于前端调用
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.paul.usercenter.common.BaseResponse;
import com.paul.usercenter.common.ErrorCode;
import com.paul.usercenter.common.ResultUtils;
import com.paul.usercenter.exception.BusinessException;
import com.paul.usercenter.model.domain.Team;
import com.paul.usercenter.model.dto.TeamQuery;
import com.paul.usercenter.service.TeamService;
import com.paul.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 组队接口
 */
@RestController
@Slf4j
@RequestMapping("/team")
//@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 36000)
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class TeamController {

    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        boolean save = teamService.save(team); //增删改查直接采用mybatisPlus中自带的简单方法进行操作
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入失败");
        }
        return ResultUtils.success(team.getId());
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败！");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<Team>> listTeam(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamQuery);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
//        team.setName(teamQuery.getName());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        List<Team> teamList = teamService.list(queryWrapper);
        return ResultUtils.success(teamList);

    }

    @GetMapping("list/page")
    public BaseResponse<Page<Team>> listTeamPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(team, teamQuery);
        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamList = teamService.page(page,queryWrapper);
        return ResultUtils.success(teamList);

    }


}
