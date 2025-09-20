INSERT INTO verdura (id, nombre, precio, troceable) VALUES ('2001', 'Tomate H2 Test', '3.82', false);
INSERT INTO verdura (id, nombre, precio, troceable) VALUES ('2002', 'Calabaza H2 Test', '4.82', true);
INSERT INTO verdura (id, nombre, precio, troceable) VALUES ('2003', 'Lechuga H2 Test', '2.82', false);

INSERT INTO users (id, username, password, role, enabled)
VALUES (2001, 'admin', '$2a$10$oWOciKZMR0wpgyoA39AqvuvrpiDuEpaYd203Ub3SqPvOwPNoDtUQO', 'ROLE_ADMIN', 1); --> password
INSERT INTO users (id, username, password, role, enabled)
VALUES (2002, 'carmen', '$2a$10$DNY7JSm3xLmQiah663oyserYDYQKGVSGG7oBvzXT/9ZFIs0uRL3qG', 'ROLE_USER', 1); --> lechuguita123

--> Usar web bcrypt-generator 10 ciclos
