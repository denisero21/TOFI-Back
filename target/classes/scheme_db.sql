CREATE TABLE user_
(
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255)            NOT NULL,
    password        VARCHAR(60)             NOT NULL,
    date            TIMESTAMP DEFAULT now() NOT NULL,
    full_name       VARCHAR(90)             NOT NULL,
    phone_number    VARCHAR(13)             NOT NULL,
    is_enabled      BOOL                    NOT NULL,
    two_factor_auth BOOLEAN                 NOT NULL
);

CREATE TABLE role_
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(120),
    creation_date TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE privilege_
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(120)
);

CREATE TABLE user_role
(
    user_id BIGSERIAL NOT NULL,
    role_id BIGSERIAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_ (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role_ (id) ON DELETE CASCADE
);

CREATE TABLE role_privilege
(
    role_id      BIGSERIAL NOT NULL,
    privilege_id BIGSERIAL NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role_ (id) ON DELETE CASCADE,
    FOREIGN KEY (privilege_id) REFERENCES privilege_ (id)
);

CREATE TABLE password_reset_token
(
    id          BIGSERIAL PRIMARY KEY,
    expiry_date TIMESTAMP    NOT NULL,
    token       VARCHAR(255) NOT NULL,
    user_id     INTEGER      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_ (id) ON DELETE CASCADE
);

CREATE TABLE sms_code
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     INTEGER   NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    code        INTEGER   NOT NULL
);
CREATE TABLE locked
(
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    user_locked BOOLEAN,
    attempt     INTEGER,
    lock_time   TIMESTAMP
);
CREATE TABLE user_verification_token
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(500) NOT NULL,
    expire_at  TIMESTAMP    NOT NULL,
    user_id    BIGSERIAL    NOT NULL,
    token_type VARCHAR(255),
    valid      BOOL
);

ALTER TABLE user_verification_token
    ADD CONSTRAINT qp_user_verification_token_user_id FOREIGN KEY (user_id) REFERENCES user_ (id) ON DELETE CASCADE ON UPDATE CASCADE;


