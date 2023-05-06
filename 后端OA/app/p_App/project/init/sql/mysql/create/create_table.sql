create table app_job (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, class_path varchar(200), cron_time varchar(50), name varchar(200), next_time varchar(50), status varchar(6), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_condition_sort (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, andor varchar(255), classifyjudgement varchar(255), classifypch varchar(255), classifyprice varchar(255), fieldname varchar(255), groupnum varchar(255), is_delete varchar(255), midandor varchar(255), bae002 varchar(255), bae004 varchar(255), rule_id varchar(255), primary key (id)) engine=InnoDB;
create table dna_external_name (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, externalname varchar(255), externaltype varchar(255), is_delete varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_external_type (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, code varchar(255), is_delete varchar(255), text varchar(255), type varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_extra_external_type (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, dicname varchar(255), displayname varchar(255), external_type_id varchar(255), is_delete varchar(255), labeltype varchar(255), name varchar(255), required bit, type varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_extra_external_type_value (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, external_id varchar(255), external_name varchar(255), extra_external_type_id varchar(255), is_delete varchar(255), value varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_extra_scan_type (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, dicname varchar(255), displayname varchar(255), is_delete varchar(255), name varchar(255), required integer, scan_type_id varchar(255), type varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_extra_scan_type_value (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, accoutid varchar(255), acoount_type integer, extra_jcid varchar(255), extra_jcname varchar(255), extra_pch varchar(255), is_delete varchar(255), json longtext, rule_id longtext, rule_name longtext, scan_type_id varchar(255), taskid varchar(255), ywid varchar(255), bae002 varchar(32), bae004 varchar(32), primary key (id)) engine=InnoDB;
create table extra_scan_type_vaule_map (dna_extra_scan_type_value_id varchar(255) not null, extra_scan_type_vaule varchar(255), extra_scan_type_key varchar(255) not null, primary key (dna_extra_scan_type_value_id, extra_scan_type_key)) engine=InnoDB;
create table dna_rule (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, classifyname varchar(100), is_delete varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_rule_dic (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, diccode varchar(255), is_delete varchar(255), bae002 varchar(255), bae004 varchar(255), rule_id varchar(255), primary key (id)) engine=InnoDB;
create table dna_scan_type (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, code varchar(255), connect_out varchar(255), is_delete varchar(255), text varchar(255), type varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_comm_search_account (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, account varchar(50), account_type integer, address varchar(50), end_time varchar(19), epv_username varchar(100), failure varchar(2000), log_path varchar(255), pas_key varchar(2000), platformid varchar(500), port varchar(10), result_type integer, rule_id longtext, rule_name longtext, safe_name varchar(500), start_time varchar(19), version varchar(200), ywid varchar(255), bae002 varchar(255), bae004 varchar(255), account_password_id varchar(255), create_user_id varchar(255), epv_password_id varchar(255), primary key (id)) engine=InnoDB;
create table dna_comm_search_connect (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, name varchar(200), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_comm_task (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, account varchar(50), account_type integer, address varchar(50), day varchar(500), description varchar(1000), engine integer, epv_username varchar(100), frequency integer, hour varchar(6), minute varchar(6), month varchar(500), name varchar(200), platformid varchar(500), port varchar(10), pwma_url varchar(300), rwcjr varchar(255), smtypezt varchar(255), safe_name varchar(500), second varchar(6), smtype varchar(255), version varchar(200), week varchar(200), task_type integer, bae002 varchar(255), bae004 varchar(255), account_password_id varchar(255), app_job_id varchar(255), epv_password_id varchar(255), primary key (id)) engine=InnoDB;
create table dna_comm_task_history (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, account varchar(50), account_type integer, address varchar(50), day varchar(500), description varchar(1000), engine integer, epv_username varchar(100), frequency integer, hour varchar(6), minute varchar(6), month varchar(500), name varchar(200), platformid varchar(500), port varchar(10), pwma_url varchar(300), rwcjr varchar(255), safe_name varchar(500), second varchar(6), smtype varchar(255), version varchar(200), week varchar(200), deal_unique_id varchar(500), end_time varchar(19), failure varchar(2000), result_type integer, start_time varchar(19), bae002 varchar(255), bae004 varchar(255), account_password_id varchar(255), app_job_id varchar(255), epv_password_id varchar(255), create_user_id varchar(255), dna_task_id varchar(255), primary key (id)) engine=InnoDB;
create table dna_extra_search_his_task (id varchar(255) not null, primary key (id)) engine=InnoDB;
create table dna_extra_search_task (id varchar(255) not null, primary key (id)) engine=InnoDB;
create table external_result (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, _json longtext, extra_jcid varchar(255), extra_jcname varchar(255), is_delete varchar(255), pch varchar(255), scan_result_title_id varchar(255), task_his_id varchar(255), value varchar(255), ywid varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table external_result_title (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, is_delete varchar(255), scan_type_id varchar(255), title varchar(255), title_displayname varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table extra_search_account_his (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, log_path varchar(255), pch varchar(255), result_type integer, rule_id longtext, rule_name longtext, scan_type_id varchar(255), start_time varchar(255), task_his_id varchar(255), ywid varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table file_upload (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, buss_type varchar(50) not null, description varchar(255), file_name varchar(150) not null, file_name_orig varchar(150) not null, file_path varchar(255) not null, bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table pswd (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, pswd varchar(1000), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table rule_result (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, accountid varchar(255), pch varchar(255), rule_id longtext, rule_name longtext, bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table scan_result (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, is_delete varchar(255), task_his_id varchar(255), ywid varchar(255), bae002 varchar(32), bae004 varchar(32), primary key (id)) engine=InnoDB;
create table scan_result_map (scan_result_id varchar(255) not null, scan_result_vaule varchar(4000), scan_result_key varchar(255) not null, primary key (scan_result_id, scan_result_key)) engine=InnoDB;
create table scan_result_title (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, excute_pch varchar(255), is_delete varchar(255), scan_type_id varchar(255), title varchar(255), title_displayname varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table scan_rule_name (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, accoutid varchar(255), rule_id longtext, rule_name longtext, task_id varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table yw_file_upload (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, buss_type varchar(50) not null, description varchar(255), file_name varchar(150) not null, file_name_orig varchar(150) not null, file_path varchar(255) not null, scan_type_id varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table zen_dic (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, code varchar(255), dic_name varchar(255), order_no integer not null, value varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table zen_sys_user (dtype varchar(31) not null, id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, login_name varchar(255), name varchar(255), password varchar(255), pwd varchar(1000), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table zen_sys_role (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, name varchar(100) not null, org_id varchar(100), type varchar(100), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table zen_sys_role_menu (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, menu_id varchar(255) not null, menu_name varchar(255) not null, role_id varchar(255) not null, bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table zen_sys_user_role (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, role_id varchar(255) not null, user_id varchar(255) not null, bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_scan_result_title_configure (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, externalname varchar(255), is_yes_no integer, is_scan varchar(255), is_changed varchar(255), is_result varchar(255), task_id varchar(255), title_push varchar(255), title_scan varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_scan_result_configure (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, externalname varchar(255), is_delete varchar(255), pch varchar(255), scan_result_title_id varchar(255), task_his_id varchar(255), value varchar(255), ywid varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table ywid (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, cur_number varchar(255), task_id varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_scan_result_checked (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, externalname varchar(255), is_delete varchar(255), pch varchar(255), scan_result_title_id varchar(255), task_his_id varchar(255), value varchar(255), ywid varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_push_history (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, externalname varchar(255), is_delete varchar(255), push_fail varchar(255), push_num varchar(255), push_success varchar(255), push_time varchar(255), task_his_id varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_push_result (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, externalname varchar(255), is_delete varchar(255), pch varchar(255), push_his_id varchar(255), push_msg varchar(255), push_status varchar(255), push_time varchar(255), scan_result_title_id varchar(255), task_his_id varchar(255), value varchar(255), ywid varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_scan_result_title_his_configure (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, externalname varchar(255), is_yes_no integer, is_changed varchar(255), is_scan varchar(255), result_taskid varchar(255), task_id varchar(255), title_push varchar(255), title_scan varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create table dna_run_task (id varchar(32) not null, bae003 datetime(6), bae005 datetime(6), has_del varchar(32) not null, taskid varchar(255), bae002 varchar(255), bae004 varchar(255), primary key (id)) engine=InnoDB;
create index hasDel on dna_run_task (has_del);
create index hasDel on dna_scan_result_title_his_configure (has_del);
create index hasDel on dna_push_history (has_del);
create index hasDel on dna_push_result (has_del);
create index hasDel on dna_scan_result_checked (has_del);
create index hasDel on dna_scan_result_configure (has_del);
create index hasDel on dna_scan_result_title_configure (has_del);
create index hasDel on app_job (has_del);
create index hasDel on dna_condition_sort (has_del);
create index hasDel on dna_external_name (has_del);
create index hasDel on dna_external_type (has_del);
create index hasDel on dna_extra_external_type (has_del);
create index hasDel on dna_extra_external_type_value (has_del);
create index hasDel on dna_extra_scan_type (has_del);
create index hasDel on dna_extra_scan_type_value (has_del);
create index hasDel on dna_rule (has_del);
create index hasDel on dna_rule_dic (has_del);
create index hasDel on dna_scan_type (has_del);
create index hasDel on dna_comm_search_account (has_del);
create index hasDel on dna_comm_search_connect (has_del);
create index hasDel on dna_comm_task (has_del);
create index hasDel on dna_comm_task_history (has_del);
create index hasDel on external_result (has_del);
create index hasDel on external_result_title (has_del);
create index hasDel on extra_search_account_his (has_del);
create index hasDel on file_upload (has_del);
create index hasDel on pswd (has_del);
create index hasDel on rule_result (has_del);
create index hasDel on scan_result (has_del);
create index hasDel on scan_result_title (has_del);
create index hasDel on scan_rule_name (has_del);
create index hasDel on yw_file_upload (has_del);
create index hasDel on zen_dic (has_del);
alter table zen_dic add constraint UKf5odiueov4fhtl58f9su09n9t unique (dic_name, code);
create index hasDel on zen_sys_user (has_del);
create index hasDel on zen_sys_role (has_del);
create index hasDel on zen_sys_role_menu (has_del);
create index hasDel on zen_sys_user_role (has_del);
create index hasDel on ywid (has_del);



CREATE INDEX scan_result_pch ON scan_result(pch);
CREATE INDEX rule_result_pch ON rule_result(pch);
CREATE INDEX scan_result_title_id ON scan_result(scan_result_title_id);


CREATE INDEX scan_result_title_scan_type_id ON scan_result_title(scan_type_id);
CREATE INDEX scan_result_title_excute_pch ON scan_result_title(excute_pch);
CREATE INDEX dna_scan_type_text ON dna_scan_type(text);

CREATE INDEX scan_result_value ON scan_result(value);

CREATE INDEX scan_result_task_his_id ON scan_result(task_his_id);

CREATE INDEX scan_result_is_delete ON scan_result(is_delete);
CREATE INDEX scan_result_title_is_delete ON scan_result_title(is_delete);


CREATE INDEX scan_result_title_id ON external_result(scan_result_title_id);
CREATE INDEX pch ON external_result(pch);


