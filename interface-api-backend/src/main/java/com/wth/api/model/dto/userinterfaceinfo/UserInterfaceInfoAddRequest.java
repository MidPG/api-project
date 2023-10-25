package com.wth.api.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 *  添加接口请求类
 */
@Data
public class UserInterfaceInfoAddRequest {


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

}
