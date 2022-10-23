create table user_file
(
    id             INTEGER primary key autoincrement,
    source         varchar(100) not null,
    user_id        varchar(200) not null,
    desc           varchar(200),
    online         varchar(100) not null,
    original_name  varchar(100) not null,
    size           INTEGER      not null,
    open_share     INTEGER default 0 not null,
    available_time DATETIME,
    create_time    DATETIME,
    update_time    DATETIME
);

create table share_file
(
    id          INTEGER   primary key autoincrement,
    code        char(50)     not null,
    source      varchar(100) not null,
    online      varchar(100) not null,
    size        INTEGER      not null,
    create_time DATETIME,
    data        TEXT
);


create table short_link(
       id            INTEGER primary key autoincrement,
       lurl          char(160) not null ,
       surl          char (30) not  null ,
       click_times   INTEGER   default 0,
       create_time   DATETIME
)


create table proxy_ip(
     id  INTEGER primary key autoincrement,
     ip  char (15) not null ,
     port INTEGER not null ,
     life INTEGER not null ,
     type INTEGER default  0, --0.未知 1.普匿 2.透明 3.高匿
     source char(100) not null ,-- 来源平台
     position char(100)  ,      ---地理位置
     protocol INTEGER default 1,   ---协议 （1.http\2.https）
     check_times INTEGER  default 0,
     creat_time DATETIME,
     check_time DATETIME
)
