-- Tabela de usuários
CREATE TABLE tb_users (
                          id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name        VARCHAR(100)  NOT NULL,
                          email       VARCHAR(150)  NOT NULL UNIQUE,
                          password    VARCHAR(255)  NOT NULL,
                          profile     VARCHAR(20),
                          status      VARCHAR(20)   DEFAULT 'ACTIVE',
                          dt_created  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                          dt_updated  TIMESTAMP
);

-- Tabela de skills
CREATE TABLE tb_skills (
                           id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name        VARCHAR(100)  NOT NULL UNIQUE,
                           category    VARCHAR(100)
);

-- Tabela de grupos
CREATE TABLE tb_groups (
                           id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name        VARCHAR(100)  NOT NULL,
                           description VARCHAR(255)  NOT NULL,
                           max_numbers INTEGER       NOT NULL,
                           created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                           owner_id    BIGINT,
                           CONSTRAINT fk_group_owner FOREIGN KEY (owner_id) REFERENCES tb_users(id)
);

-- Tabela de membros do grupo
CREATE TABLE tb_group_members (
                                  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  group_id    BIGINT        NOT NULL,
                                  user_id     BIGINT        NOT NULL,
                                  role        VARCHAR(20)   NOT NULL,
                                  joined_at   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_member_group FOREIGN KEY (group_id) REFERENCES tb_groups(id),
                                  CONSTRAINT fk_member_user  FOREIGN KEY (user_id)  REFERENCES tb_users(id)
);

-- Tabela de relacionamento usuário x skills
CREATE TABLE tb_user_skills (
                                user_id     BIGINT NOT NULL,
                                skill_id    BIGINT NOT NULL,
                                PRIMARY KEY (user_id, skill_id),
                                CONSTRAINT fk_us_user  FOREIGN KEY (user_id)  REFERENCES tb_users(id),
                                CONSTRAINT fk_us_skill FOREIGN KEY (skill_id) REFERENCES tb_skills(id)
);