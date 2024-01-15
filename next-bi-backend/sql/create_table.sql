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


CREATE TABLE chart (
   id BIGINT(20) NOT NULL COMMENT 'id',
   name VARCHAR(255) COMMENT '名称',
   goal VARCHAR(255) COMMENT '分析目标',
   chart_data TEXT COMMENT '图表数据',
   chart_type VARCHAR(255) COMMENT '图表类型',
   gen_chart TEXT COMMENT '生成的图表数据',
   gen_result TEXT COMMENT '生成的分析结论',
   user_id BIGINT(20) COMMENT '创建用户 id',
   create_time DATETIME COMMENT '创建时间',
   update_time DATETIME COMMENT '更新时间',
   is_delete INT(11) COMMENT '是否删除',
   PRIMARY KEY (id)
) COMMENT='图表表';

INSERT INTO next_bi_db.user (id, user_account, user_password, union_id, mpOpen_id, user_name, user_avatar, user_profile, user_role, create_time, update_time, is_delete) VALUES (1740718129912344578, 'nexura', 'b0dd3697a192885d7c055db46155b26a', null, null, 'nexura官方', '//p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/9eeb1800d9b78349b24682c3518ac4a3.png~tplv-uwbnlip3yd-webp.webp', null, 'admin', '2023-12-29 12:55:17', '2024-01-10 13:55:07', 0);
