-- 用户服务数据库
create database if not exists spring_cloud_blog_user charset utf8mb4;

use spring_cloud_blog_user;
-- 用户表
drop table if exists spring_cloud_blog_user.user_info;
create table spring_cloud_blog_user.user_info
(
    `id`          int          not null auto_increment,
    `user_name`   varchar(128) not null,
    `password`    varchar(128) not null,
    `github_url`  varchar(128) null,
    `email`       varchar(128) null,
    `delete_flag` tinyint(4)   null default 0,
    `create_time` datetime default now(),
    `update_time` datetime default now() on update now(),
    primary key (id),
    unique index user_name_unique (user_name asc)
) engine = innodb default character set = utf8mb4 comment = '用户表';

-- 新增用户信息
insert into spring_cloud_blog_user.user_info (user_name, password, github_url) values ("稚名不带撇", "d5965f63e48140cf9f8128f6ba24c82ee10adc3949ba59abbe56e057f20f883e", "https://github.com/zmbdp");
insert into spring_cloud_blog_user.user_info (user_name, password, github_url) values ("zhangsan","11eb2423c6064044aaa1df7ed6156331d1244f113ad54a982cb0215e0af22b68","https://gitee.com/zmbdp");
insert into spring_cloud_blog_user.user_info (user_name, password, github_url) values ("lisi","25ce8e32469c4bdb9868c182b6164d3006de2cb47383384df8279e7cfc251d9d","https://gitee.com/zmbdp");


-- 博客服务数据库
create database if not exists spring_cloud_blog_blog charset utf8mb4;

use spring_cloud_blog_blog;

-- 博客表
drop table if exists spring_cloud_blog_blog.blog_info;
create table spring_cloud_blog_blog.blog_info
(
    `id`          int not null auto_increment,
    `title`       varchar(200) null,
    `content`     text         null,
    `user_id`     int(11)      null,
    `delete_flag` tinyint(4)   null default 0,
    `create_time` datetime default now(),
    `update_time` datetime default now() on update now(),
    primary key (id)
) engine = innodb default charset = utf8mb4 comment = '博客表';

insert into spring_cloud_blog_blog.blog_info (title, content, user_id) values ("第一篇博客", "111我是博客正文我是博客正文我是博客正文", 1);
insert into spring_cloud_blog_blog.blog_info (title, content, user_id) values ("第二篇博客", "222我是博客正文我是博客正文我是博客正文", 2);
insert into spring_cloud_blog_blog.blog_info (title, content, user_id) values ("第三篇博客", "333我是博客正文我是博客正文我是博客正文", 3);