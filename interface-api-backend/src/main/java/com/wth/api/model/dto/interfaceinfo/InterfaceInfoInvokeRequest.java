package com.wth.api.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 *  添加接口请求类
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 请求参数
     */
    private String requestParams;


    private static final long serialVersionUID = -4550183407076349150L;


}
