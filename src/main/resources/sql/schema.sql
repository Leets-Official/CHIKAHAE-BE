CREATE TABLE parent (
                        parent_id BIGINT PRIMARY KEY NOT NULL COMMENT '고유 ID',
                        kakao_id VARCHAR(50) NOT NULL COMMENT '카카오 고유 ID',
                        email VARCHAR(100) NOT NULL COMMENT '카카오 이메일',
                        name VARCHAR(50) NOT NULL COMMENT '부모 이름(카톡에서 가져옴)',
                        created_at DATETIME NOT NULL COMMENT '가입일시',
                        updated_at DATETIME NOT NULL COMMENT '정보수정일시',
                        is_delete ENUM('Y', 'N') NOT NULL COMMENT '계정탈퇴 여부 (Y/N)'
);

CREATE TABLE member (
                        member_id BIGINT PRIMARY KEY NOT NULL COMMENT '고유 ID',
                        parent_id BIGINT NOT NULL COMMENT '부모 ID',
                        password VARCHAR(200) NULL COMMENT '암호화 (고려 중)',
                        name VARCHAR(200) NULL COMMENT '자녀 이름',
                        nickname VARCHAR(200) NOT NULL COMMENT '자녀 닉네임',
                        birth DATE NOT NULL COMMENT '생년월일',
                        profile_image VARCHAR(255) NULL COMMENT '프로필 이미지',
                        gender BOOLEAN NOT NULL COMMENT '성별',
                        point INT NOT NULL COMMENT '치카코인',
                        is_delete ENUM('Y', 'N') NOT NULL COMMENT '계정탈퇴 여부 (Y/N)',
                        created_at DATETIME NULL COMMENT '회원가입 일',
                        updated_at DATETIME NULL COMMENT '수정일 (마이페이지)',
                        CONSTRAINT fk_member_parent
                            FOREIGN KEY (parent_id)
                                REFERENCES parent(parent_id)
);

CREATE TABLE account_token (
                               token_id BIGINT PRIMARY KEY NOT NULL COMMENT '토큰ID',
                               member_id BIGINT NOT NULL COMMENT '고유 ID',
                               ip_address VARCHAR(200) NULL COMMENT '보안 로그인(고려)',
                               user_agent VARCHAR(200) NULL COMMENT '기기, 브라우저 기록',
                               expires_at DATETIME NULL COMMENT '만료일',
                               created_at DATETIME NULL COMMENT '생성일',
                               updated_at DATETIME NULL COMMENT '수정일',
                               CONSTRAINT fk_account_token_member
                                   FOREIGN KEY (member_id)
                                       REFERENCES member(member_id)
                                       ON DELETE CASCADE
                                       ON UPDATE CASCADE
);

CREATE TABLE point (
                       member_id BIGINT PRIMARY KEY,
                       coin INT NOT NULL,
                       CONSTRAINT fk_point_member
                           FOREIGN KEY (member_id)
                               REFERENCES member(member_id)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
);
CREATE TABLE item (
                      item_id BIGINT PRIMARY KEY,
                      name VARCHAR(255),
                      price INT,
                      image VARCHAR(255)
);
CREATE TABLE member_item (
                             member_id BIGINT NOT NULL,
                             item_id BIGINT NOT NULL,
                             PRIMARY KEY (member_id, item_id),
                             CONSTRAINT fk_member_item_member
                                 FOREIGN KEY (member_id)
                                     REFERENCES member(member_id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE,
                             CONSTRAINT fk_member_item_item
                                 FOREIGN KEY (item_id)
                                     REFERENCES item(item_id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE
);



CREATE TABLE daily_quiz (
                            daily_quiz_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            quiz_date DATE NOT NULL
);

CREATE TABLE quiz (
                      quiz_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      daily_quiz_id BIGINT NOT NULL,
                      type ENUM('OX','MCQ') NOT NULL,
                      question VARCHAR(255) NOT NULL,
                      answer VARCHAR(255) NOT NULL,
                      answer_description TEXT,
                      options JSON,
                      CONSTRAINT fk_quiz_daily_quiz
                          FOREIGN KEY (daily_quiz_id)
                              REFERENCES daily_quiz(daily_quiz_id)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE
);

CREATE TABLE member_quiz (
                             member_quiz_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             member_id BIGINT NOT NULL,
                             quiz_id BIGINT NOT NULL,
                             selected_answer VARCHAR(255),
                             is_correct BOOLEAN,
                             CONSTRAINT fk_member_quiz_member
                                 FOREIGN KEY (member_id)
                                     REFERENCES member(member_id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE,
                             CONSTRAINT fk_member_quiz_quiz
                                 FOREIGN KEY (quiz_id)
                                     REFERENCES quiz(quiz_id)
                                     ON DELETE CASCADE
                                     ON UPDATE CASCADE
);

CREATE TABLE mission (
                         mission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         step INT NOT NULL,
                         point INT DEFAULT 0,
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_mission (
                              user_mission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              member_id BIGINT NOT NULL,
                              parent_id BIGINT NOT NULL,
                              mission_id BIGINT NOT NULL,
                              status ENUM('WAITING', 'COMPLETED', 'REWARDED') DEFAULT 'WAITING',
                              target_date DATE NOT NULL,
                              reward_at DATETIME DEFAULT NULL,
                              CONSTRAINT fk_user_mission_member
                                  FOREIGN KEY (member_id)
                                      REFERENCES member(member_id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                              CONSTRAINT fk_user_mission_parent
                                  FOREIGN KEY (parent_id)
                                      REFERENCES parent(parent_id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                              CONSTRAINT fk_user_mission_mission
                                  FOREIGN KEY (mission_id)
                                      REFERENCES mission(mission_id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                              INDEX idx_user_mission_member_date (member_id, target_date)
);

CREATE TABLE user_point_history (
                                    user_point_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    member_id BIGINT NOT NULL,
                                    parent_id BIGINT NOT NULL,
                                    amount INT NOT NULL,
                                    type ENUM('EARN', 'CONSUME') NOT NULL,
                                    description VARCHAR(255),
                                    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_user_point_history_member
                                        FOREIGN KEY (member_id)
                                            REFERENCES member(member_id)
                                            ON DELETE CASCADE
                                            ON UPDATE CASCADE,
                                    CONSTRAINT fk_user_point_history_parent
                                        FOREIGN KEY (parent_id)
                                            REFERENCES parent(parent_id)
                                            ON DELETE CASCADE
                                            ON UPDATE CASCADE,
                                    INDEX idx_point_history_member (member_id)
);

CREATE TABLE notification_slots (
                                    slot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    member_id BIGINT NOT NULL,
                                    slot_type ENUM('MORNING','LUNCH','EVENING') NOT NULL,
                                    send_time TIME NOT NULL,
                                    next_send_at DATETIME NOT NULL,
                                    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
                                    title VARCHAR(200) NOT NULL,
                                    message VARCHAR(500) NOT NULL,
                                    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    UNIQUE KEY uq_slots_member_type (member_id, slot_type),
                                    CONSTRAINT fk_notification_slots_member
                                        FOREIGN KEY (member_id)
                                            REFERENCES member(member_id)
                                            ON DELETE CASCADE
                                            ON UPDATE CASCADE
);

CREATE TABLE fcm_tokens (
                            fcm_token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            member_id BIGINT NOT NULL,
                            FCM_token VARCHAR(255) NOT NULL UNIQUE,
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_fcm_tokens_member
                                FOREIGN KEY (member_id)
                                    REFERENCES member(member_id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE
);
