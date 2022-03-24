INSERT INTO user (id, username, password, is_active)
    VALUES (1, "admin", "1", true);

INSERT INTO user_role (user_id, roles)
    VALUES (1, "USER"), (1, "ADMIN");