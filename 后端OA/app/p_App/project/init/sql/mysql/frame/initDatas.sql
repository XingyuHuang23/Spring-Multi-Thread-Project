INSERT INTO `zen_sys_role` (`id`, `bae002`, `bae003`, `bae004`, `bae005`, `has_del`, `name`, `org_id`, `type`) VALUES ('4028b88157d196e70157d19707bf0000', NULL, '2018-7-5 09:16:59', NULL, '2018-7-5 09:17:02', '0', 'admin', NULL, NULL);

INSERT INTO `zen_sys_user` (`id`, `bae002`, `bae003`, `bae004`, `bae005`, `has_del`, `login_name`, `name`, `password`, `dtype`, `pwd`) VALUES ('4028b88157d196e70157d19708150003', NULL, '2018-7-5 09:16:10', NULL, '2018-7-5 09:16:13', '0', 'admin', 'admin', 'FtMVQSxNvuLFbEbwzY/jwUXnQysHy+AECGI4gsEll+HaA9XCshyK3szVS4gWXmc65cS9aG6vyvcOd6fNZm1gmjy+IKwMq5AaF92lYK7jkbsLixv60o3qg=aIkpeUfspP','UserExtend', '');

INSERT INTO `zen_sys_user_role` (`id`, `bae002`, `bae003`, `bae004`, `bae005`, `has_del`, `role_id`, `user_id`) VALUES ('4028b88157d196e70157d19708210004', NULL, '2018-7-5 09:18:16', NULL, '2018-7-5 09:18:18', '0', '4028b88157d196e70157d19707bf0000', '4028b88157d196e70157d19708150003');

INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0001', SYSDATE(), SYSDATE(), '0', '6601', '用户管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19708040002', SYSDATE(), SYSDATE(), '0', '6602', '角色管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);

INSERT INTO `zen_sys_role_menu` VALUES ('818181826acb9e3d016acba42ccb0000', SYSDATE(), SYSDATE(), '0', '33', '统计分析', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('818181826acb9e3d016acba42d050001', SYSDATE(), SYSDATE(), '0', '34', '扫描任务', '4028b88157d196e70157d19707bf0000', NULL, NULL);

INSERT INTO `zen_sys_role_menu` VALUES ('818181826acb9e3d016acba42d2b0002', SYSDATE(), SYSDATE(), '0', '3501', '扫描类型管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('818181826acb9e3d016acba42d4d0003', SYSDATE(), SYSDATE(), '0', '3502', '分类规则管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('818181826acb9e3d016acba42d4d0044', SYSDATE(), SYSDATE(), '0', '3503', '外部集成管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);



