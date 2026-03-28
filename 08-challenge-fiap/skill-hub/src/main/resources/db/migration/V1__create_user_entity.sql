CREATE TABLE user_entity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dt_created TIMESTAMP,
    dt_updated TIMESTAMP,
    status SMALLINT,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    profile SMALLINT
);

