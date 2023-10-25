package com.wth.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wth.common.model.entity.UserInterfaceInfo;

/**
* @author 79499
* @description 针对表【user_interface_info(用户调用接口信息表)】的数据库操作Mapper
* @createDate 2023-10-06 21:39:33
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    Boolean invokeCount(Long interfaceId, Long userId);
}




