INSERT INTO user (id, username, password, is_active)
    VALUES (1, "admin", "$2a$08$8NlXZoS8pKznp6IaaV/1FuCG4IcfWJUWAWR.5LxucwSL7L746ip1O", true);

INSERT INTO user_role (user_id, roles)
    VALUES (1, "USER"), (1, "ADMIN");