package com.wth.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wth.api.mapper.UserInterfaceInfoMapper;
import com.wth.api.model.entity.UserInterfaceInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
* @author 79499
* @description 针对表【user_interface_info(用户调用接口信息表)】的数据库操作Service
* @createDate 2023-10-06 21:36:53
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {


    boolean invokeCount(Long interfaceId, Long userId);

}
