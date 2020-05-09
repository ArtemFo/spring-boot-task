drop sequence if exists hibernate_sequence;
drop table if exists user_role;
drop table if exists usr;

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

insert into usr (active, email, ip, name, password, timezone, id)
values (true, 'some.email@gmail.com', '10.10.10', 'UserTest', 'password1', 'usa', nextval('hibernate_sequence'));
insert into user_role (user_id, roles)
values (currval('hibernate_sequence'), 'USER');
insert into usr (active, email, ip, name, password, timezone, id)
values (true, 'email@gmail.com', '20.20.20', 'AdminTest', 'password2', 'uk', nextval('hibernate_sequence'));
insert into user_role (user_id, roles)
values (currval('hibernate_sequence'), 'ADMIN'),
       (currval('hibernate_sequence'), 'USER');
insert into usr (active, email, ip, name, password, timezone, id)
values (true, 'qwert@qwert.com', '30.30.30', 'qwert', 'qwertqwert', 'est', nextval('hibernate_sequence'));
insert into user_role (user_id, roles)
values (currval('hibernate_sequence'), 'USER');

create extension if not exists pgcrypto;
update usr
set password = crypt(password, gen_salt('bf', 8));