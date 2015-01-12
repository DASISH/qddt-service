// Add two demo accounts
// admin:password & user:password (bcrypt(10) passwords)
INSERT INTO user (username, password, email) VALUES('admin', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'admin@example.org');
INSERT INTO user (username, password, email) VALUES('user', '$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW', 'user@example.org');

// Create ADMIN and USER authorities
INSERT INTO authority (name, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authority (name, authority) VALUES('user', 'ROLE_USER');

// Set up admin authorities
INSERT INTO user_authority (user_id, authority_id) VALUES(1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES(1, 2);
// Set up user authorities
INSERT INTO user_authority (user_id, authority_id) VALUES(2, 2);