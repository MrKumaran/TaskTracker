
CREATE DATABASE IF NOT EXISTS tasktracker;

CREATE USER IF NOT EXISTS 'taskuser'@'%' IDENTIFIED BY 'taskpass';

GRANT ALL PRIVILEGES ON tasktracker.* TO 'taskuser'@'%';
FLUSH PRIVILEGES;

USE tasktracker;

CREATE TABLE IF NOT EXISTS authentication
(
    user_id  VARCHAR(36) NOT NULL PRIMARY KEY,
    mail     VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    salt     VARCHAR(10) NOT NULL
) COMMENT 'Authentication details';

CREATE TABLE IF NOT EXISTS profile
(
    user_id    VARCHAR(36) NOT NULL PRIMARY KEY,
    user_name  VARCHAR(50) NOT NULL,
    avatar_url VARCHAR(300),
    CONSTRAINT profile_authentication_user_id_fk
        FOREIGN KEY (user_id) REFERENCES authentication(user_id)
        ON DELETE CASCADE
) COMMENT 'User Profile table';

CREATE TABLE IF NOT EXISTS task
(
    user_id     VARCHAR(36) NOT NULL,
    task_id     VARCHAR(36) NOT NULL PRIMARY KEY,
    task_title  VARCHAR(100) NOT NULL,
    due         DATETIME NOT NULL,
    isDone      TINYINT(1) NOT NULL,
    completedAt DATETIME,
    CONSTRAINT task_profile_user_id_fk
        FOREIGN KEY (user_id) REFERENCES profile(user_id)
        ON DELETE CASCADE
) COMMENT 'Task table';

