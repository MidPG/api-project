package com.wth.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wth.api.model.entity.InterfaceInfo;

/**
* @author 79499
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2023-09-26 13:48:21
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {


    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
