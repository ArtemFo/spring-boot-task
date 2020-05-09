create sequence hibernate_sequence start 1 increment 1;

create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);

create table usr
(
    id       int8         not null,
    active   boolean      not null,
    email    varchar(50)  not null,
    ip       varchar(255),
    name     varchar(30)  not null,
    password varchar(200) not null,
    timezone varchar(255),
    primary key (id)
);

alter table if exists usr
    add constraint usr_email_unique unique (email);
alter table if exists usr
    add constraint usr_name_unique unique (name);
alter table if exists user_role
    add constraint user_role_role_fk foreign key (user_id) references usr;