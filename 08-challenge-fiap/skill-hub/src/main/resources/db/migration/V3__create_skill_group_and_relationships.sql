CREATE TABLE skill_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dt_created TIMESTAMP,
    dt_updated TIMESTAMP,
    status SMALLINT,
    description VARCHAR(80) NOT NULL,
    CONSTRAINT uk_skill_description UNIQUE (description)
);

CREATE TABLE user_skill (
    user_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (user_id, skill_id),
    CONSTRAINT fk_user_skill_user FOREIGN KEY (user_id) REFERENCES user_entity(id),
    CONSTRAINT fk_user_skill_skill FOREIGN KEY (skill_id) REFERENCES skill_entity(id)
);

CREATE TABLE group_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dt_created TIMESTAMP,
    dt_updated TIMESTAMP,
    status SMALLINT,
    description VARCHAR(120) NOT NULL,
    max_members INT NOT NULL,
    owner_user_id INT NOT NULL,
    CONSTRAINT fk_group_owner FOREIGN KEY (owner_user_id) REFERENCES user_entity(id)
);

CREATE TABLE group_skill_requirement (
    group_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (group_id, skill_id),
    CONSTRAINT fk_group_skill_group FOREIGN KEY (group_id) REFERENCES group_entity(id),
    CONSTRAINT fk_group_skill_skill FOREIGN KEY (skill_id) REFERENCES skill_entity(id)
);

CREATE TABLE group_member (
    group_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (group_id, user_id),
    CONSTRAINT fk_group_member_group FOREIGN KEY (group_id) REFERENCES group_entity(id),
    CONSTRAINT fk_group_member_user FOREIGN KEY (user_id) REFERENCES user_entity(id)
);

