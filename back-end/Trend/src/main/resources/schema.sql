DROP
DATABASE IF EXISTS `trend`;
CREATE
DATABASE IF NOT EXISTS `trend`;
USE
`trend`;

-- ----------------------------------- CREATE TABLES -----------------------------------

CREATE TABLE `user`
(
    `user_id`             varchar(50) NOT NULL PRIMARY KEY,
    `user_password`       varchar(128) NULL,
    `user_nickname`       varchar(50) NULL,
    `user_address`        varchar(100) NULL,
    `user_email`          varchar(50) NULL,
    `user_phone_number`   varchar(50) NULL,
    `user_profile_img`    varchar(100) NULL,
    `user_introduction`   varchar(300) NULL,
    `user_registed_date`  timestamp NULL,
    `user_activity_score` double NULL,
    `user_rating` double NULL
);

CREATE TABLE `refresh_token`
(
    `token_id`        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`         varchar(50) NOT NULL,
    `refresh_token`   varchar(255) NULL,
    `experation_date` timestamp NULL,
    `created_at`      timestamp NULL
);

CREATE TABLE `item`
(
    `item_id`                     int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`                     varchar(50) NOT NULL,
    `item_name`                   varchar(50) NULL,
    `item_category`               varchar(100) NULL,
    `item_price`                  int NULL,
    `item_address`                varchar(100) NULL,
    `item_latitude` double NULL,
    `item_longitude` double NULL,
    `item_content`                text NULL,
    `available_rental_start_date` timestamp NULL,
    `available_rental_end_date`   timestamp NULL,
    `item_images`                 varchar(500) NULL	COMMENT '배열을 문자열 방식으로 저장',
    `item_status`                 varchar(50) NULL	COMMENT '공개, 비공개, 대여 중'
);

CREATE TABLE `item_trade`
(
    `trade_id`               int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `item_id`                int         NOT NULL,
    `lessor_id`              varchar(50) NOT NULL,
    `lessee_id`              varchar(50) NOT NULL,
    `trade_price`            int NULL,
    `trade_deposit`          int NULL,
    `payment_account_number` int NULL,
    `rental_start_date`      timestamp NULL,
    `rental_end_date`        timestamp NULL,
    `trade_state`            varchar(10) NULL COMMENT '"대여 전", "대여 중", "반납 완료"',
    `item_state_image`       varchar(500) NULL COMMENT '이미지 배열을 문자열 형태로 저장',
    `payment_status`         varchar(10) NULL,
    `payment_created_at`     timestamp NULL,
    `status_updated_at`      timestamp NULL
);

CREATE TABLE `wishlist`
(
    `wish_id` int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id` varchar(50) NOT NULL,
    `item_id` int         NOT NULL
);

CREATE TABLE `trade_review`
(
    `trade_review_id`      int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `trade_id`             int         NOT NULL,
    `author_user_id`       varchar(50) NOT NULL,
    `lessor_id`            varchar(50) NOT NULL,
    `lessee_id`            varchar(50) NOT NULL,
    `trade_review_content` varchar(500) NULL,
    `review_created_at`    timestamp NULL,
    `trade_review_rating` double NULL
);


CREATE TABLE `course`
(
    `course_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `course_writer_id`  varchar(50) NOT NULL,
    `course_title`      varchar(50) NULL,
    `region`            varchar(50) NULL,
    `course_content`    text NULL,
    `course_img`        varchar(500) NULL	COMMENT '이미지 목록-문자열',
    `view_count`        int NULL,
    `course_created_at` timestamp NULL,
    `course_updated_at` timestamp NULL
);

CREATE TABLE `course_comment`
(
    `course_comment_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `course_id`                 int         NOT NULL,
    `comment_writer_id`         varchar(50) NOT NULL,
    `course_comment_content`    varchar(500) NULL,
    `course_comment_created_at` timestamp NULL,
    `course_comment_updated_at` timestamp NULL,
    `parents_comment_id`        int NULL
);

CREATE TABLE `course_like`
(
    `course_like_id` int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `course_id`      int         NOT NULL,
    `user_id`        varchar(50) NOT NULL
);

CREATE TABLE `payment_status_history`
(
    `status_history_id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `status`            varchar(50) NULL	COMMENT '결제대기, 결제완료, 환불대기, 환불완료',
    `changed_at`        timestamp NULL	COMMENT '상태 변경 일자',
    `remark`            varchar(100) NULL	COMMENT '상태 변경 설명 (예: 환불 사유)',
    `trade_id`          int NOT NULL
);

CREATE TABLE `course_spot`
(
    `course_spot_id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `course_id`      int NOT NULL,
    `spot_id`        int NOT NULL,
    `visit_order`    int NULL
);

CREATE TABLE `article`
(
    `article_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `writer_id`          varchar(50) NOT NULL,
    `article_title`      varchar(50) NULL,
    `article_content`    text NULL,
    `article_img`        varchar(500) NULL	COMMENT '이미지 목록-문자열',
    `view_count`         int NULL,
    `article_created_at` timestamp NULL,
    `article_updated_at` timestamp NULL
);

CREATE TABLE `article_comment`
(
    `article_comment_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `article_id`                 int         NOT NULL,
    `comment_writer_id`          varchar(50) NOT NULL,
    `article_comment_content`    varchar(500) NULL,
    `article_comment_created_at` timestamp NULL,
    `article_comment_updated_at` timestamp NULL,
    `parent_comment_id`          int NULL
);

CREATE TABLE `article_like`
(
    `article_like_id` int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`         varchar(50) NOT NULL,
    `article_id`      int         NOT NULL
);

CREATE TABLE `chat_room`
(
    `room_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `item_id`         int         NOT NULL,
    `lessor_id`       varchar(50) NOT NULL,
    `lessee_id`       varchar(50) NOT NULL,
    `room_created_at` timestamp NULL
);

CREATE TABLE `chat_message`
(
    `message_id`        int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `room_id`           int         NOT NULL,
    `sender_id`         varchar(50) NOT NULL,
    `message_content`   text NULL,
    `message_img`       varchar(100) NULL,
    `transmission_time` timestamp NULL
);

CREATE TABLE `spot`
(
    `spot_id`   int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `spot_name` varchar(50) NULL
);

-- ----------------------------------- SET FOREIGN KEY -----------------------------------

ALTER TABLE `item`
    ADD CONSTRAINT `FK_item_TO_user_1` FOREIGN KEY (
                                                    `user_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `item_trade`
    ADD CONSTRAINT `FK_item_TO_item_trade_1` FOREIGN KEY (
                                                          `item_id`
        )
        REFERENCES `item` (
                           `item_id`
            );

ALTER TABLE `item_trade`
    ADD CONSTRAINT `FK_user_TO_item_trade_1` FOREIGN KEY (
                                                          `lessor_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `item_trade`
    ADD CONSTRAINT `FK_user_TO_item_trade_2` FOREIGN KEY (
                                                          `lessee_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `wishlist`
    ADD CONSTRAINT `FK_user_TO_wishlist_1` FOREIGN KEY (
                                                        `user_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `wishlist`
    ADD CONSTRAINT `FK_item_TO_wishlist_1` FOREIGN KEY (
                                                        `item_id`
        )
        REFERENCES `item` (
                           `item_id`
            );

ALTER TABLE `trade_review`
    ADD CONSTRAINT `FK_item_trade_TO_trade_review_1` FOREIGN KEY (
                                                                  `trade_id`
        )
        REFERENCES `item_trade` (
                                 `trade_id`
            );

ALTER TABLE `trade_review`
    ADD CONSTRAINT `FK_user_TO_trade_review_1` FOREIGN KEY (
                                                            `author_user_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `trade_review`
    ADD CONSTRAINT `FK_user_TO_trade_review_2` FOREIGN KEY (
                                                            `lessor_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `trade_review`
    ADD CONSTRAINT `FK_user_TO_trade_review_3` FOREIGN KEY (
                                                            `lessee_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `course`
    ADD CONSTRAINT `FK_user_TO_course_1` FOREIGN KEY (
                                                      `course_writer_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `course_comment`
    ADD CONSTRAINT `FK_course_TO_course_comment_1` FOREIGN KEY (
                                                                `course_id`
        )
        REFERENCES `course` (
                             `course_id`
            );

ALTER TABLE `course_comment`
    ADD CONSTRAINT `FK_user_TO_course_comment_1` FOREIGN KEY (
                                                              `comment_writer_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `course_like`
    ADD CONSTRAINT `FK_course_TO_course_like_1` FOREIGN KEY (
                                                             `course_id`
        )
        REFERENCES `course` (
                             `course_id`
            );

ALTER TABLE `course_like`
    ADD CONSTRAINT `FK_user_TO_course_like_1` FOREIGN KEY (
                                                           `user_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `payment_status_history`
    ADD CONSTRAINT `FK_item_trade_TO_payment_status_history_1` FOREIGN KEY (
                                                                            `trade_id`
        )
        REFERENCES `item_trade` (
                                 `trade_id`
            );

ALTER TABLE `course_spot`
    ADD CONSTRAINT `FK_course_TO_course_spot_1` FOREIGN KEY (
                                                             `course_id`
        )
        REFERENCES `course` (
                             `course_id`
            );

ALTER TABLE `article`
    ADD CONSTRAINT `FK_user_TO_article_1` FOREIGN KEY (
                                                       `writer_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `article_comment`
    ADD CONSTRAINT `FK_article_TO_article_comment_1` FOREIGN KEY (
                                                                  `article_id`
        )
        REFERENCES `article` (
                              `article_id`
            );

ALTER TABLE `article_comment`
    ADD CONSTRAINT `FK_user_TO_article_comment_1` FOREIGN KEY (
                                                               `comment_writer_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `article_like`
    ADD CONSTRAINT `FK_user_TO_article_like_1` FOREIGN KEY (
                                                            `user_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `article_like`
    ADD CONSTRAINT `FK_article_TO_article_like_1` FOREIGN KEY (
                                                               `article_id`
        )
        REFERENCES `article` (
                              `article_id`
            );

ALTER TABLE `chat_room`
    ADD CONSTRAINT `FK_item_TO_chat_room_1` FOREIGN KEY (
                                                         `item_id`
        )
        REFERENCES `item` (
                           `item_id`
            );

ALTER TABLE `chat_room`
    ADD CONSTRAINT `FK_user_TO_chat_room_1` FOREIGN KEY (
                                                         `lessor_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `chat_room`
    ADD CONSTRAINT `FK_user_TO_chat_room_2` FOREIGN KEY (
                                                         `lessee_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `chat_message`
    ADD CONSTRAINT `FK_chat_room_TO_chat_message_1` FOREIGN KEY (
                                                                 `room_id`
        )
        REFERENCES `chat_room` (
                                `room_id`
            );

ALTER TABLE `chat_message`
    ADD CONSTRAINT `FK_user_TO_chat_message_1` FOREIGN KEY (
                                                            `sender_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `course_spot`
    ADD CONSTRAINT `FK_spot_TO_course_spot_1` FOREIGN KEY (
                                                           `spot_id`
        )
        REFERENCES `spot` (
                           `spot_id`
            );

ALTER TABLE `refresh_token`
    ADD CONSTRAINT `FK_user_TO_refresh_token_1` FOREIGN KEY (
                                                             `user_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

-- ----------------------------------- INSERT DUMMY DATAS INTO TABLES -----------------------------------
-- user TABLE
INSERT INTO `user`
VALUES ("user1", "password1", "구매자", "대한민국 서울시 강남구 테헤란로", "user1@naver.com", "010-1234-5678", "user1_profile.png",
        "안녕하세요 물건 구매자입니다.", "2024-10-30", 20, 3.5),
       ("user2", "password2", "판매자", "대한민국 강원도 강릉시", "user2@gmail.com", "010-4321-8765", "user2_profile.png",
        "안녕하세요 물건 판매자입니다.", "2023-01-24", 60, 6.8),
       ("user3", "password3", "구매 및 판매자", "미국 캘리포니아주", "user3@gmail.com", "0056123312214", "user3_profile.png",
        "Hello Nice to meet you.", "2021-05-23", 52, 5.1);

-- item TABLE
INSERT INTO `item`(user_id, item_name, item_category, item_price, item_address, item_latitude, item_longitude,
                   item_content, available_rental_start_date, available_rental_end_date, item_images, item_status)
VALUES ("user2", '산악 자전거', '스포츠', 15000, '대한민국 서울시 강남구', 37.5172, 127.0473, '산악 트레일에 적합한 튼튼한 자전거',
        '2024-11-15 09:00:00', '2024-11-20 18:00:00', '자전거1.jpg,자전거2.jpg,자전거3.jpg', '공개'),
       ("user2", '캠핑용 텐트', '아웃도어', 25000, '대한민국 부산시 해운대구', 35.1587, 129.1604, '가족 캠핑에 적합한 넉넉한 공간의 텐트',
        '2024-12-01 10:00:00', '2024-12-10 15:00:00', '텐트1.jpg,텐트2.jpg', '대여 중'),
       ("user3", '디지털 카메라', '전자기기', 50000, '미국 뉴욕 맨해튼', 40.7128, -74.0060, '고해상도 줌 렌즈가 포함된 디지털 카메라',
        '2024-11-18 08:00:00', '2024-11-25 20:00:00', '카메라1.jpg,카메라2.jpg,카메라3.jpg', '비공개');

-- trade TABLE
INSERT INTO `item_trade`(item_id, lessor_id, lessee_id, trade_price, trade_deposit, payment_account_number,
                         rental_start_date, rental_end_date, trade_state, item_state_image, payment_status,
                         payment_created_at, status_updated_at)
VALUES (1, 'user2', 'user1', 15000, 5000, 123456789, '2024-11-15 09:00:00', '2024-11-20 18:00:00', '대여 중',
        'item1_status1.jpg,item1_status2.jpg', '입금 완료', '2024-11-10 14:00:00', '2024-11-10 14:30:00'),
       (2, 'user2', 'user3', 25000, 10000, 987654321, '2024-12-01 10:00:00', '2024-12-10 15:00:00', '대여 전',
        'item2_status1.jpg,item2_status2.jpg', '입금 전', '2024-11-25 09:00:00', '2024-11-25 09:30:00'),
       (3, 'user3', 'user1', 50000, 20000, 112233445, '2024-11-18 08:00:00', '2024-11-25 20:00:00', '반납 완료',
        'item3_status1.jpg,item3_status2.jpg', '입금 완료', '2024-11-20 08:00:00', '2024-11-20 08:30:00');

-- course TABLE
INSERT INTO `course`(course_writer_id, course_title, region, course_content, course_img, view_count, course_created_at,
                     course_updated_at)
VALUES ('user1', '서울 문화 탐방', '대한민국 서울시', '경복궁, 북촌 한옥마을, 인사동을 중심으로 한 전통 문화 체험 코스입니다.', 'seoul1.jpg,seoul2.jpg', 120,
        '2024-11-10 09:00:00', '2024-11-10 09:00:00'),
       ('user2', '부산 해변 산책', '대한민국 부산시', '해운대, 광안리 해수욕장을 중심으로 한 부산 해변 산책 코스입니다.', 'busan1.jpg,busan2.jpg', 95,
        '2024-11-12 08:00:00', '2024-11-12 08:00:00'),
       ('user3', '뉴욕 미술관 투어', '미국 뉴욕', '메트로폴리탄 미술관, 현대 미술관 등을 포함한 뉴욕 미술관 투어 코스입니다.', 'ny1.jpg,ny2.jpg', 150,
        '2024-11-15 10:00:00', '2024-11-15 10:00:00');

-- ----------------------------------- CHECK DATAS IN TABLE -----------------------------------
SELECT *
FROM `user`;
SELECT *
FROM `item`;
SELECT *
FROM `item_trade`;
SELECT *
FROM `course`;