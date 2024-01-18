# 数据库初始化


-- 创建库
create database if not exists next_bi_db;

-- 切换库
use next_bi_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null comment '账号',
    user_password varchar(512)                           not null comment '密码',
    union_id      varchar(256)                           null comment '微信开放平台id',
    mpOpen_id     varchar(256)                           null comment '公众号openId',
    user_name     varchar(256)                           null comment '用户昵称',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_profile  varchar(512)                           null comment '用户简介',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (union_id)
) comment '用户' collate = utf8mb4_unicode_ci;


create table chart
(
    id           bigint auto_increment comment 'id' primary key,
    goal         text                                   null comment '分析目标',
    chart_data   text                                   null comment '图表数据',
    chart_type   varchar(128)                           null comment '图表类型',
    gen_chart    text                                   null comment '生成的图表数据',
    gen_result   text                                   null comment '生成的分析结论',
    user_id      bigint                                 null comment '创建用户 id',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint      default 0                 not null comment '是否删除',
    name         varchar(255)                           null comment '名称',
    status       varchar(128) default 'wait'            not null comment 'wait,running,succeed,failed',
    exec_message text                                   null comment '执行信息'
) comment '图表信息表' collate = utf8mb4_unicode_ci;


create table chart_1747554550780358657
(
    日期 varchar(64) null,
    人数 varchar(64) null
);
