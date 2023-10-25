package com.wth.api.model.dto.interfaceinfo;

import lombok.Data;

/**
 *  添加接口请求类
 */
@Data
public class InterfaceInfoUpdateRequest {

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
     * 请求参数
     */
    private String requestParams;

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


    // 除非是管理员可以修改
//    /**
//     * 创建人id
//     */
//    private Long createId;

}
