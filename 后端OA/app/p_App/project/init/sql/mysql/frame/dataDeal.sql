alter table zen_sys_user add dtype varchar(31) not Null;
alter table zen_sys_user add pwd varchar(1000) not Null;
update zen_sys_user set dtype='UserExtend',pwd='58vt3232u5zp7onf297a57a5a743894a0e4a801fc3wl7e21uzbr' where id='4028b88157d196e70157d19708150003';