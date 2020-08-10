
drop table if exists data_type;
create table data_type
(
    id           integer primary key autoincrement,
    db_type    text  null,
    java_type text  null
);

insert into data_type(db_type, java_type)
values ('tinyint', 'boolean'),
       ('smallint', 'Integer'),
       ('mediumint', 'Integer'),
       ('int', 'Integer'),
       ('integer', 'Integer'),
       ('bigint', 'Long'),
       ('float', 'Float'),
       ('double', 'Double'),
       ('decimal', 'BigDecimal'),
       ('bit', 'Boolean'),
       ('char', 'String'),
       ('varchar', 'String'),
       ('tinytext', 'String'),
       ('text', 'String'),
       ('mediumtext', 'String'),
       ('longtext', 'String'),
       ('date', 'Date'),
       ('datetime', 'Date'),
       ('timestamp', 'Date'),
       ('NUMBER', 'Integer'),
       ('INT', 'Integer'),
       ('INTEGER', 'Integer'),
       ('BINARY_INTEGER', 'Integer'),
       ('LONG', 'String'),
       ('FLOAT', 'Float'),
       ('BINARY_FLOAT', 'Float'),
       ('DOUBLE', 'Double'),
       ('BINARY_DOUBLE', 'Double'),
       ('DECIMAL', 'BigDecimal'),
       ('CHAR', 'String'),
       ('VARCHAR', 'String'),
       ('VARCHAR2', 'String'),
       ('NVARCHAR', 'String'),
       ('NVARCHAR2', 'String'),
       ('CLOB', 'String'),
       ('BLOB', 'String'),
       ('DATE', 'Date'),
       ('DATETIME', 'Date'),
       ('TIMESTAMP', 'Date'),
       ('TIMESTAMP(6)', 'Date'),
       ('int8', 'Long'),
       ('int4', 'Integer'),
       ('int2', 'Integer'),
       ('numeric', 'BigDecimal'),
       ('nvarchar', 'String'),
       ('nvarchar', 'String');


drop table if exists project_info;
create table project_info
(
    id           integer primary key autoincrement,
    main_path    text not null,
    package_name text not null,
    module_name  text not null,
    author       text not null,
    email        text not null,
    table_prefix text not null
);
insert into project_info(main_path, package_name, module_name, author, email, table_prefix)
VALUES ('com.example', 'ipps', 'log', 'Mr.Wang', 'wry10150@163.com', 'sys_');

select * from project_info;
select * from data_type;
select id, main_path, package_name, module_name, author, email, table_prefix
from project_info;

