prompt PL/SQL Developer import file
prompt Created on 2016年10月17日 by HB
set feedback off
set define off
prompt Disabling triggers for ZEN_SYS_USER...
alter table ZEN_SYS_USER disable all triggers;
prompt Disabling triggers for ZEN_SYS_ROLE...
alter table ZEN_SYS_ROLE disable all triggers;
prompt Disabling triggers for ZEN_SYS_ROLE_MENU...
alter table ZEN_SYS_ROLE_MENU disable all triggers;
prompt Disabling triggers for ZEN_SYS_USER_ROLE...
alter table ZEN_SYS_USER_ROLE disable all triggers;
prompt Disabling foreign key constraints for ZEN_SYS_USER...
alter table ZEN_SYS_USER disable constraint FK_6RP6AAL9QQH5GNIBLYRJHO017;
alter table ZEN_SYS_USER disable constraint FK_PO1OXMI0JNDQ2WUOEJEIS0XPM;
prompt Disabling foreign key constraints for ZEN_SYS_ROLE...
alter table ZEN_SYS_ROLE disable constraint FK_HG0QYRBAVWUDD6IA14JJPWWYR;
alter table ZEN_SYS_ROLE disable constraint FK_I2RJCY20KRCNG55G4W5GOPVLW;
prompt Disabling foreign key constraints for ZEN_SYS_ROLE_MENU...
alter table ZEN_SYS_ROLE_MENU disable constraint FK_L0D7RJJPWDYE7CEF5KRKLCDO1;
alter table ZEN_SYS_ROLE_MENU disable constraint FK_L94X1GTWH56UAFCOIU53HY84E;
prompt Disabling foreign key constraints for ZEN_SYS_USER_ROLE...
alter table ZEN_SYS_USER_ROLE disable constraint FK_NI3NRJ1X7QVGQXSENU207IPBC;
alter table ZEN_SYS_USER_ROLE disable constraint FK_QDAD208BOT9LI56R5OEW8SVPW;
prompt Deleting ZEN_SYS_USER_ROLE...
delete from ZEN_SYS_USER_ROLE;
commit;
prompt Deleting ZEN_SYS_ROLE_MENU...
delete from ZEN_SYS_ROLE_MENU;
commit;
prompt Deleting ZEN_SYS_ROLE...
delete from ZEN_SYS_ROLE;
commit;
prompt Deleting ZEN_SYS_USER...
delete from ZEN_SYS_USER;
commit;
prompt Loading ZEN_SYS_USER...
insert into ZEN_SYS_USER (ID, BAE003, BAE005, HAS_DEL, LOGIN_NAME, NAME, PASSWORD, BAE002, BAE004)
values ('4028b88157d1a22d0157d1a2fcd60003', to_timestamp('17-10-2016 07:55:02.230000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('17-10-2016 07:55:02.230000', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, 'admin', 'admin', 'px7nc2321qzsxr0f297a57a5a743894a0e4a801fc39x6221bzxz', null, null);
commit;
prompt 1 records loaded
prompt Loading ZEN_SYS_ROLE...
insert into ZEN_SYS_ROLE (ID, BAE003, BAE005, HAS_DEL, NAME, ORG_ID, TYPE, BAE002, BAE004)
values ('4028b88157d1a22d0157d1a2fc7c0000', to_timestamp('17-10-2016 07:55:02.080000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('17-10-2016 07:55:02.080000', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, 'admin', null, null, null, null);
commit;
prompt 1 records loaded
prompt Loading ZEN_SYS_ROLE_MENU...
insert into ZEN_SYS_ROLE_MENU (ID, BAE003, BAE005, HAS_DEL, MENU_ID, MENU_NAME, ROLE_ID, BAE002, BAE004)
values ('4028b88157d1a22d0157d1a2fcbe0001', to_timestamp('17-10-2016 07:55:02.206000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('17-10-2016 07:55:02.206000', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, '0501', '用户管理', '4028b88157d1a22d0157d1a2fc7c0000', null, null);
insert into ZEN_SYS_ROLE_MENU (ID, BAE003, BAE005, HAS_DEL, MENU_ID, MENU_NAME, ROLE_ID, BAE002, BAE004)
values ('4028b88157d1a22d0157d1a2fccb0002', to_timestamp('17-10-2016 07:55:02.218000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('17-10-2016 07:55:02.218000', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, '0502', '角色管理', '4028b88157d1a22d0157d1a2fc7c0000', null, null);
commit;
prompt 2 records loaded
prompt Loading ZEN_SYS_USER_ROLE...
insert into ZEN_SYS_USER_ROLE (ID, BAE003, BAE005, HAS_DEL, ROLE_ID, USER_ID, BAE002, BAE004)
values ('4028b88157d1a22d0157d1a2fcee0004', to_timestamp('17-10-2016 07:55:02.254000', 'dd-mm-yyyy hh24:mi:ss.ff'), to_timestamp('17-10-2016 07:55:02.254000', 'dd-mm-yyyy hh24:mi:ss.ff'), 0, '4028b88157d1a22d0157d1a2fc7c0000', '4028b88157d1a22d0157d1a2fcd60003', null, null);
commit;
prompt 1 records loaded
prompt Enabling foreign key constraints for ZEN_SYS_USER...
alter table ZEN_SYS_USER enable constraint FK_6RP6AAL9QQH5GNIBLYRJHO017;
alter table ZEN_SYS_USER enable constraint FK_PO1OXMI0JNDQ2WUOEJEIS0XPM;
prompt Enabling foreign key constraints for ZEN_SYS_ROLE...
alter table ZEN_SYS_ROLE enable constraint FK_HG0QYRBAVWUDD6IA14JJPWWYR;
alter table ZEN_SYS_ROLE enable constraint FK_I2RJCY20KRCNG55G4W5GOPVLW;
prompt Enabling foreign key constraints for ZEN_SYS_ROLE_MENU...
alter table ZEN_SYS_ROLE_MENU enable constraint FK_L0D7RJJPWDYE7CEF5KRKLCDO1;
alter table ZEN_SYS_ROLE_MENU enable constraint FK_L94X1GTWH56UAFCOIU53HY84E;
prompt Enabling foreign key constraints for ZEN_SYS_USER_ROLE...
alter table ZEN_SYS_USER_ROLE enable constraint FK_NI3NRJ1X7QVGQXSENU207IPBC;
alter table ZEN_SYS_USER_ROLE enable constraint FK_QDAD208BOT9LI56R5OEW8SVPW;
prompt Enabling triggers for ZEN_SYS_USER...
alter table ZEN_SYS_USER enable all triggers;
prompt Enabling triggers for ZEN_SYS_ROLE...
alter table ZEN_SYS_ROLE enable all triggers;
prompt Enabling triggers for ZEN_SYS_ROLE_MENU...
alter table ZEN_SYS_ROLE_MENU enable all triggers;
prompt Enabling triggers for ZEN_SYS_USER_ROLE...
alter table ZEN_SYS_USER_ROLE enable all triggers;
set feedback on
set define on
prompt Done.
