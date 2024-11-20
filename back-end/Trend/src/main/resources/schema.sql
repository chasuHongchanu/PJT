-- -----------------------------------------------------
-- Schema trend
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `trend`;

-- -----------------------------------------------------
-- Schema trend
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trend` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `trend`;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`             VARCHAR(50)  NOT NULL,
    `user_password`       VARCHAR(128) NULL DEFAULT NULL,
    `user_nickname`       VARCHAR(50)  NULL DEFAULT NULL,
    `user_address`        VARCHAR(100) NULL DEFAULT NULL,
    `user_email`          VARCHAR(50)  NULL DEFAULT NULL,
    `user_phone_number`   VARCHAR(50)  NULL DEFAULT NULL,
    `user_profile_img`    VARCHAR(100) NULL DEFAULT NULL,
    `user_introduction`   VARCHAR(300) NULL DEFAULT NULL,
    `user_activity_score` DOUBLE       NULL DEFAULT NULL,
    `user_rating`         DOUBLE       NULL DEFAULT NULL,
    `country`             VARCHAR(30)  NULL DEFAULT NULL,
    `user_created_at`     TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    `user_deleted_at`     TIMESTAMP    NULL DEFAULT NULL,
    PRIMARY KEY (`user_id`)
    );


-- -----------------------------------------------------
-- Table `article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article`
(
    `article_id`         INT          NOT NULL AUTO_INCREMENT,
    `writer_id`          VARCHAR(50)  NOT NULL,
    `article_title`      VARCHAR(50)  NULL DEFAULT NULL,
    `article_content`    TEXT         NULL DEFAULT NULL,
    `thumbnail`          VARCHAR(100) NULL DEFAULT NULL,
    `view_count`         INT          NULL DEFAULT NULL,
    `article_created_at` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_id`),
    CONSTRAINT `FK_user_TO_article_1`
    FOREIGN KEY (`writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );

-- -----------------------------------------------------
-- Table `article_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_comment`
(
    `article_comment_id`         INT          NOT NULL AUTO_INCREMENT,
    `article_id`                 INT          NOT NULL,
    `comment_writer_id`          VARCHAR(50)  NOT NULL,
    `parent_comment_id`          INT          NULL DEFAULT NULL,
    `article_comment_content`    VARCHAR(500) NULL DEFAULT NULL,
    `article_comment_created_at` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_comment_id`),
    CONSTRAINT `FK_article_TO_article_comment_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `article` (`article_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_article_comment_1`
    FOREIGN KEY (`comment_writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `article_comment_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_comment_like`
(
    `article_comment_id` INT         NOT NULL,
    `user_id`            VARCHAR(50) NOT NULL,
    PRIMARY KEY (`article_comment_id`, `user_id`),
    CONSTRAINT `FK_article_comment_TO_like`
    FOREIGN KEY (`article_comment_id`)
    REFERENCES `article_comment` (`article_comment_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
    CONSTRAINT `FK_user_TO_like`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT
    );


-- -----------------------------------------------------
-- Table `course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course`
(
    `course_id`         INT          NOT NULL AUTO_INCREMENT,
    `course_writer_id`  VARCHAR(50)  NOT NULL,
    `course_title`      VARCHAR(50)  NULL DEFAULT NULL,
    `course_content`    TEXT         NULL DEFAULT NULL,
    `address`           VARCHAR(50)  NULL DEFAULT NULL,
    `thumbnail`         VARCHAR(100) NULL DEFAULT NULL,
    `country`           VARCHAR(30)  NULL DEFAULT NULL,
    `view_count`        INT          NULL DEFAULT NULL,
    `course_created_at` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`course_id`),
    CONSTRAINT `FK_user_TO_course_1`
    FOREIGN KEY (`course_writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );

-- -----------------------------------------------------
-- Table `course_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_comment`
(
    `course_comment_id`         INT          NOT NULL AUTO_INCREMENT,
    `course_id`                 INT          NOT NULL,
    `comment_writer_id`         VARCHAR(50)  NOT NULL,
    `parents_comment_id`        INT          NULL DEFAULT NULL,
    `course_comment_content`    VARCHAR(500) NULL DEFAULT NULL,
    `course_comment_created_at` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`course_comment_id`),
    CONSTRAINT `FK_course_TO_course_comment_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_course_comment_1`
    FOREIGN KEY (`comment_writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `course_comment_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_comment_like`
(
    `course_comment_id` INT         NOT NULL,
    `user_id`           VARCHAR(50) NOT NULL,
    PRIMARY KEY (`course_comment_id`, `user_id`),
    CONSTRAINT `FK_course_comment_TO_course_comment_like`
    FOREIGN KEY (`course_comment_id`)
    REFERENCES `course_comment` (`course_comment_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT,
    CONSTRAINT `FK_user_TO_course_comment_like`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT
    );

-- -----------------------------------------------------
-- Table `article_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_image`
(
    `article_img_id` INT          NOT NULL AUTO_INCREMENT,
    `article_id`     INT          NOT NULL,
    `article_image`  VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (`article_img_id`),
    CONSTRAINT `FK_article_TO_article_image_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `article` (`article_id`)
    ON DELETE CASCADE
    );

-- -----------------------------------------------------
-- Table `article_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_like`
(
    `user_id`    VARCHAR(50) NOT NULL,
    `article_id` INT         NOT NULL,
    PRIMARY KEY (`user_id`, `article_id`),
    CONSTRAINT `FK_article_TO_article_like_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `article` (`article_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_article_like_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item`
(
    `item_id`                     INT         NOT NULL AUTO_INCREMENT,
    `user_id`                     VARCHAR(50) NOT NULL,
    `item_name`                   VARCHAR(50) NULL DEFAULT NULL,
    `main_category`               VARCHAR(30) NULL DEFAULT NULL,
    `sub_category`                VARCHAR(30) NULL DEFAULT NULL,
    `sub_subcategory`             VARCHAR(30) NULL DEFAULT NULL,
    `item_price`                  INT         NULL DEFAULT NULL,
    `country`                     VARCHAR(30) NULL DEFAULT NULL,
    `province`                    VARCHAR(30) NULL DEFAULT NULL,
    `district`                    VARCHAR(30) NULL DEFAULT NULL,
    `town`                        VARCHAR(30) NULL DEFAULT NULL,
    `item_latitude`               DOUBLE      NULL DEFAULT NULL,
    `item_longitude`              DOUBLE      NULL DEFAULT NULL,
    `item_content`                TEXT        NULL DEFAULT NULL,
    `available_rental_start_date` TIMESTAMP   NULL DEFAULT NULL,
    `available_rental_end_date`   TIMESTAMP   NULL DEFAULT NULL,
    `item_status`                 VARCHAR(50) NULL DEFAULT '대여 가능' COMMENT '대여 가능, 예약 중, 대여 중',
    `view_count`                  INT         NULL DEFAULT NULL,
    `item_created_at`             TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP,
    `item_deleted_at`             TIMESTAMP   NULL DEFAULT NULL,
    `thumbnail`                   VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (`item_id`),
    CONSTRAINT `FK_user_TO_item_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `chat_room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_room`
(
    `room_id`         INT         NOT NULL AUTO_INCREMENT,
    `item_id`         INT         NOT NULL,
    `lessor_id`       VARCHAR(50) NOT NULL,
    `lessee_id`       VARCHAR(50) NOT NULL,
    `room_created_at` TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`room_id`),
    CONSTRAINT `FK_item_TO_chat_room_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`),
    CONSTRAINT `FK_user_TO_chat_room_1`
    FOREIGN KEY (`lessor_id`)
    REFERENCES `user` (`user_id`),
    CONSTRAINT `FK_user_TO_chat_room_2`
    FOREIGN KEY (`lessee_id`)
    REFERENCES `user` (`user_id`)
    );


-- -----------------------------------------------------
-- Table `chat_message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_message`
(
    `message_id`      INT          NOT NULL AUTO_INCREMENT,
    `room_id`         INT          NOT NULL,
    `sender_id`       VARCHAR(50)  NOT NULL,
    `message_content` TEXT         NULL DEFAULT NULL,
    `message_img`     VARCHAR(100) NULL DEFAULT NULL,
    `chat_created_at` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`message_id`),
    CONSTRAINT `FK_chat_room_TO_chat_message_1`
    FOREIGN KEY (`room_id`)
    REFERENCES `chat_room` (`room_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_chat_message_1`
    FOREIGN KEY (`sender_id`)
    REFERENCES `user` (`user_id`)
    );


-- -----------------------------------------------------
-- Table `course_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_image`
(
    `course_img_id` INT          NOT NULL AUTO_INCREMENT,
    `course_id`     INT          NOT NULL,
    `course_img`    VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (`course_img_id`),
    CONSTRAINT `FK_course_TO_course_image_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `course_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_like`
(
    `course_id` INT         NOT NULL,
    `user_id`   VARCHAR(50) NOT NULL,
    PRIMARY KEY (`course_id`, `user_id`),
    CONSTRAINT `FK_course_TO_course_like_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_course_like_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );



-- -----------------------------------------------------
-- Table `course_spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_spot`
(
    `course_spot_id` INT         NOT NULL AUTO_INCREMENT,
    `course_id`      INT         NOT NULL,
    `visit_order`    int         NULL,
    `spot_name`      varchar(30) NULL,
    `address`        varchar(50) NULL,
    `latitude`       double      NULL,
    `longitude`      double      NULL,
    PRIMARY KEY (`course_spot_id`),
    CONSTRAINT `FK_course_TO_course_spot_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `item_trade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item_trade`
(
    `trade_id`               INT         NOT NULL AUTO_INCREMENT,
    `item_id`                INT         NOT NULL,
    `lessor_id`              VARCHAR(50) NOT NULL,
    `lessee_id`              VARCHAR(50) NOT NULL,
    `trade_price`            INT         NULL DEFAULT NULL,
    `trade_deposit`          INT         NULL DEFAULT NULL,
    `payment_account_number` INT         NULL DEFAULT NULL,
    `rental_start_date`      TIMESTAMP   NULL DEFAULT NULL,
    `rental_end_date`        TIMESTAMP   NULL DEFAULT NULL,
    `trade_state`            VARCHAR(10) NULL DEFAULT '대여 전' COMMENT '\"대여 전\", \"대여 중\", \"반납 완료\"',
    `payment_status`         VARCHAR(10) NULL DEFAULT '입금 전' COMMENT '\"입금 전\", \"입금 완료\"',
    `status_updated_at`      TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `payment_created_at`     TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`trade_id`),
    CONSTRAINT `FK_item_TO_item_trade_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`),
    CONSTRAINT `FK_user_TO_item_trade_1`
    FOREIGN KEY (`lessor_id`)
    REFERENCES `user` (`user_id`),
    CONSTRAINT `FK_user_TO_item_trade_2`
    FOREIGN KEY (`lessee_id`)
    REFERENCES `user` (`user_id`)
    );


-- -----------------------------------------------------
-- Table `item_condition_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item_condition_image`
(
    `condition_image_id` INT          NOT NULL AUTO_INCREMENT,
    `trade_id`           INT          NOT NULL,
    `condition_img`      VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (`condition_image_id`),
    CONSTRAINT `FK_item_trade_TO_item_condition_image_1`
    FOREIGN KEY (`trade_id`)
    REFERENCES `item_trade` (`trade_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `item_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item_image`
(
    `item_img_id` INT         NOT NULL AUTO_INCREMENT,
    `item_id`     INT         NOT NULL,
    `item_img`    VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (`item_img_id`),
    CONSTRAINT `FK_item_TO_item_image_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `payment_status_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `payment_status_history`
(
    `status_history_id` INT          NOT NULL AUTO_INCREMENT,
    `status`            VARCHAR(50)  NULL DEFAULT NULL COMMENT '결제대기, 결제완료, 환불대기, 환불완료',
    `changed_at`        TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '상태 변경 일자',
    `remark`            VARCHAR(100) NULL DEFAULT NULL COMMENT '상태 변경 설명 (예: 환불 사유)',
    `trade_id`          INT          NOT NULL,
    PRIMARY KEY (`status_history_id`),
    CONSTRAINT `FK_item_trade_TO_payment_status_history_1`
    FOREIGN KEY (`trade_id`)
    REFERENCES `item_trade` (`trade_id`)
                                                                    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `refresh_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `refresh_token`
(
    `token_id`      INT          NOT NULL AUTO_INCREMENT,
    `user_id`       VARCHAR(50)  NOT NULL,
    `refresh_token` VARCHAR(255) NULL DEFAULT NULL,
    `expires_at`    TIMESTAMP    NULL DEFAULT NULL,
    `created_at`    TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`token_id`),
    CONSTRAINT `FK_user_TO_refresh_token_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    );


-- -----------------------------------------------------
-- Table `trade_review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trade_review`
(
    `trade_review_id`      INT          NOT NULL AUTO_INCREMENT,
    `trade_id`             INT          NOT NULL,
    `author_user_id`       VARCHAR(50)  NOT NULL,
    `lessor_id`            VARCHAR(50)  NOT NULL,
    `lessee_id`            VARCHAR(50)  NOT NULL,
    `trade_review_content` VARCHAR(500) NULL DEFAULT NULL,
    `trade_review_rating`  DOUBLE       NULL DEFAULT NULL,
    `review_created_at`    TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`trade_review_id`),
    CONSTRAINT `FK_item_trade_TO_trade_review_1`
    FOREIGN KEY (`trade_id`)
    REFERENCES `item_trade` (`trade_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_trade_review_1`
    FOREIGN KEY (`author_user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_trade_review_2`
    FOREIGN KEY (`lessor_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_trade_review_3`
    FOREIGN KEY (`lessee_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `user_chatroom`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_chatroom`
(
    `user_chat_id` INT         NOT NULL AUTO_INCREMENT,
    `room_id`      INT         NOT NULL,
    `user_id`      VARCHAR(50) NOT NULL,
    `deleted_at`   TIMESTAMP   NULL DEFAULT NULL,
    PRIMARY KEY (`user_chat_id`),
    CONSTRAINT `FK_chat_room_TO_user_chatroom_1`
    FOREIGN KEY (`room_id`)
    REFERENCES `chat_room` (`room_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_user_chatroom_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );


-- -----------------------------------------------------
-- Table `wishlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wishlist`
(
    `wish_id` INT         NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(50) NOT NULL,
    `item_id` INT         NOT NULL,
    PRIMARY KEY (`wish_id`),
    CONSTRAINT `FK_item_TO_wishlist_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_wishlist_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    );

-- ---------------------------------- INSERT DUMMY DATAS INTO TABLES ---------------------------------
use trend;

-- Step 1: Insert data into `user` table
-- 비밀번호는 전부 password1 입력.
INSERT INTO `user` (`user_id`, `user_password`, `user_nickname`, `user_address`, `user_email`, `user_phone_number`, `user_profile_img`, `user_introduction`, `user_activity_score`, `user_rating`, `country`)
VALUES
    ('user1', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Alice', 'Seoul', 'alice@example.com', '010-1111-1111', 'img1.jpg', '안녕하세요! 저는 여행을 좋아합니다.', 95.0, 4.8, 'South Korea'),
    ('user2', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Bob', 'Busan', 'bob@example.com', '010-2222-2222', 'img2.jpg', '바다를 좋아하는 Bob입니다.', 90.0, 4.5, 'South Korea'),
    ('user3', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Charlie', 'Incheon', 'charlie@example.com', '010-3333-3333', 'img3.jpg', '맛집 탐방을 즐기는 Charlie입니다.', 85.0, 4.3, 'South Korea'),
    ('user4', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Dave', 'Daegu', 'dave@example.com', '010-4444-4444', 'img4.jpg', '사진 촬영 전문가 Dave입니다.', 80.0, 4.0, 'South Korea'),
    ('user5', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Eve', 'Daejeon', 'eve@example.com', '010-5555-5555', 'img5.jpg', '산책을 좋아하는 Eve입니다.', 75.0, 3.9, 'South Korea'),
    ('user6', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Frank', 'Gwangju', 'frank@example.com', '010-6666-6666', 'img6.jpg', '박물관을 좋아하는 Frank입니다.', 70.0, 4.1, 'South Korea'),
    ('user7', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Grace', 'Jeju', 'grace@example.com', '010-7777-7777', 'img7.jpg', '제주도를 사랑하는 Grace입니다.', 92.0, 4.7, 'South Korea'),
    ('user8', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Heidi', 'Suwon', 'heidi@example.com', '010-8888-8888', 'img8.jpg', '수원을 기반으로 활동하는 Heidi입니다.', 88.0, 4.4, 'South Korea'),
    ('user9', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Ivan', 'Ulsan', 'ivan@example.com', '010-9999-9999', 'img9.jpg', 'Ulsan Ivan입니다.', 78.0, 4.2, 'South Korea'),
    ('user10', 'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da', 'Judy', 'Sejong', 'judy@example.com', '010-1010-1010', 'img10.jpg', '세종 Judy입니다.', 85.0, 4.5, 'South Korea');

-- Step 2: Insert data into `item` table
INSERT INTO `item` (`user_id`, `item_name`, `main_category`, `sub_category`, `sub_subcategory`, `item_price`, `country`,
                    `province`, `district`, `town`, `item_latitude`, `item_longitude`, `item_content`,
                    `available_rental_start_date`, `available_rental_end_date`, `item_status`)
VALUES ('user1', 'item1', 'Electronics', 'Camera', 'Digital', 10000, '대한민국', 'Seoul', 'Gangnam', 'Apgujeong', 37.5272,
        127.0276,
        'Item 1 description', '2023-01-01', '2023-01-10', '공개'),
       ('user2', 'item2', 'Sports', 'Bike', 'Mountain', 15000, '대한민국', 'Incheon', 'Bupyeong', 'Bupyeong Market',
        37.5074,
        126.7218, 'Item 2 description', '2023-02-01', '2023-02-10', '공개'),
       ('user3', 'item3', 'Fashion', 'Clothes', 'Jacket', 8000, '대한민국', 'Busan', 'Haeundae', 'Haeundae Beach', 35.1587,
        129.1604, 'Item 3 description', '2023-03-01', '2023-03-10', '대여 중');

-- Step 3: Insert data into `item_trade` table
INSERT INTO `item_trade` (`item_id`, `lessor_id`, `lessee_id`, `trade_price`, `trade_deposit`, `payment_account_number`,
                          `rental_start_date`, `rental_end_date`, `trade_state`, `payment_status`)
VALUES (1, 'user1', 'user2', 12000, 5000, 123456, '2023-01-02', '2023-01-08', '대여 중', '입금 완료'),
       (2, 'user2', 'user3', 13000, 6000, 234567, '2023-02-02', '2023-02-08', '대여 전', '입금 전'),
       (3, 'user3', 'user1', 9000, 3000, 345678, '2023-03-02', '2023-03-08', '반납 완료', '입금 완료');

-- Step 4: Insert data into `wishlist` table
INSERT INTO `wishlist` (`user_id`, `item_id`)
VALUES ('user1', 2),
       ('user2', 3),
       ('user3', 1);

-- Step 5: Insert data into `trade_review` table
INSERT INTO `trade_review` (`trade_id`, `author_user_id`, `lessor_id`, `lessee_id`, `trade_review_content`,
                            `trade_review_rating`)
VALUES (1, 'user2', 'user1', 'user2', '좋은 거래였습니다.', 4.5),
       (2, 'user3', 'user2', 'user3', '만족스러운 거래였습니다.', 4.0),
       (3, 'user1', 'user3', 'user1', '최상의 경험.', 5.0);

-- Step 6: Insert data into `course` table
INSERT INTO `course` (`course_writer_id`, `course_title`, `course_content`, `address`,
                      `view_count`)
VALUES ('user1', 'Seoul Tour', '서울 투어 추천합니다.', 'Seoul Jongno Gwanghwamun', 100),
       ('user2', 'Busan Adventure', '부산 투어 추천합니다.', 'Busan Haeundae Haeundae Beach', 200),
       ('user3', 'Incheon Day Trip', '인천 투어 추천합니다.', 'Incheon Songdo Central Park', 150);


-- Step 7: Insert data into `course_spot` table
INSERT INTO `course_spot` (`course_id`, `visit_order`, `spot_name`, `address`, `latitude`, `longitude`)
VALUES
-- Spots for 'Seoul Tour' (course_id = 1)
(1, 1, 'Gyeongbokgung Palace', '161 Sajik-ro, Jongno-gu, Seoul', 37.579617, 126.977041),
(1, 2, 'Bukchon Hanok Village', '37, Gyedong-gil, Jongno-gu, Seoul', 37.582604, 126.983716),

-- Spots for 'Busan Adventure' (course_id = 2)
(2, 1, 'Haeundae Beach', 'Haeundae-gu, Busan', 35.158698, 129.160384),
(2, 2, 'Gamcheon Culture Village', '203, Gamnae 2-ro, Saha-gu, Busan', 35.097089, 129.010350),

-- Spot for 'Incheon Day Trip' (course_id = 3)
(3, 1, 'Songdo Central Park', '196, Techno park-ro, Yeonsu-gu, Incheon', 37.392567, 126.634631);

-- Step 8: Insert data into `course_comment` table
INSERT INTO `course_comment` (`course_id`, `comment_writer_id`, `course_comment_content`)
VALUES (1, 'user2', 'Great tour!'),
       (2, 'user3', 'Loved it!'),
       (3, 'user1', 'Had a fantastic time.');

-- Step 9: Insert data into `course_like` table
INSERT INTO `course_like` (`course_id`, `user_id`)
VALUES (1, 'user3'),
       (2, 'user1'),
       (3, 'user2');

-- Step 10: Insert data into `payment_status_history` table
INSERT INTO `payment_status_history` (`status`, `remark`, `trade_id`)
VALUES ('결제완료', 'Paid in full', 1),
       ('결제대기', 'Awaiting payment', 2),
       ('환불대기', 'Refund requested', 3);

-- Step 11: Insert data into `article` table
INSERT INTO `article` (`writer_id`, `article_title`, `article_content`, `thumbnail`, `view_count`)
VALUES
    ('user1', 'Seoul Travel Tips', '서울 여행 꿀팁을 공유합니다.', 'thumb1.jpg', 100),
    ('user2', 'Busan Food Tour', '부산의 맛집을 소개합니다.', 'thumb2.jpg', 150),
    ('user3', 'Incheon Hidden Gems', '인천의 숨겨진 명소를 알려드립니다.', 'thumb3.jpg', 80),
    ('user4', 'Daegu Nightlife', '대구의 밤 문화를 경험해보세요.', 'thumb4.jpg', 200),
    ('user5', 'Daejeon Museum Guide', '대전의 박물관을 소개합니다.', 'thumb5.jpg', 70),
    ('user6', 'Gwangju Art Scene', '광주의 예술 문화를 소개합니다.', 'thumb6.jpg', 50),
    ('user7', 'Jeju Hiking Trails', '제주의 등산로를 탐험하세요.', 'thumb7.jpg', 120),
    ('user8', 'Suwon History Walk', '수원의 역사 산책로를 공유합니다.', 'thumb8.jpg', 90),
    ('user9', 'Ulsan Industrial Tour', '울산의 산업 관광을 소개합니다.', 'thumb9.jpg', 60),
    ('user10', 'Sejong City Exploration', '세종시의 명소를 탐험하세요.', 'thumb10.jpg', 110);

-- Step 12: Insert data into `article_comment` table
INSERT INTO `article_comment` (`article_id`, `comment_writer_id`, `parent_comment_id`, `article_comment_content`)
VALUES
    (1, 'user2', NULL, '정말 유용한 정보네요!'),
    (1, 'user3', NULL, '서울 여행 중 방문해야 할 곳이네요.'),
    (2, 'user1', NULL, '부산 여행 갈 때 참고하겠습니다.'),
    (3, 'user4', NULL, '인천 숨겨진 명소 소개 감사합니다.'),
    (4, 'user5', NULL, '대구의 밤 문화 꼭 가보고 싶네요.'),
    (5, 'user6', NULL, '대전 박물관에 대해 잘 알게 되었어요.'),
    (6, 'user7', NULL, '광주 예술 문화도 매력적이네요.'),
    (7, 'user8', NULL, '제주도 등산로 꼭 가볼게요!'),
    (8, 'user9', NULL, '수원의 역사 산책로 멋지네요.'),
    (9, 'user10', NULL, '세종시 탐험할 때 참고하겠습니다.');

-- Step 13: Insert data into `article_like` table
INSERT INTO `article_like` (`user_id`, `article_id`)
VALUES
    ('user2', 1), ('user3', 1), ('user4', 2), ('user5', 3), ('user6', 4),
    ('user7', 5), ('user8', 6), ('user9', 7), ('user10', 8), ('user1', 9);

-- Step 14: Insert data into `chat_room` table
INSERT INTO `chat_room` (`item_id`, `lessor_id`, `lessee_id`)
VALUES (1, 'user1', 'user2'),
       (2, 'user2', 'user3'),
       (3, 'user3', 'user1');

-- ----------------------------------- CHECK DATAS IN TABLE -----------------------------------
SELECT *
FROM `user`;
SELECT *
FROM `item`;
SELECT *
FROM `item_trade`;
SELECT *
FROM `course`;