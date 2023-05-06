set sql_safe_updates=0;
 CREATE TABLE IF NOT EXISTS `sequence` (
  `name` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='序列表，命名s_[table_name]';
INSERT INTO `sequence` (`name`, `current_value`, `increment`) VALUES
('rule_dic', 0, 1) ;

INSERT INTO `sequence` (`name`, `current_value`, `increment`) VALUES
('scan_type', 5, 1) ;

INSERT INTO `sequence` (`name`, `current_value`, `increment`) VALUES
('pch', 1000, 1) ;

INSERT INTO `sequence` (`name`, `current_value`, `increment`) VALUES
('ywid', 1, 1) ;

INSERT INTO `sequence` (`name`, `current_value`, `increment`) VALUES
('external_type', 200, 1) ;

INSERT INTO `sequence` (`name`, `current_value`, `increment`) VALUES
('rule_pch', 1, 1) ;


DROP FUNCTION IF EXISTS `currval`;
DELIMITER //
CREATE  FUNCTION `currval`(seq_name VARCHAR(50)) RETURNS int(11)
    READS SQL DATA
    DETERMINISTIC
BEGIN
DECLARE VALUE INTEGER;
SET VALUE = 0;
SELECT current_value INTO VALUE FROM sequence WHERE NAME = seq_name;
RETURN VALUE;
END//
DELIMITER ;


DROP FUNCTION IF EXISTS `nextval`;
DELIMITER //
CREATE  FUNCTION `nextval`(seq_name VARCHAR(50)) RETURNS int(11)
    DETERMINISTIC
BEGIN
UPDATE sequence SET current_value = current_value + increment WHERE NAME = seq_name;
RETURN currval(seq_name);
END//
DELIMITER ;


-- INSERT INTO `dna_scan_type` (`id`, `has_del`, `code`, `text`, `type`, `connect_out`, `is_delete`) VALUES ('818181826acba618016acbab9e3c0000', '0', '01', 'Windows', '0','30','0');
-- INSERT INTO `dna_scan_type` (`id`, `has_del`, `code`, `text`, `type`, `connect_out`, `is_delete`) VALUES ('818181826acba618016acbab9ec30001', '0', '02', 'Linux', '0','30','0');
-- INSERT INTO `dna_scan_type` (`id`, `has_del`, `code`, `text`, `type`, `connect_out`, `is_delete`) VALUES ('818181826acba618016acbab9ef30002', '0', '03', 'Oracle', '0','30','0');
-- INSERT INTO `dna_scan_type` (`id`, `has_del`, `code`, `text`, `type`, `connect_out`, `is_delete`) VALUES ('818181826acba618016acbab9f2b0003', '0', '04', 'MySQL', '0','30','0');
-- INSERT INTO `dna_scan_type` (`id`, `has_del`, `code`, `text`, `type`, `connect_out`, `is_delete`) VALUES ('818181826acba618016acbab9f730004', '0', '05', 'MSSQL', '0','30','0');
--
--
--
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e91b00000', '0', '818181826acba618016acbab9e3c0000', 'address', '地址','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e91fd0001', '0', '818181826acba618016acbab9e3c0000', 'name', '用户名','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92180002', '0', '818181826acba618016acbab9e3c0000', 'rule_name', '分类名称','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92330003', '0', '818181826acba618016acbab9e3c0000', 'passwordexpires', '密码过期','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92450004', '0', '818181826acba618016acbab9e3c0000', 'disabled', '账号状态','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92510005', '0', '818181826acba618016acbab9e3c0000', 'domain', '域名','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('88726681cfb311e9946cfa163ef628ad', '1', '818181826acba618016acbab9e3c0000', 'groupname', '组名','1');
--
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92610006', '0', '818181826acba618016acbab9ec30001', 'address', '地址','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e927d0007', '0', '818181826acba618016acbab9ec30001', 'name', '用户名','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92960008', '0', '818181826acba618016acbab9ec30001', 'rule_name', '分类名称','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92ad0009', '0', '818181826acba618016acbab9ec30001', 'passwExpiredate', '密码过期','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92b7000a', '0', '818181826acba618016acbab9ec30001', 'accountState', '账号状态','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92d1000b', '0', '818181826acba618016acbab9ec30001', 'numPasswdNotChange', '未改密天数','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('8c3f712acd1d11e9b961005056840959', '1', '818181826acba618016acbab9ec30001', 'userid', '用户ID','1');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('98a0e24ecd1d11e9b961005056840959', '1', '818181826acba618016acbab9ec30001', 'groupname', '组名','1');
--
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92df000c', '0', '818181826acba618016acbab9ef30002', 'address', '地址','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e92f4000d', '0', '818181826acba618016acbab9ef30002', 'name', '用户名','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e9301000e', '0', '818181826acba618016acbab9ef30002', 'rule_name', '分类名称','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e930e000f', '0', '818181826acba618016acbab9ef30002', 'isDba', '是否DBA','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93260010', '0', '818181826acba618016acbab9ef30002', 'accountStatus', '用户状态','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93310011', '0', '818181826acba618016acbab9ef30002', 'expiryDate', '到期日期','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e933c0012', '0', '818181826acba618016acbab9ef30002', 'lockDate', '锁定日期','0');
--
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93470013', '0', '818181826acba618016acbab9f2b0003', 'address', '地址','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93520014', '0', '818181826acba618016acbab9f2b0003', 'name', '用户名','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e935e0015', '0', '818181826acba618016acbab9f2b0003', 'rule_name', '分类名称','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93680016', '0', '818181826acba618016acbab9f2b0003', 'isDba', '是否DBA','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93720017', '0', '818181826acba618016acbab9f2b0003', 'passwordExpired', '密码过期','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93860018', '0', '818181826acba618016acbab9f2b0003', 'state', '账号状态','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93900019', '0', '818181826acba618016acbab9f2b0003', 'passwordLastChanged', '最近一次改密时间','0');
--
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93a4001a', '0', '818181826acba618016acbab9f730004', 'address', '地址','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93ae001b', '0', '818181826acba618016acbab9f730004', 'name', '用户名','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93b8001c', '0', '818181826acba618016acbab9f730004', 'rule_name', '分类名称','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93cc001d', '0', '818181826acba618016acbab9f730004', 'isDba', '是否DBA','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93d6001e', '0', '818181826acba618016acbab9f730004', 'createdate', '添加日期','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e93ea001f', '0', '818181826acba618016acbab9f730004', 'updatedate', '上次修改日期','0');
-- INSERT INTO `scan_result_title` (`id`, `has_del`, `scan_type_id`, `title`, `title_displayname`, `is_delete`) VALUES ('40289e816b6869ec016b686e94d60020', '0', '818181826acba618016acbab9f730004', 'hasaccess', '数据库访问权限','0');
--
--
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '地址', 'address', '1', '818181826acba618016acbab9e3c0000','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '用户名', 'username', '1', '818181826acba618016acbab9e3c0000','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `type`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '密码', 'password', '1', '818181826acba618016acbab9e3c0000','password','0');
--
--
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '地址', 'address', '1', '818181826acba618016acbab9ec30001','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '端口', 'port', '1', '818181826acba618016acbab9ec30001','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '用户名', 'username', '1', '818181826acba618016acbab9ec30001','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `type`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '密码', 'password', '1', '818181826acba618016acbab9ec30001','password','0');
--
--
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '地址', 'address', '1', '818181826acba618016acbab9ef30002','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '端口', 'port', '1', '818181826acba618016acbab9ef30002','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '用户名', 'username', '1', '818181826acba618016acbab9ef30002','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `type`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '密码', 'password', '1', '818181826acba618016acbab9ef30002','password','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '数据库名', 'databasename', '1', '818181826acba618016acbab9ef30002','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `type`, `dicname`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '连接方式', 'connecttype', '1', '818181826acba618016acbab9ef30002','select','connecttype','0');
--
--
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '地址', 'address', '1', '818181826acba618016acbab9f2b0003','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '端口', 'port', '1', '818181826acba618016acbab9f2b0003','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '用户名', 'username', '1', '818181826acba618016acbab9f2b0003','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `type`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '密码', 'password', '1', '818181826acba618016acbab9f2b0003','password','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '数据库名', 'databasename', '1', '818181826acba618016acbab9f2b0003','0');
--
--
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '地址', 'address', '1', '818181826acba618016acbab9f730004','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '端口', 'port', '1', '818181826acba618016acbab9f730004','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '用户名', 'username', '1', '818181826acba618016acbab9f730004','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `type`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '密码', 'password', '1', '818181826acba618016acbab9f730004','password','0');
-- INSERT INTO `dna_extra_scan_type` (`id`, `has_del`, `displayname`, `name`, `required`, `scan_type_id`, `is_delete`) VALUES (REPLACE(UUID(), '-', ''), '0', '数据库名', 'databasename', '1', '818181826acba618016acbab9f730004','0');