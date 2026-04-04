ALTER TABLE user_entity
    ADD CONSTRAINT uk_user_entity_email UNIQUE (email);

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
