INSERT INTO `usuarios` (username, password, enabled) VALUES ('andres','$2a$10$6YpnQR7OxgxGvFSbZc5kZOzg21yaO.ZxNJ58h7rRq.zC3mCYxnoqS',1);
INSERT INTO `usuarios` (username, password, enabled) VALUES ('admin','$2a$10$KRbldG0tNsyXOwMxKi4bk.iJluXsHH0e2fvHdL0C/jFVmC4Cy2lSm',1);

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuarios_id, roles_id) VALUES (1, 1);
INSERT INTO `usuarios_roles` (usuarios_id, roles_id) VALUES (2, 2);
INSERT INTO `usuarios_roles` (usuarios_id, roles_id) VALUES (2, 1);