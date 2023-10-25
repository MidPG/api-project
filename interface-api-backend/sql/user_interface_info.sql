-- 切换库
use my_db;

-- auto-generated definition
create table user_interface_info
(
    id           bigint auto_increment comment '主键'
        primary key,
    user_id      bigint                              not null comment '用户id',
    interface_id bigint                              not null comment '被调用的接口id',
    total_number int       default 0                 not null comment '接口总调用次数',
    left_number  int       default 0                 not null comment '剩余调用次数',
    status       int       default 0                 not null comment '调用状态（0 - 正常，1 - 禁用）',
    create_time  timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint   default 0                 not null comment '是否参数（0 - 未删除  1 - 删除）'
)
    comment '用户调用接口信息表';