package com.wth.api.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.wth.api.annotation.AuthCheck;
import com.wth.api.common.BaseResponse;
import com.wth.api.common.ErrorCode;
import com.wth.api.common.IdRequest;
import com.wth.api.common.ResultUtils;
import com.wth.api.exception.BusinessException;
import com.wth.api.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.wth.api.model.entity.InterfaceInfo;
import com.wth.api.model.enums.InterfaceInfoStatusEnum;
import com.wth.api.service.InterfaceInfoService;
import com.wth.api.service.UserService;
import com.wth.wthclientsdk.client.TApiClient;
import com.wth.wthclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;
    // region 增删改查
    @Resource
    private TApiClient tApiClient;


    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> online(@RequestBody IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = idRequest.getId();
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        com.wth.wthclientsdk.model.User user = new User();
        user.setUsername("test");
        String userNameByPost = tApiClient.getUserNameByPost(user);
        if (CharSequenceUtil.isBlank(userNameByPost)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        int value = InterfaceInfoStatusEnum.ONLINE.getValue();
        interfaceInfo.setStatus(value);
        boolean res = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(res);
    }


    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offline(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() < 0 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setCreateId(idRequest.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean res = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(res);
    }

    @PostMapping("/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest invokeRequest, HttpServletRequest request) {
        if (invokeRequest == null || invokeRequest.getId() <= 0 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String requestParams = invokeRequest.getRequestParams();
        Long id = invokeRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        // 调用接口之前需要判断接口是否存在以及存在情况下是否未下线
        if (oldInterfaceInfo == null || oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在或接口已经下线");
        }
        // 调用接口
        com.wth.api.model.entity.User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        TApiClient client = new TApiClient(accessKey, secretKey);
        Gson gson = new Gson();
        User user = gson.fromJson(requestParams, User.class);
        String result = client.getUserNameByPost(user);
        return ResultUtils.success(result);
    }

}
























