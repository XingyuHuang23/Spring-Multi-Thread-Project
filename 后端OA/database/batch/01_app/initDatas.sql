INSERT INTO `zen_sys_role` (`id`, `bae002`, `bae003`, `bae004`, `bae005`, `has_del`, `name`, `org_id`, `type`) VALUES ('4028b88157d196e70157d19707bf0000', NULL, '2018-7-5 09:16:59', NULL, '2018-7-5 09:17:02', '0', 'admin', NULL, NULL);

INSERT INTO `zen_sys_user` (`id`, `bae002`, `bae003`, `bae004`, `bae005`, `has_del`, `login_name`, `name`, `password`, `dtype`, `pwd`) VALUES ('4028b88157d196e70157d19708150003', NULL, '2018-7-5 09:16:10', NULL, '2018-7-5 09:16:13', '0', 'admin', 'admin', 'FtMVQSxNvuLFbEbwzY/jwUXnQysHy+AECGI4gsEll+HaA9XCshyK3szVS4gWXmc65cS9aG6vyvcOd6fNZm1gmjy+IKwMq5AaF92lYK7jkbsLixv60o3qg=aIkpeUfspP','UserExtend', '');

INSERT INTO `zen_sys_user_role` (`id`, `bae002`, `bae003`, `bae004`, `bae005`, `has_del`, `role_id`, `user_id`) VALUES ('4028b88157d196e70157d19708210004', NULL, '2018-7-5 09:18:16', NULL, '2018-7-5 09:18:18', '0', '4028b88157d196e70157d19707bf0000', '4028b88157d196e70157d19708150003');


INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0001', SYSDATE(), SYSDATE(), '0', '11', '库存管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0002', SYSDATE(), SYSDATE(), '0', '1101', '入库管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0003', SYSDATE(), SYSDATE(), '0', '1102', '出库管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0004', SYSDATE(), SYSDATE(), '0', '1103', '库存统计', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0005', SYSDATE(), SYSDATE(), '0', '12', '系统设置', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0006', SYSDATE(), SYSDATE(), '0', '1201', '基础信息', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0007', SYSDATE(), SYSDATE(), '0', '1202', '物品档案', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0008', SYSDATE(), SYSDATE(), '0', '13', '权限管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0009', SYSDATE(), SYSDATE(), '0', '1301', '用户权限管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);
INSERT INTO `zen_sys_role_menu` VALUES ('4028b88157d196e70157d19707fa0010', SYSDATE(), SYSDATE(), '0', '1302', '角色管理', '4028b88157d196e70157d19707bf0000', NULL, NULL);


DELETE FROM `zen_dic` WHERE `dic_name` = 'YesNo';
INSERT INTO `zen_dic` (`dic_name`, `code`, `value`, `order_no`, `id`, `bae003`, `has_del`) VALUES ('YesNo', '0', '否', 1, REPLACE(UUID(),'-',''), CURRENT_TIMESTAMP, '0');
INSERT INTO `zen_dic` (`dic_name`, `code`, `value`, `order_no`, `id`, `bae003`, `has_del`) VALUES ('YesNo', '1', '是', 2, REPLACE(UUID(),'-',''), CURRENT_TIMESTAMP, '0');
COMMIT;