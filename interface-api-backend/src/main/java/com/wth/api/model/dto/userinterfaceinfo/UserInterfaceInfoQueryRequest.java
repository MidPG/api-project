package com.wth.api.model.dto.userinterfaceinfo;

/**
 *  接口查询对象
 */
public class UserInterfaceInfoQueryRequest {

    /**
     *  主键id
     */
    private Long userId;

    /**
     * 被调用的接口id
     */
    private Long interfaceId;

    /**
     * 接口总调用次数
     */
    private Integer totalNumber;

    /**
     * 剩余调用次数
     */
    private Integer leftNumber;

    /**
     * 调用状态（0 - 正常，1 - 禁用）
     */
    private Integer status;

}
