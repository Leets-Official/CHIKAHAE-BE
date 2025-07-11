-- 드랍
-- 자식 테이블 먼저 드롭 (의존도 순서 중요)
DROP TABLE IF EXISTS fcm_tokens;
DROP TABLE IF EXISTS notification_slots;
DROP TABLE IF EXISTS user_point_history;
DROP TABLE IF EXISTS user_mission;
DROP TABLE IF EXISTS member_quiz;
DROP TABLE IF EXISTS quiz;
DROP TABLE IF EXISTS daily_quiz;
DROP TABLE IF EXISTS member_item;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS point;
DROP TABLE IF EXISTS alarm_log;
DROP TABLE IF EXISTS kakao_channel_token;
DROP TABLE IF EXISTS account_token;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS mission;
DROP TABLE IF EXISTS parent;



-- 부모 테이블
CREATE TABLE parent (
                        parent_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '고유 ID',
                        kakao_id VARCHAR(50) NOT NULL COMMENT '카카오 고유 ID',
                        email VARCHAR(100) NOT NULL COMMENT '카카오 이메일',
                        name VARCHAR(50) NOT NULL COMMENT '부모 이름(카톡에서 가져옴)',
                        created_at DATETIME NOT NULL COMMENT '가입일시',
                        updated_at DATETIME NOT NULL COMMENT '정보수정일시',
                        is_delete ENUM('Y', 'N') NOT NULL COMMENT '계정탈퇴 여부 (Y/N)'
);


-- 자녀 테이블
CREATE TABLE member (
                        member_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '고유 ID',
                        parent_id BIGINT NULL COMMENT '부모 ID',
                        name VARCHAR(200) NULL COMMENT '자녀 이름',
                        nickname VARCHAR(200) NOT NULL UNIQUE COMMENT '자녀 닉네임',
                        birth DATE NOT NULL COMMENT '생년월일',
                        profile_image VARCHAR(255) NULL COMMENT '프로필 이미지',
                        gender BOOLEAN NOT NULL COMMENT '성별 (true: 남, false: 여)',
                        is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '계정탈퇴 여부',
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '회원가입 일',
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일 (마이페이지)',
                        CONSTRAINT fk_member_parent
                            FOREIGN KEY (parent_id)
                                REFERENCES PARENT(parent_id)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
);


-- 토큰 테이블
CREATE TABLE account_token (
                               token_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '토큰ID',
                               member_id BIGINT NOT NULL COMMENT '회원 ID',
                               token_type ENUM('ACCESS', 'REFRESH') NOT NULL DEFAULT 'ACCESS' COMMENT '토큰 유형',
                               ip_address VARCHAR(200) NULL COMMENT '보안 로그인(고려)',
                               user_agent VARCHAR(200) NULL COMMENT '기기, 브라우저 기록',
                               expires_at DATETIME NULL COMMENT '만료일',
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
                               updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
                               CONSTRAINT fk_account_token_member
                                   FOREIGN KEY (member_id)
                                       REFERENCES MEMBER(member_id)
                                       ON DELETE CASCADE
                                       ON UPDATE CASCADE
);

-- 카카오 채널 토큰 테이블
CREATE TABLE kakao_channel_token (
                                     token_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '카카오 채널 토큰 ID',
                                     member_id BIGINT NOT NULL COMMENT '회원 ID',
                                     channel_user_id VARCHAR(100) NOT NULL COMMENT '카카오 채널 유저 ID',
                                     is_valid BOOLEAN NOT NULL DEFAULT TRUE COMMENT '토큰 유효 여부',
                                     linked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '연동 시각',

                                     CONSTRAINT fk_kakao_token_member FOREIGN KEY (member_id)
                                         REFERENCES MEMBER(member_id)
                                         ON DELETE CASCADE
                                         ON UPDATE CASCADE
);

-- 알람 로그 테이블
CREATE TABLE alarm_log (
                           alarm_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '알림 고유 ID',
                           member_id BIGINT NOT NULL COMMENT '대상 회원',
                           message_title VARCHAR(100) NOT NULL COMMENT '알림 제목',
                           message_content TEXT NOT NULL COMMENT '알림 내용',
                           sent_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '전송 시각',
                           success BOOLEAN NOT NULL DEFAULT TRUE COMMENT '전송 성공 여부',

                           CONSTRAINT fk_alarm_member FOREIGN KEY (member_id)
                               REFERENCES MEMBER(member_id)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
);

-- 포인트 테이블
CREATE TABLE point (
                       member_id BIGINT PRIMARY KEY,
                       coin INT NOT NULL,
                       CONSTRAINT fk_point_member
                           FOREIGN KEY (member_id)
                               REFERENCES member(member_id)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
);

-- 아이템 테이블
CREATE TABLE item (
                      item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
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
                             member_quiz_id BIGINT PRIMARY KEY AUTO_INCREMENT,
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
                                 FOREIGN KEY (quiz_id )
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
                            fcm_token VARCHAR(255) NOT NULL UNIQUE,
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_fcm_tokens_member
                                FOREIGN KEY (member_id)
                                    REFERENCES member(member_id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE
);
