-- 切换库
use my_db;

-- auto-generated definition
create table interface_info
(
    id             bigint auto_increment comment '主键id'
        primary key,
    interface_name varchar(255)                        not null comment '接口名称',
    description    varchar(255)                        null comment '接口描述',
    url            varchar(255)                        not null comment '接口地址',
    request_params text                                not null comment '请求参数',
    request_head   text                                null comment '请求头',
    response_head  text                                null comment '响应头',
    status         tinyint   default 0                 not null comment '接口状态(0 - 关闭，1 - 开启)',
    interface_type varchar(255)                        not null comment '请求类型',
    create_id      bigint                              not null comment '创建人id',
    create_time    timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted     tinyint   default 0                 not null comment '逻辑删除（0 - 未删除 - 1 删除）'
)
    comment '接口信息表';