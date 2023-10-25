package com.wth.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.wth.api.annotation.AuthCheck;
import com.wth.api.common.BaseResponse;
import com.wth.api.common.ErrorCode;
import com.wth.api.common.IdRequest;
import com.wth.api.common.ResultUtils;
import com.wth.api.constant.UserConstant;
import com.wth.api.exception.BusinessException;
import com.wth.api.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.wth.api.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.wth.api.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.wth.api.model.enums.InterfaceInfoStatusEnum;
import com.wth.api.service.InterfaceInfoService;
import com.wth.api.service.UserInterfaceInfoService;
import com.wth.api.service.UserService;
import com.wth.common.model.entity.UserInterfaceInfo;
import com.wth.wthclientsdk.client.TApiClient;
import com.wth.wthclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;

/**
 *
 * @author yupi
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private UserService userService;
    // region 增删改查
    @Resource
    private TApiClient tApiClient;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest addRequest) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceId = addRequest.getInterfaceId();
        Long userId = addRequest.getUserId();
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interface_id", interfaceId).eq("user_id", userId);
        UserInterfaceInfo user = userInterfaceInfoService.getOne(queryWrapper);
        if (user != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已有信息，不可重复添加");
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(addRequest, userInterfaceInfo);
        boolean save = userInterfaceInfoService.save(userInterfaceInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据添加失败！");
        }
        return ResultUtils.success(save);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = updateRequest.getId();
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据不存在");
        }
        BeanUtils.copyProperties(updateRequest, oldUserInterfaceInfo);
        boolean update = userInterfaceInfoService.updateById(oldUserInterfaceInfo);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据更新失败");
        }
        return ResultUtils.success(update);
    }

    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(userInterfaceInfo);
    }

    @GetMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean delete = userInterfaceInfoService.removeById(id);
        if (!delete) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }



}
























