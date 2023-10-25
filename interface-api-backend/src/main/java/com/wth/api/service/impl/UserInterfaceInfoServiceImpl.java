package com.wth.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wth.api.common.ErrorCode;
import com.wth.api.exception.BusinessException;
import com.wth.api.mapper.UserInterfaceInfoMapper;
import com.wth.api.service.UserInterfaceInfoService;
import com.wth.common.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 79499
* @description 针对表【user_interface_info(用户调用接口信息表)】的数据库操作Service实现
* @createDate 2023-10-06 21:36:53
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Override
    public boolean invokeCount(Long interfaceId, Long userId) {
        if (interfaceId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        userInterfaceInfoMapper.invokeCount(interfaceId, userId);
        return true;
    }
}






