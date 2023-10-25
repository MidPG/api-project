package com.wth.api.model.dto.interfaceinfo;

import com.wth.api.common.PageRequest;

/**
 *  接口查询对象
 */
public class InterfaceInfoQueryRequest extends PageRequest {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 接口描述
     */
    private String description;

    /**
     *  接口名称
     */
    private String interfaceName;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHead;

    /**
     * 响应头
     */
    private String responseHead;

    /**
     * 接口状态(0 - 关闭，1 - 开启)
     */
    private Integer status;

    /**
     *  接口类型
     */
    private String method;

    /**
     * 创建人id
     */
    private Long createId;

}
