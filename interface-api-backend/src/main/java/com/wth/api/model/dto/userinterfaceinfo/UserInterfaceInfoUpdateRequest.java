package com.wth.api.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 *  添加接口请求类
 */
@Data
public class UserInterfaceInfoUpdateRequest {

    /**
     *  主键id
     */
    private Long id;

    /**
     * 用户id
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
