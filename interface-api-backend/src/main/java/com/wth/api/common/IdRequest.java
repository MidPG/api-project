package com.wth.api.common;

import lombok.Data;

import java.io.Serializable;

/**
 *  使用id 为参数的请求对象 和deleteRequest相似
 */
@Data
public class IdRequest implements Serializable {

    private Long id;

    private static final long serialVersionUID = 1L;

}
