
INSERT INTO permission (name) VALUES
('create_profile'),
('read_profile'),
('update_profile'),
('delete_profile');

INSERT INTO role (id, name) VALUES
(1,'ROLE_admin'),(2,'ROLE_operator');

INSERT INTO permission_role (permission_id, role_id) VALUES
(1,1), /*create-> admin */
(2,1), /* read admin */
(3,1), /* update admin */
(4,1), /* delete admin */
(2,2),  /* read operator */
(3,2);  /* update operator */
insert into user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('1', 'krish','123456', 'k@krishantha.com', '1', '1', '1', '1');
insert into  user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('2', 'suranga', '123456','k@krishantha.com', '1', '1', '1', '1');

INSERT INTO role_user (role_id, user_id)
VALUES
(1, 1) /* krish-admin */,
(2, 2) /* suranga-operatorr */ ;

INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity, refresh_token_validity, resource_ids, authorized_grant_types, additional_information) VALUES
('mobile', '123456', 'http://localhost:8080/code', 'READ,WRITE', '3600', '10000', 'inventory,payment', 'authorization_code,password,refresh_token,implicit', '{}');
