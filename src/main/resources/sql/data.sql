-- 1. parent
INSERT INTO parent (parent_id, kakao_id, email, name, created_at, updated_at, is_delete) VALUES
                                                                                             (1, 'kakao_001', 'parent1@example.com', '김민수', NOW(), NOW(), 'N'),
                                                                                             (2, 'kakao_002', 'parent2@example.com', '이영희', NOW(), NOW(), 'N'),
                                                                                             (3, 'kakao_003', 'parent3@example.com', '박정우', NOW(), NOW(), 'Y'),
                                                                                             (4, 'kakao_004', 'parent4@example.com', '최지훈', NOW(), NOW(), 'N'),
                                                                                             (5, 'kakao_005', 'parent5@example.com', '정수연', NOW(), NOW(), 'N');

-- 2. member
INSERT INTO member (member_id, parent_id, name, nickname, birth, profile_image, gender, is_deleted, created_at, updated_at)
VALUES
    (101, 1, '김서준', '치카요정1', '2015-04-12', NULL, 1, FALSE, NOW(), NOW()),
    (102, 1, '김하린', '치카요정2', '2017-08-25', NULL, 0, FALSE, NOW(), NOW()),
    (103, 2, '이도윤', '치카마스터', '2016-12-02', NULL, 1, FALSE, NOW(), NOW()),
    (104, 3, '박지민', '치카짱', '2014-09-30', NULL, 0, FALSE, NOW(), NOW()),
    (105, 4, '최예린', '치카왕', '2013-11-20', NULL, 1, FALSE, NOW(), NOW());

-- 3. point
INSERT INTO point (member_id, coin) VALUES
                                        (101, 100),
                                        (102, 200),
                                        (103, 150),
                                        (104, 170),
                                        (105, 90);

-- 4. item
INSERT INTO item (item_id, name, price, image) VALUES
                                                   (1, '치약', 50, 'item1.png'),
                                                   (2, '칫솔', 30, 'item2.png'),
                                                   (3, '양치컵', 20, 'item3.png'),
                                                   (4, '치실', 25, 'item4.png'),
                                                   (5, '구강청결제', 40, 'item5.png');

-- 5. member_item
INSERT INTO member_item (member_id, item_id) VALUES
                                                 (101, 1), (101, 2), (101, 3),
                                                 (102, 2), (102, 4),
                                                 (103, 1), (103, 5),
                                                 (104, 3), (104, 4),
                                                 (105, 5);

-- 6. daily_quiz
INSERT INTO daily_quiz (daily_quiz_id, quiz_date) VALUES
                                                      (1, '2024-07-07 00:00:00'),
                                                      (2, '2024-07-08 00:00:00'),
                                                      (3, '2024-07-09 00:00:00'),
                                                      (4, '2024-07-10 00:00:00'),
                                                      (5, '2024-07-11 00:00:00');

-- 7. quiz
INSERT INTO quiz (quiz_id, daily_quiz_id, image, type, question, answer, answer_description, options) VALUES
                                                                                                          (1, 1, 'quiz1.png', 'OX', '양치는 하루에 두 번 해야 한다.', 'O', '정답: 하루 두 번 양치가 권장됩니다.', '["O","X"]'),
                                                                                                          (2, 1, 'quiz2.png', 'MCQ', '치약의 주성분은?', '불소', '불소는 충치 예방에 효과적입니다.', '["불소","소금","설탕","물"]'),
                                                                                                          (3, 2, 'quiz3.png', 'OX', '양치 후에는 물로 충분히 헹궈야 한다.', 'O', '정답: 잔여 치약이 남지 않도록 충분히 헹굽니다.', '["O","X"]'),
                                                                                                          (4, 3, 'quiz4.png', 'MCQ', '칫솔은 몇 개월마다 교체해야 할까요?', '3개월', '정답: 3개월마다 교체 권장.', '["1개월","3개월","6개월","1년"]'),
                                                                                                          (5, 4, 'quiz5.png', 'OX', '치실은 양치 후에 사용하는 것이 좋다.', 'O', '정답: 치실은 양치 후 사용 권장.', '["O","X"]');

-- 8. member_quiz
INSERT INTO member_quiz (quiz_answer_id, parent_id, member_id, quiz_question_id, selected_answer, is_correct) VALUES
                                                                                                                  (1, 1, 101, 1, 'O', 1),
                                                                                                                  (2, 1, 102, 2, '불소', 1),
                                                                                                                  (3, 2, 103, 3, 'O', 1),
                                                                                                                  (4, 3, 104, 4, '3개월', 1),
                                                                                                                  (5, 4, 105, 5, 'O', 1);

-- 9. mission
INSERT INTO mission (mission_id, step, point, created_at) VALUES
                                                              (1, 1, 15, '2024-07-01 09:00:00'),
                                                              (2, 2, 5, '2024-07-02 09:00:00'),
                                                              (3, 3, 15, '2024-07-03 09:00:00'),
                                                              (4, 4, 10, '2024-07-04 09:00:00'),
                                                              (5, 5, 5, '2024-07-05 09:00:00');

-- 10. user_mission
INSERT INTO user_mission (user_mission_id, member_id, parent_id, mission_id, status, target_date, reward_at) VALUES
                                                                                                                 (1, 101, 1, 1, 'COMPLETED', '2024-07-07', '2024-07-07 20:00:00'),
                                                                                                                 (2, 102, 1, 2, 'WAITING', '2024-07-08', NULL),
                                                                                                                 (3, 103, 2, 3, 'REWARDED', '2024-07-09', '2024-07-09 21:00:00'),
                                                                                                                 (4, 104, 3, 4, 'COMPLETED', '2024-07-10', '2024-07-10 20:00:00'),
                                                                                                                 (5, 105, 4, 5, 'WAITING', '2024-07-11', NULL);

-- 11. user_point_history
INSERT INTO user_point_history (user_point_id, member_id, parent_id, amount, type, description, created_at) VALUES
                                                                                                                (1, 101, 1, 10, 'EARN', '퀴즈 정답', '2024-07-07 20:10:00'),
                                                                                                                (2, 102, 1, 20, 'CONSUME', '아이템 구매', '2024-07-08 21:00:00'),
                                                                                                                (3, 103, 2, 15, 'EARN', '미션 성공', '2024-07-09 21:10:00'),
                                                                                                                (4, 104, 3, 30, 'CONSUME', '아이템 구매', '2024-07-10 21:00:00'),
                                                                                                                (5, 105, 4, 25, 'EARN', '퀴즈 정답', '2024-07-11 20:30:00');

-- 12. notification_slots
INSERT INTO notification_slots (slot_id, member_id, slot_type, send_time, next_send_at, is_enabled, title, message, updated_at) VALUES
                                                                                                                                    (1, 101, 'MORNING', '08:00:00', '2024-07-08 08:00:00', TRUE, '아침 양치 알림', '아침에 양치하세요!', '2024-07-07 08:00:00'),
                                                                                                                                    (2, 102, 'EVENING', '20:00:00', '2024-07-08 20:00:00', TRUE, '저녁 양치 알림', '저녁에 양치하세요!', '2024-07-07 20:00:00'),
                                                                                                                                    (3, 103, 'LUNCH', '12:00:00', '2024-07-09 12:00:00', TRUE, '점심 양치 알림', '점심에 양치하세요!', '2024-07-08 12:00:00'),
                                                                                                                                    (4, 104, 'MORNING', '08:00:00', '2024-07-10 08:00:00', TRUE, '아침 양치 알림', '아침에 양치하세요!', '2024-07-09 08:00:00'),
                                                                                                                                    (5, 105, 'EVENING', '20:00:00', '2024-07-11 20:00:00', TRUE, '저녁 양치 알림', '저녁에 양치하세요!', '2024-07-10 20:00:00');

-- 13. fcm_tokens
INSERT INTO fcm_tokens (fcm_token_id, member_id, fcm_token, created_at, updated_at) VALUES
                                                                                                       (1, 101, 'token_abc123',  '2024-07-01 10:00:00', '2024-07-01 10:00:00'),
                                                                                                       (2, 102, 'token_def456', '2024-07-02 11:00:00', '2024-07-02 11:00:00'),
                                                                                                       (3, 103, 'token_ghi789', '2024-07-03 12:00:00', '2024-07-03 12:00:00'),
                                                                                                       (4, 104, 'token_jkl012',  '2024-07-04 13:00:00', '2024-07-04 13:00:00'),
                                                                                                       (5, 105, 'token_mno345',  '2024-07-05 14:00:00', '2024-07-05 14:00:00');
