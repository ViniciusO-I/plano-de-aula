CREATE TABLE user_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dt_created TIMESTAMP,
    dt_updated TIMESTAMP,
    status SMALLINT,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    profile SMALLINT,
    CONSTRAINT uk_user_entity_email UNIQUE (email)
);

CREATE TABLE skill_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dt_created TIMESTAMP,
    dt_updated TIMESTAMP,
    status SMALLINT,
    description VARCHAR(255)
);

CREATE TABLE group_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dt_created TIMESTAMP,
    dt_updated TIMESTAMP,
    status SMALLINT,
    description VARCHAR(255),
    max_members INT,
    owner_user_id INT NOT NULL,
    CONSTRAINT fk_group_owner FOREIGN KEY (owner_user_id) REFERENCES user_entity(id)
);

CREATE TABLE refresh_token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dt_created TIMESTAMP,
    dt_updated TIMESTAMP,
    status SMALLINT,
    user_id INT NOT NULL,
    token_hash VARCHAR(64) NOT NULL,
    jti VARCHAR(36) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES user_entity(id),
    CONSTRAINT uk_refresh_token_hash UNIQUE (token_hash),
    CONSTRAINT uk_refresh_token_jti UNIQUE (jti)
);

CREATE TABLE user_skill (
    user_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (user_id, skill_id),
    CONSTRAINT fk_user_skill_user FOREIGN KEY (user_id) REFERENCES user_entity(id),
    CONSTRAINT fk_user_skill_skill FOREIGN KEY (skill_id) REFERENCES skill_entity(id)
);

CREATE TABLE group_member (
    group_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (group_id, user_id),
    CONSTRAINT fk_group_member_group FOREIGN KEY (group_id) REFERENCES group_entity(id),
    CONSTRAINT fk_group_member_user FOREIGN KEY (user_id) REFERENCES user_entity(id)
);

CREATE TABLE group_skill_requirement (
    group_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (group_id, skill_id),
    CONSTRAINT fk_group_skill_group FOREIGN KEY (group_id) REFERENCES group_entity(id),
    CONSTRAINT fk_group_skill_skill FOREIGN KEY (skill_id) REFERENCES skill_entity(id)
);
