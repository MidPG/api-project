package com.wth.api.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wth.api.common.ErrorCode;
import com.wth.api.exception.BusinessException;
import com.wth.api.mapper.InterfaceInfoMapper;
import com.wth.api.service.InterfaceInfoService;
import com.wth.common.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 79499
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2023-09-26 13:48:21
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String interfaceName = interfaceInfo.getInterfaceName();
        if (add) {
            if (StringUtils.isBlank(interfaceName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(interfaceName) && interfaceName.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名字过长");
        }

    }
}




