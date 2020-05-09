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

