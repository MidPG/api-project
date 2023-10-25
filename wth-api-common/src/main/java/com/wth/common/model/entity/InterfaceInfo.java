package com.wth.common.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息表
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
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
    private String interfaceType;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除（0 - 未删除 - 1 删除）
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}