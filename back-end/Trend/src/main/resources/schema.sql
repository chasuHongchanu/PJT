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
    `article_id`         INT         NOT NULL AUTO_INCREMENT,
    `writer_id`          VARCHAR(50) NOT NULL,
    `article_title`      VARCHAR(50) NULL DEFAULT NULL,
    `article_content`    TEXT        NULL DEFAULT NULL,
    `view_count`         INT         NULL DEFAULT NULL,
    `article_created_at` TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP,
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
    `region`            VARCHAR(100) NULL DEFAULT NULL,
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
    `item_id`                     INT          NOT NULL AUTO_INCREMENT,
    `user_id`                     VARCHAR(50)  NOT NULL,
    `item_name`                   VARCHAR(50)  NULL DEFAULT NULL,
    `main_category`               VARCHAR(30)  NULL DEFAULT NULL,
    `sub_category`                VARCHAR(30)  NULL DEFAULT NULL,
    `sub_subcategory`             VARCHAR(30)  NULL DEFAULT NULL,
    `item_price`                  INT          NULL DEFAULT NULL,
    `address`                     VARCHAR(100) NULL DEFAULT NULL,
    `item_latitude`               DOUBLE       NULL DEFAULT NULL,
    `item_longitude`              DOUBLE       NULL DEFAULT NULL,
    `item_content`                TEXT         NULL DEFAULT NULL,
    `available_rental_start_date` TIMESTAMP    NULL DEFAULT NULL,
    `available_rental_end_date`   TIMESTAMP    NULL DEFAULT NULL,
    `item_status`                 VARCHAR(50)  NULL DEFAULT '대여 가능' COMMENT '대여 가능, 예약 중, 대여 중',
    `view_count`                  INT          NULL DEFAULT 0,
    `item_created_at`             TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    `item_deleted_at`             TIMESTAMP    NULL DEFAULT NULL,
    `thumbnail`                   VARCHAR(50)  NULL DEFAULT NULL,
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
-- Table `spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spot`
(
    `spot_id`      INT          NOT NULL AUTO_INCREMENT,
    `spot_name`    VARCHAR(50)  NULL DEFAULT NULL,
    `spot_address` VARCHAR(100) NULL DEFAULT NULL,
    `latitude`     DOUBLE       NULL DEFAULT NULL,
    `longitude`    DOUBLE       NULL DEFAULT NULL,
    PRIMARY KEY (`spot_id`)
    );


-- -----------------------------------------------------
-- Table `course_spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_spot`
(
    `course_spot_id` INT         NOT NULL AUTO_INCREMENT,
    `course_id`      INT         NOT NULL,
    `spot_id`        INT         NOT NULL,
    `spot_name`      VARCHAR(50) NULL DEFAULT NULL,
    `visit_order`    INT         NULL DEFAULT NULL,
    `spot_address`   VARCHAR(50) NULL DEFAULT NULL,
    `latitude`       DOUBLE      NULL DEFAULT NULL,
    `longitude`      DOUBLE      NULL DEFAULT NULL,
    PRIMARY KEY (`course_spot_id`),
    CONSTRAINT `FK_course_TO_course_spot_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_spot_TO_course_spot_1`
    FOREIGN KEY (`spot_id`)
    REFERENCES `spot` (`spot_id`)
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

-- -----------------------------------------------------
-- Table `ai_chatroom`
-- -----------------------------------------------------

CREATE TABLE `ai_chatroom` (
	`room_id`	int	NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`user_id`	varchar(50)	NOT NULL,
    CONSTRAINT `FK_user_TO_ai_chatroom_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table `ai_chat_message`
-- -----------------------------------------------------

CREATE TABLE `ai_chat_message` (
	`message_id`	int	NOT NULL	PRIMARY KEY AUTO_INCREMENT,
	`room_id`	int	NOT NULL,
	`sender`	varchar(50)	NULL	COMMENT 'userId, TrendGPT',
	`message_content`	varchar(500)	NULL,
	`chat_send_at`	TIMESTAMP	DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `FK_ai_chatroom_TO_ai_chat_message_1`
    FOREIGN KEY(`room_id`)
    REFERENCES `ai_chatroom` (`room_id`)
);

-- ---------------------------------- INSERT DUMMY DATAS INTO TABLES ---------------------------------
use trend;

-- Step 1: Insert data into `user` table
INSERT INTO `user` (`user_id`, `user_password`, `user_nickname`, `user_address`, `user_email`, `user_phone_number`,
                    `user_profile_img`, `user_introduction`, `user_activity_score`, `user_rating`, `country`)
VALUES ('user1',
        'bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da',
        'nickname1', '서울시 강서구 등촌동', 'user1@example.com', '010-1111-1111', 'profile1.jpg',
        'Introduction 1', 10, 4.5, '대한민국'),
       ('user2',
        '92a891f888e79d1c2e8b82663c0f37cc6d61466c508ec62b8132588afe354712b20bb75429aa20aa3ab7cfcc58836c734306b43efd368080a2250831bf7f363f',
        'nickname2', '서울시 강남구 역삼동', 'user2@example.com', '010-2222-2222', 'profile2.jpg',
        'Introduction 2', 20, 4.0, '대한민국'),
       ('user3',
        '2a64d6563d9729493f91bf5b143365c0a7bec4025e1fb0ae67e307a0c3bed1c28cfb259fc6be768ab0a962b1e2c9527c5f21a1090a9b9b2d956487eb97ad4209',
        'nickname3', '인천시 연수구 송도동', 'user3@example.com', '010-3333-3333', 'profile3.jpg',
        'Introduction 3', 15, 3.8, '미쿡');

-- Step 2: Insert data into `item` table
INSERT INTO `item` (`user_id`, `item_name`, `main_category`, `sub_category`, `sub_subcategory`, `item_price`, `address`,
                    `item_latitude`, `item_longitude`, `item_content`,
                    `available_rental_start_date`, `available_rental_end_date`, `item_status`)
VALUES ('user1', 'item1', 'Electronics', 'Camera', 'Digital', 10000, '대한민국 Seoul Gangnam Apgujeong', 37.5272,
        127.0276,
        'Item 1 description', '2023-01-01', '2023-01-10', '공개'),
       ('user2', 'item2', 'Sports', 'Bike', 'Mountain', 15000, '대한민국 Incheon Bupyeong Bupyeong Market',
        37.5074,
        126.7218, 'Item 2 description', '2023-02-01', '2023-02-10', '공개'),
       ('user3', 'item3', 'Fashion', 'Clothes', 'Jacket', 8000, '대한민국 Busan Haeundae Haeundae Beach', 35.1587,
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

-- Step 6: Insert data into `spot` table
INSERT INTO `spot` (`spot_name`, `spot_address`, `latitude`, `longitude`)
VALUES ('멀티캠퍼스 역삼', '서울 강남구 역삼동', 37.5012767241426, 127.039600248343),
       ('스타벅스 국기원사거리점', '서울 강남구 테헤란로 125 (역삼동)', 37.499603693833, 127.031676800566),
       ('삼성세무서', '서울 강남구 테헤란로 114', 37.4982884559713, 127.030103204318);

-- Step 7: Insert data into `course` table
INSERT INTO `course` (`course_writer_id`, `course_title`, `course_content`, `region`,
                      `view_count`)
VALUES ('user1', 'Seoul Tour', '서울 투어 추천합니다.', 'Seoul Jongno Gwanghwamun', 100),
       ('user2', 'Busan Adventure', '부산 투어 추천합니다.', 'Busan Haeundae Haeundae Beach', 200),
       ('user3', 'Incheon Day Trip', '인천 투어 추천합니다.', 'Incheon Songdo Central Park', 150);

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
INSERT INTO `article` (`writer_id`, `article_title`, `article_content`, `view_count`)
VALUES ('user1', 'My First Post', 'Content of the first post', 10),
       ('user2', 'Seoul Travel Tips', 'Content of Seoul travel tips', 20),
       ('user3', 'Best Cafes in Busan', 'Content about cafes in Busan', 15);

-- Step 12: Insert data into `chat_room` table
INSERT INTO `chat_room` (`item_id`, `lessor_id`, `lessee_id`)
VALUES (1, 'user1', 'user2'),
       (2, 'user2', 'user3'),
       (3, 'user3', 'user1');

INSERT INTO spot (spot_name, address, latitude, longitude) VALUES ('김포국제조각공원', '경기도 김포시 월곶면 용강로13번길 38', 37.72080378, 126.552204), ('아트빌리지(한옥마을)', '경기도 김포시 모담공원로 170', 37.6466321, 126.696212), ('예천포리관광지', '경상북도 예천군 감천면 복골길 150', 36.70015808, 128.514204), ('예천삼강', '경상북도 예천군 풍양면 삼강리길 53-23', 36.56378393, 128.304509), ('서울 원효로 예수성심당', '서울특별시 용산구 원효로19길 49', 37.53420937, 126.9545934), ('구 용산 수위관측소', '서울특별시 용산구 청암동 169-1', 37.533956, 126.947132), ('심원정 왜명강화지처비', '서울특별시 용산구 효창원로8길 28', 37.53342496, 126.95186), ('청암동 부군당', '서울특별시 용산구 원효로 17-11', 37.534803, 126.946373), ('산천동 부군당', '서울특별시 용산구 효창원로15길 7', 37.535662, 126.9531703), ('3.1운동 용산인쇄소 노동자 만세시위지', '서울특별시 용산구 원효로41길 33', 37.53543893, 126.9584156), ('이봉창 의사 집터', '서울특별시 용산구 백범로 275', 37.540313, 126.959653), ('백범김구기념관', '서울특별시 용산구 임정로 26', 37.54427794, 126.959253), ('이봉창 의사 역사울림관', '서울특별시 용산구 백범로 281-9', 37.54010272, 126.960852), ('소촌아트팩토리', '광주광역시 광산구 소촌로85번길 14-9', 35.1526623, 126.7908822), ('별밤미술관 in 첨단', '광주광역시 광산구 첨단중앙로182번길 39', 35.22222807, 126.8442514), ('별밤미술관 in 수완', '광주광역시 광산구 신가동 1051', 35.18611658, 126.8193296), ('별밤미술관 in 운남', '광주광역시 광산구 목련로 156', 35.17144137, 126.8159402), ('으능정이문화의거리', '대전광역시 중구 중앙로 170', 36.32934665, 127.428337), ('유성온천', '대전광역시 유성구 계룡로60', 36.3547298, 127.3375865), ('협재관광지', '제주특별자치도 제주시 한림읍 협재리 2450', 33.39349585, 126.2385969), ('곽지관광지', '제주특별자치도 제주시 애월읍 곽지리 1566', 33.4499564, 126.3041171), ('김녕관광지', '제주특별자치도 제주시 구좌읍 김녕리 497-5', 33.5548971, 126.7654535), ('40계단테마거리', '부산광역시 중구 대청로135번길 13', 35.10397225, 129.0346287), ('BIFF광장', '부산광역시 중구 비프광장로 36', 35.09862035, 129.0287588), ('영도대교', '부산광역시 중구 태종로 8', 35.09725344, 129.035681), ('보수동책방골목', '부산광역시 중구 대청로 57-1', 35.10323904, 129.0263248), ('용두산공원', '부산광역시 중구 용두산길 37-55', 35.1009305, 129.0324434), ('광복로', '부산광역시 중구 광복중앙로 1', 35.09947039, 129.0311659), ('부산영화체험박물관', '부산광역시 중구 대청로126번길 12', 35.10169342, 129.033594), ('작괘천', '울산광역시 울주군 상북면 등억알프스리 18-2', 35.55162452, 129.0953395), ('대운산 내원암 계곡', '울산광역시 울주군 온양읍 대운상대길 382', 35.39913422, 129.2309807), ('간절곶 공원', '울산광역시 울주군 서생면 간절곶해안길 205', 35.3625312, 129.3591079), ('어답산관광지', '강원특별자치도 횡성군 갑천면 어답산로 516', 37.59380861, 128.0575207), ('유현문화관광지', '강원특별자치도 횡성군 서원면 경강로유현1길 30', 37.52903211, 127.8186829), ('웰리힐리파크 관광단지', '강원특별자치도 횡성군 둔내면 고원로 451', 37.49012611, 128.2501389), ('임경대', '경상남도 양산시 원동면 원동로 285', 35.32243455, 128.9778074), ('통도사', '경상남도 양산시 하북면 통도사로 108', 35.47540912, 129.0563672), ('통도환타지아', '경상남도 양산시 하북면 통도7길 68', 35.4999924, 129.0831299), ('홍룡폭포(홍룡사)', '경상남도 양산시 상북면 홍룡로 372', 35.39699967, 129.0864895), ('황산공원', '경상남도 양산시 물금읍 황산공원5길 13', 35.30198745, 128.9909861), ('롯데월드 부산', '부산광역시 기장군 기장읍 동부산관광로 42', 35.19603043, 129.2129711), ('순천만국가정원', '전라남도 순천시 국가정원1호길 47', 34.92888588, 127.4999591), ('순천만습지', '전라남도 순천시 순천만길 513-25(대대동)', 34.88579701, 127.5092833), ('낙안읍성', '전라남도 순천시 낙안면 충민길 30(동내리)', 34.90640107, 127.3418227), ('드라마촬영장', '전라남도 순천시 비례골길 24(조례동)', 34.95810919, 127.5378026), ('뿌리깊은나무박물관', '전라남도 순천시 낙안면 평촌3길 45', 34.9036464, 127.3399827), ('송광사', '전라남도 순천시 송광면 송광사안길 100', 35.00236255, 127.2748706), ('선암사', '전라남도 순천시 승주읍 선암사길 450', 34.99618213, 127.327356), ('울주 대곡리 반구대 암각화', '울산광역시 울주군 언양읍 반구대안길 285', 35.60863087, 129.172957), ('홍주의사총', '충청남도 홍성군 홍성읍 의사로 79', 36.6048297, 126.6708805), ('결성동헌', '충청남도 홍성군 흥남서로 738번길', 36.551381, 126.611921), ('죽성드림세트장', '부산광역시 기장군 기장읍 죽성리 134-7', 35.24113258, 129.2479302), ('박태준기념관', '부산광역시 기장군 장안읍 임랑해안길 1', 35.316877, 129.261226), ('달음산자연휴양림', '부산광역시 기장군 일광읍 화용길 299-106', 35.291977, 129.213303), ('섬진강 망덕포구', '전라남도 광양시 진월면 망덕길 171', 34.96787088, 127.7579265), ('윤동주 유고 보존 정병욱 가옥', '전라남도 광양시 진월면 망덕길 249', 34.97075423, 127.759122), ('광양매화문화관', '전라남도 광양시 다압면 지막1길 21', 35.07665864, 127.7180474), ('구덕운동장', '부산광역시 서구 망양로 57 (씨름체육관 서구 꽃마을로 156번길12)', 35.11659638, 129.0145297), ('임시수도기념관', '부산광역시 서구 임시수도기념로 45', 35.10374876, 129.0175954), ('송도오션파크', '부산광역시 서구 송도해변로 36', 35.0727183, 129.0174477), ('송도용궁구름다리', '부산광역시 서구 암남동 620-53', 35.0619212, 129.021965), ('남이장군 사당', '서울특별시 용산구 효창원로 88-10', 37.536475, 126.958433), ('연복사탑중창비', '서울특별시 용산구 한강대로21나길 7', 37.5297718, 126.9647415), ('서울 구 용산철도병원 본관(현 용산역사박물관)', '서울특별시 용산구 한강대로14길 35-29', 37.52557465, 126.9656016), ('왜고개 순교성지', '서울특별시 용산구 한강대로40길 46', 37.52990239, 126.9719253), ('옛 간조 경성지점 사옥', '서울특별시 용산구 한강대로42길 13', 37.53059231, 126.970508), ('새남터', '서울특별시 용산구 이촌로 71', 37.52587942, 126.956708), ('천주교 순교성지 새남터 성당', '서울특별시 용산구 이촌로 80-8', 37.52512658, 126.9570717), ('이태원 부군당', '서울특별시 용산구 녹사평대로40다길 37', 37.53587802, 126.9897044), ('유관순 열사 추모비', '서울특별시 용산구 녹사평대로40다길 37', 37.53587802, 126.9897044), ('중흥골드스파&리조트', '전라남도 나주시 다도면 나주호로 558-314', 34.94833414, 126.8714774), ('나주영상테마파크', '전라남도 나주시 공산면 덕음로 450', 34.97948996, 126.5997029), ('남파고택', '전라남도 나주시 금성길 13', 35.03051267, 126.7184493), ('배알도수변공원', '전라남도 광양시 태인동 1632-13', 34.959931, 127.7601849), ('광양 김 시식지', '전라남도 광양시 김시식지1길 57-6', 34.94697848, 127.7519933), ('무이예술관', '강원특별자치도 평창군 봉평면 사리평길 233', 37.61527594, 128.348787), ('백룡동굴', '강원특별자치도 평창군 미탄면 문희길 63', 37.27786356, 128.577051), ('중앙공원', '부산광역시 서구 대신공원로 37-18', 35.121591, 129.0161785), ('부산공동어시장', '부산광역시 서구 충무대로 202', 35.08990091, 129.0263675), ('천마산조각공원', '부산광역시 서구 천마산로 209번길 94', 35.08903987, 129.0174786), ('한강 나루터', '서울특별시 용산구 한남동 524-6', 37.530347, 127.007028), ('큰한강 부군당(한남제1부군당)', '서울특별시 용산구 한남대로8길 9-9', 37.531338, 127.0103972), ('작은한강 부군당(한남제2부군당)', '서울특별시 용산구 장문로49길 17-10', 37.529112, 127.0047267), ('이슬람 중앙성원', '서울특별시 용산구 우사단로10길 39', 37.5332513, 126.997615), ('이효석문화예술촌', '강원특별자치도 평창군 봉평면 효석문학길 73-25', 37.61317426, 128.3701239), ('평창돌문화체험관', '강원특별자치도 평창군 평창읍 바위공원길 111', 37.376584, 128.409516), ('장도포대지', '인천광역시 남동구 논현동 111-13', 37.39790186, 126.7382687), ('소래역사관', '인천광역시 남동구 아암대로 1605(논현동)', 37.39816347, 126.7375992), ('인천도호부청사', '인천광역시 미추홀구 매소홀로 589', 37.4393787, 126.6868134), ('신륵사관광지', '경기도 여주시 신륵사길 73', 37.29742583, 127.6599364), ('송도해상케이블카', '부산광역시 서구 송도해변로 171', 35.07664334, 129.0233986), ('송도해수욕장', '부산광역시 서구 송도해변로 100', 35.07682117, 129.0180502), ('암남공원', '부산광역시 서구 암남공원로 185', 35.0616574, 129.0149368), ('구덕문화공원', '부산광역시 서구 꽃마을로 163번길 73', 35.12641369, 129.005742), ('용산공예관', '서울특별시 용산구 이태원로 274', 37.539005, 127.0020755), ('서빙고동 부군당', '서울특별시 용산구 서빙고로59길 3-6', 37.520454, 126.993114), ('동빙고 부군당', '서울특별시 용산구 서빙고로73길 24-27', 37.52214299, 126.996145), ('용산기지 옛 미군장교숙소(현 용산공원)', '서울특별시 용산구 서빙고로 221', 37.52300977, 126.989165), ('김유신 장군 사당', '서울특별시 용산구 서빙고로91길 28-15', 37.523941, 127.000688), ('보광동 무후묘', '서울특별시 용산구 장문로15나길 6', 37.527058, 126.9973443), ('탑산약수온천', '경상북도 의성군 봉양면 도리원2길 88-42', 36.30396195, 128.5892642), ('구곡폭포', '강원특별자치도 춘천시 남산면 강촌구곡길 254', 37.797033, 127.615852), ('춘천호반', '강원특별자치도 춘천시 서면 경춘로 1401-25', 37.825714, 127.659368), ('청평사(춘천)', '강원특별자치도 춘천시 북산면 오봉산길 810', 37.986071, 127.808407), ('신영', '강원특별자치도 춘천시 동산면 종자리로 148-16', 37.747692, 127.725507), ('라비에벨', '강원특별자치도 춘천시 동산면 종자리로 436', 37.741217, 127.75782), ('누리천문대', '경기도 군포시 대야2로 139', 37.33028283, 126.9153444), ('내원사계곡(내원사)', '경상남도 양산시 하북면 내원로 207', 35.44578536, 129.1104508), ('대운산자연휴양림', '경상남도 양산시 탑골길 270', 35.41784975, 129.2088996), ('법기수원지', '경상남도 양산시 동면 법기로 198-13', 35.34825271, 129.1079758), ('양산시립박물관', '경상남도 양산시 북정로 78', 35.35849531, 129.0490358), ('양산타워', '경상남도 양산시 동면 강변로 264', 35.32591877, 129.0240164), ('에덴밸리', '경상남도 양산시 원동면 어실로 1206', 35.42905699, 128.9844664), ('백야 김좌진 장군 생가', '충청남도 홍성군 갈산면 백야로 546번길 12', 36.59786571, 126.5471345), ('한용운선생생가지', '충청남도 홍성군 결성면 만해로 318번길 83', 36.56214158, 126.5529499), ('성삼문 선생 유허지', '충청남도 홍성군 홍북읍 매죽헌길 403-12', 36.64524478, 126.7310363), ('홍주성 역사관', '충청남도 홍성군 홍성읍 아문길 20', 36.60019877, 126.6609204), ('충남보훈관', '충청남도 홍성군 홍성읍 상하천로', 36.602981, 126.660651), ('기독교선교100주년기념관', '전라남도 광양시 진상면 성지로 399', 35.04305027, 127.6604508), ('대천해수욕장', '충청남도 보령시 머드로 123', 36.31554012, 126.5108563), ('무창포해수욕장', '충청남도 보령시 웅천읍 열린바다1길 10', 36.24457737, 126.5365426), ('천지연폭포', '제주특별자치도 서귀포시 남성중로 2-15(서홍동)', 33.2447447, 126.5595511), ('정방폭포', '제주특별자치도 서귀포시 칠십리로214번길 37(동홍동)', 33.24474912, 126.5730486), ('주상절리대', '제주특별자치도 서귀포시 이어도로 36-30(중문동)', 33.23799399, 126.4260174), ('설악한화리조트', '강원특별자치도 속초시 미시령로 2983번길 111', 38.21030036, 128.5287214), ('공릉관광지', '경기도 파주시 조리읍 장곡로 218', 37.74984994, 126.8335561), ('임진각관광지', '경기도 파주시 문산읍 임진각로 177', 37.88953876, 126.7401859), ('천안종합휴양관광지', '충청남도 천안시 동남구 성남면 종합휴양지로 200', 36.75731215, 127.2240961), ('태조산관광지', '충청남도 천안시 동남구 태조산길 261', 36.82207666, 127.1889559), ('고구려대장간마을', '경기도 구리시 우미내길 41', 37.56080787, 127.1109492), ('동구릉', '경기도 구리시 동구릉로 197', 37.6171286, 127.136334), ('당항포관광지', '경상남도 고성군 회화면 당항만로 1116', 35.05322615, 128.3920276), ('남당', '충청남도 홍성군 서부면 남당리 859-2', 36.53886252, 126.4716002), ('장림포구', '부산광역시 사하구 장림로 93번길 72(장림동)', 35.07915955, 128.9513486), ('소요산관광지', '경기도 동두천시 평화로2910번길 145', 37.94652177, 127.0693409), ('헌인릉', '서울특별시 서초구 헌인릉길 36-10', 37.46537744, 127.0837156), ('매헌 윤봉길의사 기념관', '서울특별시 서초구 매헌로 99', 37.4730498, 127.0369771), ('고투몰', '서울특별시 서초구 신반포로 지하200', 37.50622031, 127.0050517), ('세빛섬· 반포대교 달빛무지개분수', '서울특별시 서초구 올림픽대로 2085-14', 37.5124362, 126.9959623), ('고씨굴 관광지', '강원특별자치도 영월군 김삿갓면 영월동로 1111', 37.13117779, 128.5352623), ('성류굴 관광지', '경상북도 울진군 근남면 성류굴로 202', 36.95884825, 129.3794409), ('백암온천 관광지', '경상북도 울진군 온정면 온천로 7', 36.72132775, 129.343467), ('용마폭포공원', '서울특별시 중랑구 용마산로 250-12', 37.57335201, 127.0891138), ('망우리공원', '서울특별시 중랑구 망우로 570', 37.59835362, 127.1148164), ('중랑캠핑숲', '서울특별시 중랑구 망우로87길 110', 37.60495511, 127.1099608), ('옹기테마공원', '서울특별시 중랑구 신내로21길 116', 37.61265372, 127.0885194), ('해인사', '경상남도 합천군 가야면 해인사길 122', 35.801504, 128.0976255), ('대장경테마파크', '경상남도 합천군 가야면 가야산로 1160', 35.76749785, 128.1365362), ('오도산 자연휴양림', '경상남도 합천군 봉산면 오도산휴양로 398', 35.67233319, 128.0553007), ('합천박물관', '경상남도 합천군 쌍책면 황강옥전로 1558', 35.58042271, 128.2829423), ('황매산군립공원', '경상남도 합천군 가회면 황매산공원길 4', 35.48184913, 128.0037495), ('영상테마파크&청와대세트장', '경상남도 합천군 용주면 합천호수로 757', 35.54877253, 128.0728984), ('황계폭포', '경상남도 합천군 용주면 황계2길 30', 35.51135706, 128.071173), ('느랭이골자연리조트', '전라남도 광양시 토끼재길 119-32', 35.06003247, 127.7114041), ('구봉산전망대', '전라남도 광양시 구봉산전망대길 155', 34.93646837, 127.6427008), ('광양항해양공원', '전라남도 광양시 중동 1894', 34.92127751, 127.6939636), ('무지개다리', '전라남도 광양시 금호동 625-1', 34.93679857, 127.715141), ('해오름육교', '전라남도 광양시 마동 1125', 34.93442662, 127.7061606), ('용머리해안', '제주특별자치도 서귀포시 안덕면 사계남로216번길 24-30', 33.23342738, 126.3136956), ('감귤박물관', '제주특별자치도 서귀포시 효돈순환로 441(신효동)', 33.27159304, 126.6074285), ('벽계관광지', '경상남도 의령군 궁류면 벽계로 201', 35.42604967, 128.2080942), ('치산관광지', '경상북도 영천시 신녕면 신암길 17', 36.04693281, 128.712821), ('대가야생활촌', '경상북도 고령군 대가야읍 신남로 81', 35.7118532, 128.269244), ('대가야역사테마관광지', '경상북도 고령군 대가야읍 대가야로 1216', 35.72008263, 128.2596761), ('장성호관광지', '전라남도 장성군 북하면 백양로 591-4', 35.41373254, 126.8508186), ('홍길동테마파크', '전라남도 장성군 황룡면 홍길동로 431', 35.31862071, 126.7265578), ('소래포구', '인천광역시 남동구 포구로 2-9(논현동)', 37.3988308, 126.7403795), ('소래습지생태공원', '인천광역시 남동구 소래로 154번길 77(논현동)', 37.40736572, 126.7463366), ('소래철교', '인천광역시 남동구 포구로 2-9(논현동)', 37.3988308, 126.7403795), ('새남터 순교성지', '서울특별시 용산구 이촌로 80-8', 37.52512658, 126.9570717), ('남산&N서울타워', '서울특별시 용산구 남산공원길 105', 37.55112257, 126.9878678), ('나주시천연염색박물관', '전라남도 나주시 다시면 백호로 379', 34.99403168, 126.6625254), ('나주목문화관', '전라남도 나주시 금성관길 15', 35.03232804, 126.7157031), ('별밤미술관 in 신창', '광주광역시 광산구 왕버들로 331', 35.19467258, 126.8452966), ('별밤미술관 in 선운', '광주광역시 광산구 선운로2번길 40', 35.14604759, 126.7732908), ('한국대나무박물관', '전라남도 담양군 담양읍 죽향문화로 35', 35.30934367, 126.9764088), ('한국가사문학관', '전라남도 담양군 가사문학면 가사문학로 877', 35.18796216, 127.0054858), ('슬로시티삼지내마을', '전라남도 담양군 창평면 돌담길 56-24', 35.23522516, 127.0178498), ('후곡약수터', '강원특별자치도 양구군 동면 약수터길 90-1', 38.16733552, 128.0573744), ('불회사', '전라남도 나주시 다도면 다도로 1224-142', 34.90877918, 126.8234812), ('금성관', '전라남도 나주시 금성관길 8', 35.03335643, 126.7165553), ('도래마을(홍기옥 가옥)', '전라남도 나주시 다도면 동력길 16', 34.9981825, 126.8223995), ('정수루', '전라남도 나주시 금성관길 13-20', 35.03193288, 126.7150148), ('나주향교', '전라남도 나주시 향교길 38', 35.03403137, 126.711466), ('완사천', '전라남도 나주시 완사천길 14', 35.01496092, 126.7128194), ('목사내아', '전라남도 나주시 금성관길 13-8', 35.0320205, 126.7151642), ('영모정', '전라남도 나주시 다시면 회진길 14-8', 34.9973737, 126.6671219), ('남고문', '전라남도 나주시 남내동 2-20', 35.0295791, 126.7204701), ('구)나주역', '전라남도 나주시 죽림길 26', 35.0280742, 126.7262445), ('노안천주교회', '전라남도 나주시 노안면 이슬촌길 108', 35.10432714, 126.6754469), ('문경짚라인', '경상북도 문경시 불정길 174', 36.62285644, 128.1367824), ('문경 종합온천', '경상북도 문경시 문경읍 온천2길 24', 36.72937376, 128.1089547), ('문경 활공랜드', '경상북도 문경시 문경읍 활공장길 80', 36.7362239, 128.1538507), ('불정자연휴양림', '경상북도 문경시 불정길 180', 36.62128207, 128.1365074), ('국사봉랜드', '전라남도 광양시 옥곡면 대치로 433', 35.00642478, 127.6525399), ('노들섬', '서울특별시 용산구 양녕로 445', 37.51766383, 126.9580365), ('예당관광지', '충청남도 예산군 응봉면 예당관광로 161', 36.63801252, 126.799434), ('덕산온천관광단지', '충청남도 예산군 덕산면 온천단지2로 11', 36.69075649, 126.659328), ('한국코미디타운', '경상북도 청도군 이서면 이서로 565', 35.69358927, 128.6863109), ('청도소싸움미디어체험관', '경상북도 청도군 화양읍 남성현로 346', 35.68529236, 128.7242012), ('청도레일바이크', '경상북도 청도군 청도읍 하지길 46-51', 35.58811543, 128.7661723), ('표충사', '경상남도 밀양시 단장면 표충로1338', 35.532294, 128.959045), ('부석사관광지', '경상북도 영주시 부석면 부석사로 298', 36.99435088, 128.6797054), ('절물자연휴양림', '제주특별자치도 제주시 명림로 584(봉개동)', 33.44004881, 126.6252034), ('노루생태관찰원', '제주특별자치도 제주시 명림로 520(봉개동)', 33.4443253, 126.6266934), ('제주별빛누리공원', '제주특별자치도 제주시 선돌목동길 60(오등동)', 33.4445529, 126.5491863), ('금정산', '부산광역시 금정구 범어사로 250', 35.28304264, 129.0676208), ('범어사', '부산광역시 금정구 범어사로 250', 35.28304308, 129.0676212), ('회동수원지', '부산광역시 금정구 오륜대로258', 35.24707332, 129.115753), ('성기동', '전라남도 영암군 군서면 왕인로 440', 34.75653201, 126.6291224), ('마한문화', '전라남도 영암군 시종면 남해당로 65', 34.89539241, 126.5859196), ('영산호관광지', '전라남도 영암군 삼호읍 녹색로 653-11', 34.776135, 126.4577774), ('요트올림픽동산지구', '부산광역시 해운대구 APEC로 30', 35.16578256, 129.1350266), ('동백섬', '부산광역시 해운대구 동백로 116', 35.15234205, 129.1513619), ('해운대해수욕장', '부산광역시 해운대구 해운대해변로 264', 35.15910698, 129.1602838), ('달맞이동산지구', '부산광역시 해운대구 달맞이길 190', 35.15768146, 129.1825429), ('맹종죽테마공원', '경상남도 거제시 하청면 거제북로 700', 34.967708, 128.650512), ('거제박물관', '경상남도 거제시 거제대로 3791(옥포동)', 34.894071, 128.686203), ('거제시농업개발원', '경상남도 거제시 거제면 거제남서로 3577', 34.856464, 128.579187), ('거제요트학교', '경상남도 거제시 일운면 지세포해안로 41', 34.833694, 128.701885), ('씨라인', '경상남도 거제시 덕포5길 32-3', 34.91111602, 128.7102346), ('진남관', '전라남도 여수시 동문로 11', 34.7413383, 127.7365704), ('향일암', '전라남도 여수시 돌산읍 향일암로 60', 34.59379448, 127.8029861), ('진안고원치유의숲', '전북특별자치도 진안군 정천면 봉학로 171-22', 35.87516093, 127.4177407), ('가위박물관', '전북특별자치도 진안군 진안읍 마이산로 258', 35.76592077, 127.4150023), ('영도관광안내센터', '부산광역시 영도구 봉래나루로 79', 35.09454127, 129.0387847), ('오수의견관광지', '전북특별자치도 임실군 오수면 충효로 2096-16', 35.54520338, 127.3326779), ('사선대관광지', '전북특별자치도 임실군 관촌면 사선2길 68-7', 35.672357, 127.2746265), ('송정관광지', '경상남도 남해군 미조면 미송로 483', 34.7231451, 128.0249667), ('서창한옥문화관', '광주광역시 서구 눌재로 420', 35.11523537, 126.8300424), ('청마기념관', '경상남도 거제시 둔덕면 방하2길 6', 34.854887, 128.517123), ('씨월드', '경상남도 거제시 일운면 지세포해안로 15', 34.835848, 128.701478), ('알로에테마파크', '경상남도 거제시 거제면 거제남서로 3937', 34.835716, 128.560282), ('노을이물드는언덕', '경상남도 거제시 사등면 창호리 1475', 34.95450963, 128.5194543), ('가조수협효시공원', '경상남도 거제시 사등면 가조서로 7', 34.929516, 128.525144), ('하회관광지', '경상북도 안동시 풍천면 전서로 206', 36.5489594, 128.5274931), ('안동문화관광지', '경상북도 안동시 관광단지로 346-30', 36.5695076, 128.7786154), ('화순온천', '전라남도 화순군 백아면 옥리길 14-7', 35.1624699, 127.0837781), ('도곡온천', '전라남도 화순군 도곡면 온천2길 44', 35.02470562, 126.903529), ('운주사', '전라남도 화순군 도암면 천태로 91-44', 34.92906333, 126.8825635), ('삼척해수욕장', '강원특별자치도 삼척시 수로부인길 453', 37.47034344, 129.1647024), ('맹방관광지', '강원특별자치도 삼척시 근덕면 상맹방길 30-80', 37.39591618, 129.2207941), ('장호관광지', '강원특별자치도 삼척시 근덕면 삼척로 2154', 37.2868038, 129.3079336), ('초당관광지', '강원특별자치도 삼척시 근덕면 미근로 1629', 37.37650408, 129.2047561), ('보문관광단지', '경상북도 경주시 보문로 446', 35.843673, 129.2869659), ('선바위관광지', '경상북도 영양군 입암면 영양로 883-16', 36.60854407, 129.0820141), ('예술의전당', '서울특별시 서초구 남부순환로 2406', 37.4802402, 127.0142152), ('국립국악원', '서울특별시 서초구 남부순환로 2364', 37.47835156, 127.0090736), ('서울 해병대사령부 초대교회', '서울특별시 용산구 용산동2가 2-18', 37.54303445, 126.982882), ('오륜대한국순교자박물관', '부산광역시 금정구 오륜대로 106-1', 35.24570172, 129.1009016), ('부산대젊음의거리', '부산광역시 금정구 장전온천천로 51', 35.229587, 129.0888177), ('금정산성마을', '부산광역시 금정구 산성로 452', 35.250339, 129.0561338), ('산청전통한방휴양관광지', '경상남도 산청군 금서면 동의보감로555번길 45-6', 35.441066, 127.829361), ('산청중산관광지', '경상남도 산청군 시천면 지리산대로 525', 35.293297, 127.7549317), ('산청금서관광지', '경상남도 산청군 금서면 친환경로 1715', 35.394237, 127.847052), ('미당서정주의 집', '서울특별시 관악구 남부순환로 256 나길 4', 37.4734032, 126.973367), ('강감찬전시관', '서울특별시 관악구 낙성대로 77 낙성대공원내', 37.47197208, 126.959371), ('주왕산 관광지', '경상북도 청송군 주왕산면 주왕산로 494', 36.37813214, 129.108598), ('우수영관광지', '전라남도 해남군 문내면 관광레저로 1', 34.57257374, 126.3101147), ('땅끝관광지', '전라남도 해남군 송지면 땅끝마을길 100', 34.29471348, 126.5250233), ('박차정의사생가', '부산광역시 동래구 명륜로 98번길 129-10(칠산동)', 35.20169645, 129.090397), ('금강공원', '부산광역시 동래구 우장춘로 155 (온천동)', 35.21962513, 129.075758), ('허심청', '부산광역시 동래구 온천장로107번길 32 (온천동)', 35.221157, 129.0826889), ('복천박물관', '부산광역시 동래구 복천로 63(복천동)', 35.20877662, 129.0911741), ('충렬사', '부산광역시 동래구 충렬대로 345', 35.199577, 129.0959922), ('해양자연사박물관', '부산광역시 동래구 우장춘로 175 (온천동)', 35.2219498, 129.075647), ('반월호수', '경기도 군포시 호수로 92', 37.32458238, 126.8899642), ('초막골 생태공원', '경기도 군포시 초막골길 216', 37.35410097, 126.9191417), ('철쭉동산', '경기도 군포시 산본동 1152-14', 37.35543392, 126.924967), ('수리사', '경기도 군포시 속달로 347-181', 37.35443607, 126.8967938), ('물누리체험관', '경기도 군포시 호수로 170-85', 37.32136505, 126.8996775), ('좌천동굴', '부산광역시 동구 고관로185번길 1', 35.13325838, 129.0522683), ('만화카페', '부산광역시 동구 성북로 31', 35.13675143, 129.0496096), ('명란브랜드연구소', '부산광역시 동구 영초윗길 22-1', 35.117117, 129.035016), ('속초해수욕장', '강원특별자치도 속초시 해오름로 186', 38.19014884, 128.6035305), ('척산온천', '강원특별자치도 속초시 관광로 327', 38.19020313, 128.5407019), ('태고사', '충청남도 금산군 진산면 청림동로 440', 36.13440357, 127.3251004), ('개삼터', '충청남도 금산군 남이면 개삼로 101', 36.07533917, 127.4783939), ('대광해수욕장', '전라남도 신안군 임자면 대광해수욕장길 179', 35.10291276, 126.0725117), ('실안관광지', '경상남도 사천시 노을길 138', 34.94398447, 128.0399764), ('변산해수욕장', '전북특별자치도 부안군 변산면 변산로 2086', 35.67999179, 126.5309105), ('위도해수욕장', '전북특별자치도 부안군 위도면 위도로 366', 35.60583098, 126.2853294), ('모항해수욕장', '전북특별자치도 부안군 변산면 모항길 23-1', 35.5831428, 126.5082098), ('광명동굴', '경기도 광명시 가학로 85번길 142', 37.42467857, 126.8634321), ('이바구공작소', '부산광역시 동구 망양로486번길 14-13', 35.11709939, 129.0338233), ('장기려더나눔센터', '부산광역시 동구 영초윗길 48', 35.11853593, 129.0326526), ('이바구충전소', '부산광역시 동구 영초윗길 25', 35.11735028, 129.034686), ('168계단 부대시설', '부산광역시 동구 영초윗길26번길 14', 35.1176995, 129.0353474), ('김민부전망대', '부산광역시 동구 영초윗길26번길 14', 35.1176995, 129.0353474), ('유치환의 우체통', '부산광역시 동구 망양로580번길 2', 35.12209588, 129.0338423), ('소흥관(한중우호센터)', '부산광역시 동구 초량중로 36', 35.11507608, 129.0375502), ('만화체험관', '부산광역시 동구 성북로 42-1', 35.13778041, 129.0497944), ('안용복기념 부산포개항문화관', '부산광역시 동구 증산로 100', 35.13563677, 129.0529467), ('가정역(섬진강출렁다리)', '전라남도 곡성군 오곡면 섬진강로 1465', 35.23018353, 127.3682034), ('도림사오토갬핑리조트', '전라남도 곡성군 곡성읍 도림로 74', 35.26209959, 127.2668616), ('청계동솔바람야영장', '전라남도 곡성군 곡성읍 청계동로 415', 35.31107436, 127.2623887), ('마곡사관광지', '충청남도 공주시 사곡면 운암리 731-2', 36.55617136, 127.0205918), ('공주문화관광지', '충청남도 공주시 웅진동 431', 36.47022166, 127.1117007), ('삽교호관광지', '충청남도 당진시 신평면 삽교천3길 100', 36.88890544, 126.8247541), ('왜목마을관광지', '충청남도 당진시 석문면 왜목길 15-5', 37.04493276, 126.5246154), ('난지섬관광지', '충청남도 당진시 석문면 난지1길 67', 37.047036, 126.4242087), ('마하생태관광지', '강원특별자치도 평창군 미탄면 마하길 42-5', 37.29236873, 128.5412837), ('용평관광단지', '강원특별자치도 평창군 대관령면 올림픽로 715', 37.65094444, 128.7055569), ('알펜시아관광단지', '강원특별자치도 평창군 대관령면 솔봉로 325', 37.65925417, 128.6840107), ('휘닉스파크관광단지', '강원특별자치도 평창군 봉평면 태기로 174', 37.58246341, 128.3268678), ('망상', '강원특별자치도 동해시 동해대로 6270-10', 37.59225327, 129.0896922), ('추암', '강원특별자치도 동해시 촛대바위길 28', 37.477063, 129.159196), ('농월정', '경상남도 함양군 안의면 농월정길 9-35', 35.62464301, 127.7815571), ('황령산관광지', '부산광역시 수영구 황령산로 156', 35.1552432, 129.1029084), ('우장춘기념관', '부산광역시 동래구 우장춘로62번길 7(온천동)', 35.213515, 129.0720181), ('장영실과학동산', '부산광역시 동래구 동래역사관길 18(복천동)', 35.20916308, 129.0896688), ('칠곡양떼목장', '경상북도 칠곡군 지천면 창평로 209-42', 35.984545, 128.48874), ('영산포 역사갤러리', '전라남도 나주시 영산3길 17', 35.00072134, 126.7115039), ('나주복암리고분전시관', '전라남도 나주시 다시면 백호로 287', 34.99608962, 126.6570592), ('일본인근대가옥', '전라남도 나주시 예향로 3869-4', 34.99946578, 126.7131249), ('빛가람호수공원전망대', '전라남도 나주시 호수로 77', 35.01683337, 126.7904361), ('에버랜드', '경기도 용인시 처인구 포곡읍 에버랜드로 199', 37.2940667, 127.202128), ('한국민속촌', '경기도 용인시 기흥구 민속촌로 90', 37.25913189, 127.1225431), ('장흥관광지', '경기도 양주시 장흥면 권율로 193', 37.73281632, 126.9492504), ('불갑사관광지', '전라남도 영광군 불갑면 불갑사로 450', 35.199675, 126.550047), ('곡성 섬진강 기차마을', '전라남도 곡성군 오곡면 기차마을로 232', 35.27753157, 127.3082867), ('기차마을 장미공원', '전라남도 곡성군 오곡면 기차마을로 232', 35.27753157, 127.3082867), ('가산산성', '경상북도 칠곡군 동명면 남원리 32-78', 36.021011, 128.590851), ('가산산성야영장', '경상북도 칠곡군 동명면 한티로 1034', 36.016197, 128.618108), ('구상문학관', '경상북도 칠곡군 왜관읍 구상길 191', 35.983943, 128.394568), ('다부동전적기념관', '경상북도 칠곡군 가산면 호국로 1486', 36.046443, 128.518875), ('송정자연휴양림', '경상북도 칠곡군 석적읍 반계3길 88', 36.023332, 128.441641), ('칠곡보오토캠핑장', '경상북도 칠곡군 약목면 강변서로 110-43', 36.020591, 128.394566), ('칠곡호국평화기념관', '경상북도 칠곡군 석적읍 강변대로 1580', 36.017918, 128.404032), ('국립나주박물관', '전라남도 나주시 반남면 고분로 747', 34.91485383, 126.6589014), ('해달별천문대', '전라남도 광양시 봉강면 하조길 91-20', 35.07478024, 127.5540516), ('백운산자연휴양림', '전라남도 광양시 옥룡면 백계로 33', 35.05645795, 127.5976491), ('광양 옥룡사지', '전라남도 광양시 옥룡면 추산리 산 35-1', 35.04701721, 127.6085305), ('청도자전거공원', '경상북도 청도군 청도읍 하지길 46-51', 35.5881145, 128.7661742), ('청도신화랑풍류마을', '경상북도 청도군 운문면 신화랑길 1', 35.71750257, 128.9207343), ('새마을운동발상지기념공원', '경상북도 청도군 청도읍 새마을1길 34', 35.60012971, 128.7661487), ('청도소싸움경기장', '경상북도 청도군 화양읍 남성현로 348', 35.6867346, 128.7251449), ('와인터널', '경상북도 청도군 화양읍 송금길 100', 35.71513567, 128.7205341), ('프로방스 포토랜드', '경상북도 청도군 화양읍 이슬미로 272-28', 35.68397476, 128.7187441), ('경천대관광지', '경상북도 상주시 사벌국면 경천로 652', 36.45722382, 128.2478641), ('회상나루관광지', '경상북도 상주시 중동면 갱다불길 97', 36.44187503, 128.2657793), ('쉬자파크', '경기도 양평군 양평읍 쉬자파크길 193', 37.51122354, 127.5331978), ('주문진해변관광지', '강원특별자치도 강릉시 주문진읍 주문북로 222-30', 37.91144947, 128.8177557), ('연곡해변관광지', '강원특별자치도 강릉시 연곡면 해안로 1282', 37.85990446, 128.8520583), ('등명해변관광지', '강원특별자치도 강릉시 강동면 정동등명길 2', 37.70232906, 129.0161926), ('대관령어흘리관광지', '강원특별자치도 강릉시 성산면 삼포암길 17', 37.719113, 128.7960845), ('무극전적국민관광지', '충청북도 음성군 음성읍 생음대로 594', 36.95063812, 127.6470132), ('통일전망대', '강원특별자치도 고성군 현내면 금강산로 481', 38.51438609, 128.416703), ('화진포역사안보전시관', '강원특별자치도 고성군 거진읍 화진포길 280', 38.47250515, 128.4430602), ('유니온파크', '경기도 하남시 미사대로 710', 37.54654783, 127.2205032), ('광주향교', '경기도 하남시 대성로 126-13', 37.52207622, 127.1984175), ('이성산성', '경기도 하남시 춘궁동 산 36', 37.52530687, 127.1847777), ('미사경정공원', '경기도 하남시 미사대로 505', 37.55330797, 127.2131671), ('자갈치시장', '부산광역시 중구 자갈치해안로 52', 35.09661444, 129.0305818), ('운조루', '전라남도 구례군 토지면 운조루길 59', 35.20594498, 127.5152886), ('한국압화박물관', '전라남도 구례군 구례읍 동산1길 29', 35.19664998, 127.4599106), ('지리산역사문화관', '전라남도 구례군 마산면 화엄사로 377-36', 35.24408524, 127.4837599), ('섬진강어류생태관', '전라남도 구례군 간전면 간전중앙로 47', 35.18745224, 127.5465226), ('청도박물관', '경상북도 청도군 이서면 이서로 567', 35.6942561, 128.6865383), ('가산수피아', '경상북도 칠곡군 가산면 학하들안2길 105', 36.086109, 128.485044), ('국립칠곡숲체원', '경상북도 칠곡군 석적읍 유학로 532', 36.063539, 128.466183), ('옥계해변관광지', '강원특별자치도 강릉시 옥계면 금진솔밭길 104-29', 37.62673237, 129.0485254), ('마니산 국민관광지', '인천광역시 강화군 화도면 마니산로675번길 18', 37.63245827, 126.4237957), ('경산상대온천', '경상북도 경산시 남산면 상대로116길 18', 35.78037855, 128.7922287), ('동의한방촌', '경상북도 경산시 남산면 삼성현공원로38', 35.79937497, 128.800141), ('삼성현역사문화공원', '경상북도 경산시 남산면 삼성현공원로59', 35.80168834, 128.7966117), ('자인계정숲', '경상북도 경산시 자인면 계정길 68', 35.81784608, 128.8189958), ('화암관광지', '강원특별자치도 정선군 화암면 화암동굴길 12-13', 37.35092374, 128.7896216), ('아우라지 관광지', '강원특별자치도 정선군 여량면 아우라지길 69', 37.47574407, 128.7243663), ('성륜사', '전라남도 곡성군 옥과면 미술관로 287', 35.29704978, 127.1208706), ('관음사', '전라남도 곡성군 오산면 성덕관음길 453', 35.21469614, 127.1653199), ('천태암', '전라남도 곡성군 목사동면 대신로 381-336', 35.09395689, 127.2764599), ('용산재', '전라남도 곡성군 목사동면 신숭겸로 226', 35.13103091, 127.3210133), ('은평역사한옥박물관', '서울특별시 은평구 연서로50길 8', 37.64033246, 126.9380102), ('금성당', '서울특별시 은평구 진관2로 57-23', 37.63513547, 126.92538), ('다대포해수욕장', '부산광역시 사하구 몰운대1길 14(다대동)', 35.04638172, 128.9680068), ('낙동강하구아미산전망대', '부산광역시 사하구 다대낙조2길 77(다대동)', 35.05276243, 128.9607468), ('을숙도 생태공원', '부산광역시 사하구 낙동남로1233번길 25(하단동)', 35.11008837, 128.9449571), ('을숙도 철새공원', '부산광역시 사하구 낙동남로 1240(하단동)', 35.10449092, 128.9459791), ('감천문화마을', '부산광역시 사하구 감내2로 203(감천동)', 35.09748815, 129.0106077), ('철암탄광역사촌', '강원특별자치도 태백시 동태백로 406', 37.11436554, 129.0372165), ('석탄박물관', '강원특별자치도 태백시 천제단길 195', 37.11724029, 128.9505662), ('태백고생대자연사박물관', '강원특별자치도 태백시 태백로 2249', 37.0948994, 129.0396319), ('무릉계곡', '강원특별자치도 동해시 삼화로 584', 37.463551, 129.014511), ('신라불교초전지', '경상북도 구미시 도개면 도개다곡길 389-46', 36.29090918, 128.3633378), ('대신주말농장 정보화마을', '전라남도 곡성군 곡성읍 신리길 24-1', 35.29488111, 127.3237142), ('오-월드', '대전광역시 중구 사정공원로 70(사정동)', 36.28749924, 127.3985039), ('뿌리공원', '대전광역시 중구 뿌리공원로 79(침산동)', 36.28538043, 127.3883), ('아쿠아리움', '대전광역시 중구 보문산공원로 469(대사동)', 36.31005741, 127.4209439), ('춘향테마파크', '전북특별자치도 남원시 양림길 14-9', 35.401464, 127.386902), ('남원항공우주천문대', '전북특별자치도 남원시 양림길 48-63', 35.399194, 127.387388), ('남원시립김병종미술관', '전북특별자치도 남원시 함파우길 65-14', 35.398706, 127.389555), ('혼불문학관', '전북특별자치도 남원시 사매면 노봉안길 52', 35.481096, 127.319992), ('국악의 성지', '전북특별자치도 남원시 운봉읍 비전길 69', 35.459997, 127.569101), ('지리산허브밸리', '전북특별자치도 남원시 운봉읍 바래봉길 214', 35.435092, 127.548617), ('남원백두대간생태교육장전시관', '전북특별자치도 남원시 운봉읍 운봉로 151', 35.397328, 127.5079302), ('만인의총', '전북특별자치도 남원시 만인로 3', 35.420404, 127.377325), ('아담원', '전북특별자치도 남원시 이백면 목가길 193', 35.440571, 127.476826), ('남원관광지', '전북특별자치도 남원시 양림길 12', 35.402481, 127.386137), ('용문산 관광지', '경기도 양평군 용문면 용문산로 641', 37.54529617, 127.583066), ('두물머리', '경기도 양평군 양서면 두물머리길 145', 37.53362262, 127.3174715), ('세미원', '경기도 양평군 양서면 양수로 93', 37.54093866, 127.3239608), ('오동도', '전라남도 여수시 오동도로 222', 34.7443435, 127.764121), ('상한 농촌체험 휴양마을', '전라남도 곡성군 죽곡면 상한길 240', 35.16105726, 127.3987916), ('무창 농촌체험 휴양마을', '전라남도 곡성군 옥과면 금의길 59', 35.28642041, 127.1520961), ('안개 농촌체험 휴양마을', '전라남도 곡성군 고달면 고산로 39-18', 35.29052093, 127.3419816), ('가정 농촌체험 휴양마을', '전라남도 곡성군 고달면 가정마을길 65', 35.22727591, 127.3704119), ('죽산 농촌체험 휴양마을', '전라남도 곡성군 겸면 죽산하늘재길42', 35.23352336, 127.1752282), ('용암 농촌체험 휴양마을', '전라남도 곡성군 목사동면 용사용암길 8-2', 35.12878212, 127.3134337), ('칠봉 농촌체험 휴양마을', '전라남도 곡성군 겸면 칠봉길 89', 35.24620059, 127.1802226), ('봉정 농촌체험 휴양마을', '전라남도 곡성군 죽곡면 봉정길 43-137', 35.19394727, 127.3107811), ('성옥기념관', '전라남도 목포시 영산로 11', 34.78698576, 126.3798645), ('구 동본원사 목포별원', '전라남도 목포시 영산로 75번길 5', 34.78970032, 126.3842863), ('유달산', '전라남도 목포시 유달로 180', 34.79142993, 126.3759183), ('서산동 시화골목', '전라남도 목포시 해안로 127번길 14-2', 34.78174519, 126.3773176), ('용연동굴', '강원특별자치도 태백시 태백로 283-29', 37.20888277, 128.9418601), ('섬진강 침실습지', '전라남도 곡성군 고달면 고달길 192-6', 35.27503038, 127.3335915), ('곡성아트빌리지', '전라남도 곡성군 오곡면 섬진강로 1455', 35.23004949, 127.3677915), ('곡성섬진강천문대', '전라남도 구례군 구례읍 섬진강로 1234', 35.22666779, 127.3691674), ('섬진강도깨비마을', '전라남도 곡성군 고달면 호곡도깨비길 119-99', 35.25424902, 127.363209), ('압록상상스쿨', '전라남도 곡성군 오곡면 섬진강로 1060', 35.19580901, 127.375484), ('압록유원지', '전라남도 곡성군 죽곡면 섬진강로 1012', 35.19196269, 127.3775123), ('곡성기차당 뚝방마켓', '전라남도 곡성군 곡성읍 곡성로 856', 35.27976851, 127.2984376), ('용현자연휴양림', '충청남도 서산시 운산면 마애삼존불길 339', 36.7518805, 126.6076383), ('해미읍성', '충청남도 서산시 해미면 남문2로 143', 36.71355717, 126.5503842), ('반남고분군', '전라남도 나주시 반남면 고분로 756', 34.91475649, 126.6613546), ('백호문학관', '전라남도 나주시 다시면 회진길 8', 34.99694703, 126.6668094), ('거제자연휴양림', '경상남도 거제시 동부면 거제중앙로 325', 34.785699, 128.625943), ('계도어촌체험마을', '경상남도 거제시 사등면 가조로 837', 34.974872, 128.517512), ('다대어촌체험마을', '경상남도 거제시 남부면 다대5길 11', 34.73449995, 128.630513), ('도장포어촌체험마을', '경상남도 거제시 남부면 도장포1길 76', 34.743449, 128.662475), ('쌍근어촌체험마을', '경상남도 거제시 남부면 남부해안로 1094', 34.762399, 128.589438), ('광덕계곡관광지', '강원특별자치도 화천군 사내면 포화로 582', 38.07630808, 127.4817356), ('은파관광지', '전북특별자치도 군산시 은파순환길9 (나운동)', 35.95536227, 126.6890748), ('금강호관광지', '전북특별자치도 군산시 성산면 철새로 120', 36.01987856, 126.7653058), ('섬진강변 철쭉길', '전라남도 곡성군 오곡면 섬진강로 2068', 35.26690786, 127.3325028), ('섬진강변 자전거하이킹', '전라남도 곡성군 오곡면 기차마을로 232', 35.27753393, 127.3082912), ('두계 농촌체험 휴양마을', '전라남도 곡성군 고달면 두계길 126', 35.24354627, 127.3790396), ('봉조 농촌체험 휴양마을', '전라남도 곡성군 오곡면 봉조길 114', 35.21496518, 127.3604656), ('김해가야테마파크', '경상남도 김해시 가야테마길 161', 35.25163508, 128.8937494), ('화진포해양박물관', '강원특별자치도 고성군 현내면 화진포길 412', 38.481345, 128.435682), ('화진포 생태박물관', '강원특별자치도 고성군 거진읍 화진포길 256', 38.47218738, 128.4436214), ('송지호 관망타워', '강원특별자치도 고성군 죽왕면 동해대로 6021', 38.33623615, 128.5175989), ('진하해수욕장', '울산광역시 울주군 서생면 진하해변길 77', 35.38384823, 129.3460154), ('영남알프스 복합웰컴센터', '울산광역시 울주군 상북면 알프스온천5길 103-8', 35.55645417, 129.0679842), ('외고산옹기마을', '울산광역시 울주군 온양읍 외고산 3길 36', 35.435094, 129.2795067), ('곡성청소년야영장', '전라남도 곡성군 고달면 가정마을길 51', 35.22777786, 127.369723), ('곡성기차마을전통시장', '전라남도 곡성군 곡성읍 곡성로 856', 35.27976851, 127.2984461), ('곡성기차마을 패러글라이딩체험', '전라남도 곡성군 오곡면 덕산리 123-6', 35.26672854, 127.3087391), ('섬진강 레일바이크', '전라남도 곡성군 오곡면 섬진강로 1465 (가정역)', 35.23015492, 127.3682034), ('곡성집라인', '전라남도 곡성군 오곡면 섬진강로 1492', 35.23192974, 127.3698908), ('대황강 출렁다리', '전라남도 곡성군 죽곡면 태평리 783', 35.15035107, 127.3286414), ('반구정습지', '전라남도 곡성군 석곡면 봉전리 34-1', 35.11550817, 127.2453298), ('대황강자연휴식공원', '전라남도 곡성군 석곡면 석곡리 246-12', 35.12890532, 127.2559931), ('서동미로시장', '부산광역시 금정구 서동로141번길 16', 35.21586987, 129.102466), ('스포원파크', '부산광역시 금정구 체육공원로399번길 324', 35.28912108, 129.1070434), ('한국이슬람부산성원', '부산광역시 금정구 금단로 113-13', 35.25998626, 129.0922564), ('요산문학관', '부산광역시 금정구 팔송로 60-6', 35.27236562, 129.085732), ('분원백자자료관', '경기도 광주시 남종면 산수로 1642-1', 37.49625742, 127.3034492), ('천진암', '경기도 광주시 퇴촌면 천진암로 1203', 37.42357313, 127.3883989), ('경기도자박물관', '경기도 광주시 곤지암읍 경충대로 727', 37.35028421, 127.3347877), ('경안천습지생태공원', '경기도 광주시 퇴촌면 산수로 1159', 37.45920014, 127.3051169), ('팔당물안개공원', '경기도 광주시 남종면 산수로 1897', 37.50956344, 127.3174613), ('중대물빛공원', '경기도 광주시 순암로 307', 37.40272258, 127.218054), ('조태일시문학기념관', '전라남도 곡성군 죽곡면 태안로 622-38', 35.13351552, 127.3685909), ('섬진강문화학교 독도사진전시관', '전라남도 곡성군 죽곡면 태안로 793', 35.14820762, 127.3603367), ('시사교육박물관 김갑진 갤러리', '전라남도 곡성군 목사동면 대곡당산길 1', 35.11000012, 127.262542), ('문경 철로자전거(구랑리역)', '경상북도 문경시 마성면 구랑로 20', 36.66102501, 128.0993075), ('문경 철로자전거(가은역)', '경상북도 문경시 가은읍 대야로 2445', 36.64931827, 128.0607306), ('문경 오미자테마터널', '경상북도 문경시 마성면 문경대로 1356-1', 36.65989315, 128.1286031), ('고모산성, 토끼비리', '경상북도 문경시 마성면 고모산성길 60', 36.66292256, 128.1290151), ('심청효문화센터', '전라남도 곡성군 오산면 오산로 254-4', 35.20836622, 127.1288481), ('심청골짝나라학교', '전라남도 곡성군 석곡면 방주마천목길 107', 35.17183675, 127.2580116), ('심청한옥마을', '전라남도 곡성군 오곡면 심청로 178', 35.23063008, 127.3575205), ('오션어드벤처', '경상남도 거제시 일운면 거제대로 2660', 34.843791, 128.702943), ('옥포대첩기념공원', '경상남도 거제시 팔랑포2길 87', 34.902196, 128.714059), ('조선해양문화관', '경상남도 거제시 일운면 지세포해안로 41', 34.833694, 128.701885), ('지심도', '경상남도 거제시 일운면 지심도길 56-12', 34.820647, 128.751037), ('칠천량해전공원', '경상남도 거제시 하청면 칠천로 265-39', 34.977629, 128.628247), ('한산도 제승당', '경상남도 통영시 한산면 한산일주로 70', 34.79264032, 128.4759648), ('통영루지', '경상남도 통영시 발개로 178', 34.82440908, 128.4241617), ('박경리기념관', '경상남도 통영시 산양읍 산양중앙로 173', 34.802301, 128.4035792), ('해저터널', '경상남도 통영시 당동 1-3', 34.83609021, 128.411698), ('장사도해상공원', '경상남도 통영시 한산면 장사도길 55', 34.71373747, 128.5588706), ('통영시립박물관', '경상남도 통영시 중앙로 65', 34.84075153, 128.4167887), ('삼도수군통제영', '경상남도 통영시 세병로 27', 34.84715495, 128.423489), ('평택호관광단지', '경기도 평택시 현덕면 평택호길 48', 36.914825, 126.912951), ('남한산성', '경기도 광주시 남한산성면 남한산성로 731', 37.47669636, 127.1884165), ('서천무지개분수대', '전라남도 광양시 광양읍 칠성리 729', 34.9735109, 127.5756854), ('장도전수관', '전라남도 광양시 광양읍 매천로 771', 34.97705608, 127.5805515), ('도립미술관', '전라남도 광양시 순광로 680-4', 34.96887104, 127.5903033), ('광양와인동굴', '전라남도 광양시 광양읍 강정길 33', 34.96130264, 127.6085909), ('광양역사문화관', '전라남도 광양시 광양읍 매천로 829', 34.9757671, 127.5859802), ('사라실 라벤더 단지', '전라남도 광양시 주암면 문길리 산 20-2', 35.04162939, 127.270748), ('사라실 예술촌', '전라남도 광양시 사곡로 201', 34.962211, 127.626222), ('광양 마로산성', '전라남도 광양시 사곡리 산 254', 34.966742, 127.619496), ('광양 궁시장', '전라남도 광양시 광양읍 구산리 716', 34.98778794, 127.5865036), ('매천황현생가', '전라남도 광양시 봉강면 서석길 14-3', 34.99341564, 127.561707), ('몰운대', '부산광역시 사하구 몰운대1길 14(다대동)', 35.04638172, 128.9680068), ('다대포 꿈의 낙조분수', '부산광역시 사하구 몰운대1길 14(다대동)', 35.04638172, 128.9680068), ('용호 Sea-Side 관광지', '부산광역시 남구 용호동 산 205번지', 35.101225, 129.120361), ('팔공산 하늘정원', '경상북도 군위군 부계면 동산리 산 74-18', 36.023813, 128.696761), ('김수환 추기경 사랑과 나눔 공원', '경상북도 군위군 군위읍 군위금성로 270', 36.232449, 128.599613), ('사라온 이야기 마을', '경상북도 군위군 군위읍 동서길 49', 36.236155, 128.567733), ('다산초당', '전라남도 강진군 도암면 다산초당길 68-35', 34.5807211, 126.7449445), ('고려청자박물관', '전라남도 강진군 대구면 청자촌길 33', 34.50866203, 126.8008374), ('백련사', '전라남도 강진군 도암면 백련사길 145', 34.58775658, 126.747892), ('한려수도조망케이블카', '경상남도 통영시 발개로 205', 34.82665041, 128.4251243), ('간월도관광지', '충청남도 서산시 부석면 간월도리 산33 일원', 36.60625263, 126.4177537), ('서산류방택천문기상과학관', '충청남도 서산시 인지면 무학로 1353-4', 36.72735767, 126.4131464), ('서산버드랜드', '충청남도 서산시 부석면 천수만로 655-73', 36.62951253, 126.378389), ('서포리관광지', '인천광역시 옹진군 덕적면 덕적남로606번길 3', 37.2232919, 126.1169337), ('고래불관광지', '경상북도 영덕군 병곡면 고래불로 68', 36.57151713, 129.4190153), ('장사해수욕장관광지', '경상북도 영덕군 남정면 동해대로 3592', 36.2824187, 129.3755938), ('오색관광지', '강원특별자치도 양양군 서면 가라피리 165', 38.07706115, 128.5128997), ('지경관광지', '강원특별자치도 양양군 현남면 지리 5-1', 37.92741327, 128.7948611), ('설해원(양양국제공항)', '강원특별자치도 양양군 손양면 동호리 510-28', 38.05555608, 128.6641979), ('표충비', '경상남도 밀양시 무안면 동부동안길4', 35.471582, 128.6520376), ('작원관지', '경상남도 밀양시 삼랑진읍 작원관지길 77', 35.39853126, 128.8665763), ('예림서원', '경상남도 밀양시 부북면 예림서원로128', 35.46605971, 128.7299691), ('밀양향교', '경상남도 밀양시 밀양향교3길 19 (교동)', 35.50660573, 128.7545381), ('밀양관아', '경상남도 밀양시 중앙로348', 35.49429512, 128.754331), ('밀양영남루', '경상남도 밀양시 중앙로324', 35.49154522, 128.755608), ('만어사', '경상남도 밀양시 삼랑진읍 만어로776', 35.454932, 128.846222), ('유당공원', '전라남도 광양시 광양읍 백운로 14', 34.97010442, 127.5924243), ('장계관광지', '충청북도 옥천군 안내면 장계1길 57', 36.37753534, 127.6350074), ('온달관광지', '충청북도 단양군 영춘면 온달로 23', 37.06295564, 128.4929221), ('천동관광지', '충청북도 단양군 단양읍 다리안로 520', 36.97401141, 128.4165952), ('다리안관광지', '충청북도 단양군 단양읍 소백산등산길 12', 36.9647819, 128.4207808), ('목포근대역사관 1관 (구, 일본영사관)', '전라남도 목포시 영산로 29번길 6', 34.78769578, 126.3817942), ('목포근대역사관 2관 (구,동양척식주식회사)', '전라남도 목포시 번화로 18', 34.78596374, 126.3814912), ('정양레포츠공원', '경상남도 합천군 대양면 동부로 39-13', 35.55690191, 128.1668052), ('정양늪생태공원', '경상남도 합천군 대양면 대야로 730', 35.55322772, 128.162786), ('해인사 소리길', '경상남도 합천군 가야면 가야산로 1160', 35.76749785, 128.1365362), ('신소양체육공원', '경상남도 합천군 합천읍 영창리 898', 35.580901, 128.170251), ('합천호회양관광단지', '경상남도 합천군 대병면 회양리 700-27', 35.524398, 128.016537), ('심우장', '서울특별시 성북구 성북로29길 24', 37.5936268, 126.9916662), ('길상사', '서울특별시 성북구 선잠로5길 68(성북동)', 37.59910942, 126.9946522), ('한국가구박물관', '서울특별시 성북구 대사관로121', 37.60037831, 126.9934492), ('호미곶관광단지', '경상북도 포항시 남구 호미곶면 해맞이로 136', 36.07608308, 129.5665319), ('일제 경성호국신사 계단(108계단)', '서울특별시 용산구 신흥로22가길 33', 37.546023, 126.982542), ('찬바람재', '서울특별시 용산구 녹사평대로 195', 37.534815, 126.987014), ('옛 남영동 대공분실(현 민주화운동기념관)', '서울특별시 용산구 한강대로71길 37', 37.54101166, 126.9716345), ('옛 용산공설시장(현 남영아케이드)', '서울특별시 용산구 한강대로84길 7', 37.543723, 126.972924), ('전쟁기념관', '서울특별시 용산구 이태원로 29', 37.53731405, 126.9784661), ('문경새재 도립공원', '경상북도 문경시 문경읍 새재로 932', 36.76160207, 128.0769905), ('문경 에코랄라(석탄박물관 포함)', '경상북도 문경시 가은읍 왕능길 112', 36.65420039, 128.0613259), ('문경 철로자전거(진남역)', '경상북도 문경시 마성면 진남1길 155', 36.65438526, 128.1283486), ('레인보우힐링관광지', '충청북도 영동군 영동읍 매천리 산 35-1', 36.15635341, 127.7865137), ('송호관광지', '충청북도 영동군 양산면 송호로 105', 36.12969079, 127.676367), ('가우도', '전라남도 강진군 대구면 중저길 31-27', 34.53639075, 126.791046), ('영랑생가', '전라남도 강진군 강진읍 영랑생가길 15', 34.6420738, 126.7655711), ('아산조방원미술관', '전라남도 곡성군 옥과면 미술관로 288', 35.29706668, 127.1224042), ('곡성생태체험관', '전라남도 곡성군 옥과면 월파로 295', 35.31719414, 127.1699834), ('김해낙동강레일파크', '경상남도 김해시 생림면 마사로473번길 41', 35.37177644, 128.8208387), ('오월드', '대전광역시 중구 사정공원로 70', 36.28749807, 127.3985061), ('뿌리공원', '대전광역시 중구 뿌리공원로 79', 36.28537962, 127.3883), ('엑스포과학공원', '대전광역시 유성구 대덕대로 480', 36.37662561, 127.3871992), ('한밭수목원', '대전광역시 서구 둔산대로 169', 36.36646857, 127.3880154), ('계족산황톳길', '대전광역시 대덕구 장동로 59', 36.40406651, 127.4297932), ('대청호반', '대전광역시 대덕구 대청로 607', 36.47362538, 127.4735679), ('장태산휴양림', '대전광역시 서구 장안로 461', 36.21596913, 127.3412778), ('대전둘레산길', '대전광역시 중구 보문산공원로 440', 36.30879672, 127.4231236), ('동춘당', '대전광역시 대덕구 동춘당로 80', 36.36537889, 127.4412933), ('대전문화예술단지', '대전광역시 서구 둔산대로 169', 36.366496, 127.3880154), ('진주성', '경상남도 진주시 남강로 626(본성동)', 35.19043229, 128.0802214), ('진양호', '경상남도 진주시 남강로1번길 130(판문동)', 35.17660992, 128.0362677), ('경상남도 수목원', '경상남도 진주시 이반성면 수목원로 386', 35.15719862, 128.2957346), ('용담호및주변시설', '전북특별자치도 진안군 안천면 안용로 793', 35.94593497, 127.5256742), ('운장산자연휴양림', '전북특별자치도 진안군 정천면 휴향림길 77', 35.89924607, 127.4211096), ('데미샘자연휴양림', '전북특별자치도 진안군 백운면 데미샘1길 172', 35.6569938, 127.4610402), ('대황강자연휴식체혐장야영장', '전라남도 곡성군 석곡면 유정리 37', 35.12497878, 127.2550756), ('청계동계곡', '전라남도 곡성군 곡성읍 신기리 산 190-1', 35.31375081, 127.2525557), ('금강하구둑관광지', '충청남도 서천군 마서면 장산로 855번길 56-2', 36.01993599, 126.736726), ('춘장대', '충청남도 서천군 서면 춘장대길 20', 36.16334922, 126.5243355), ('예술랜드리조트', '전라남도 여수시 돌산읍 무술목길 142-1,', 34.69510702, 127.7769452), ('여수이순신대교', '전라남도 여수시 묘도7길 110', 34.88923653, 127.704929), ('허준근린공원', '서울특별시 강서구 허준로5길 42', 37.56759867, 126.8537291), ('양천향교', '서울특별시 강서구 양천로47나길 53', 37.572863, 126.839905), ('약사사', '서울특별시 강서구 금낭화로17길 261', 37.58312625, 126.8050072), ('양천고성지', '서울특별시 강서구 양천로47나길 52-54', 37.57425363, 126.8413319), ('궁산땅굴역사전시관', '서울특별시 강서구 양천로49길 106', 37.57266746, 126.8383649), ('허가바위', '서울특별시 강서구 허준로5길 15', 37.56983571, 126.8500301), ('마이산도립공원 일원', '전북특별자치도 진안군 마령면 마이산남로 182', 35.75700898, 127.3938329), ('담양호국민관광지', '전라남도 담양군 용면 월계리 149-67', 35.4019831, 126.9990829), ('죽녹원', '전라남도 담양군 담양읍 죽녹원로 119', 35.32535666, 126.9866155), ('메타세쿼이아랜드', '전라남도 담양군 담양읍 학동리 578-4', 35.32342607, 127.001993), ('소쇄원', '전라남도 담양군 가사문학면 소쇄원길 17', 35.18433903, 127.0121793), ('천제연폭포', '제주특별자치도 서귀포시 천제연로 132(중문동)', 33.2526783, 126.4183754), ('산방산', '제주특별자치도 서귀포시 안덕면 산방로 218-11', 33.23697818, 126.312306), ('여수해상케이블카', '전라남도 여수시 자산4길 39', 34.7402223, 127.7529197), ('여수세계박람회장', '전라남도 여수시 박람회길 1', 34.75218956, 127.7465005), ('여수해양공원', '전라남도 여수시 하멜로 96', 34.73676698, 127.7484079), ('인천도시역사관', '인천광역시 연수구 인천타워대로 238', 37.39281227, 126.6358879), ('가천박물관', '인천광역시 연수구 청량로102번길 40-9', 37.41847997, 126.6576026), ('백제문화관광단지', '충청남도 부여군 규암면 백제문로 400', 36.30386704, 126.9007732), ('최참판댁', '경상남도 하동군 악양면 평사리길 66-7', 35.15590905, 127.688408), ('화개장터', '경상남도 하동군 화개면 쌍계로 15', 35.18798946, 127.6240666), ('삼성궁', '경상남도 하동군 청암면 삼성궁길 2', 35.23870093, 127.7054288), ('이병주문학관', '경상남도 하동군 북천면 이명골길 14-28', 35.09720238, 127.8942256), ('하동레일파크', '경상남도 하동군 북천면 경서대로 2446-6', 35.11333681, 127.8929765), ('지리산역사관', '경상남도 하동군 화개면 화개로 1438', 35.2896912, 127.646527), ('하동코리아짚와이어', '경상남도 하동군 금남면 경충로 493-9', 34.98260917, 127.894714), ('하동 플라이웨이 케이블카', '경상남도 하동군 금남면 경충로 461-7', 34.98070015, 127.8953015), ('녹진관광지', '전라남도 진도군 군내면 진도대로 8478', 34.56907794, 126.300024), ('회동관광지', '전라남도 진도군 고군면 신비의바닷길 148', 34.42726548, 126.3513474), ('아리랑마을관광지', '전라남도 진도군 임회면 아리랑길 95-5', 34.38201529, 126.2310334), ('신정호국민관광지', '충청남도 아산시 신정로 616', 36.77152141, 126.9839152), ('아산온천관광지', '충청남도 아산시 송악면 도송로632번길 138', 36.69884759, 126.9666069), ('노고단', '전라남도 구례군 토지면 반곡길 42-237', 35.23629045, 127.5547986), ('지리산정원', '전라남도 구례군 광의면 온동2길 124', 35.28722643, 127.4388392), ('섬진강대숲길', '전라남도 구례군 구례읍 원방리 1', 35.18633781, 127.4652531), ('화엄사', '전라남도 구례군 마산면 화엄사로 239', 35.25731602, 127.4977197), ('천은사', '전라남도 구례군 광의면 노고단로 209', 35.27309271, 127.476325), ('사성암', '전라남도 구례군 문척면 사성암길 303', 35.18012828, 127.480644), ('월봉서원', '광주광역시 광산구 광곡길 133', 35.23583653, 126.7451862), ('무양서원', '광주광역시 광산구 산월로21번길 26', 35.21001305, 126.8400956), ('용아생가', '광주광역시 광산구 소촌로46번길 24', 35.14942906, 126.7951682), ('호가정', '광주광역시 광산구 동곡분토길 195', 35.08243286, 126.7871342), ('신창동 유적체험학습관', '광주광역시 광산구 신창동 632-4', 35.19682512, 126.8457374), ('쌍암공원', '광주광역시 광산구 첨단중앙로182번길 39', 35.22222807, 126.8442514), ('수완호수공원', '광주광역시 광산구 장신로82번길 57', 35.18794733, 126.8202489), ('송정작은미술관', '광주광역시 광산구 송정로30번길 25', 35.13510527, 126.7933629), ('구문소 관광지', '강원특별자치도 태백시 동태백로11(동점동)', 37.09296812, 129.0422636), ('회산백련지', '전라남도 무안군 일로읍 백련로 333', 34.86008886, 126.5298981), ('인천가스과학관', '인천광역시 연수구 인천신항대로960', 37.35281836, 126.6121219), ('인천상륙작전기념관', '인천광역시 연수구 청량로160번길 26', 37.42070925, 126.6531798), ('웅포관광지', '전북특별자치도 익산시 웅포면 강변로 25', 36.06581391, 126.8754193), ('보은 삼년산성', '충청북도 보은군 보은읍 성주1길 104', 36.48993999, 127.7417752), ('보은 우당고택', '충청북도 보은군 장안면 개안길 10-2', 36.46857583, 127.7906134), ('속리산 법주사', '충청북도 보은군 속리산면 법주사로 379', 36.53993659, 127.8306346), ('동학농민혁명기념공원', '충청북도 보은군 보은읍 동학로 236-22', 36.51567136, 127.7591951), ('구드래관광지', '충청남도 부여군 부여읍 백강로 148', 36.28718846, 126.9068136), ('서동요역사관광지', '충청남도 부여군 충화면 충신로 618', 36.14594895, 126.8248464), ('연희네슈퍼(영화 1987 촬영지)', '전라남도 목포시 해안로 127번길 14-2', 34.78174519, 126.3773176), ('춤추는 바다분수', '전라남도 목포시 평화로 82', 34.79666213, 126.4337909), ('목포해상케이블카', '전라남도 목포시 해양대학로 240', 34.79940179, 126.3700239), ('삼학도크루즈', '전라남도 목포시 삼학로 92번길 104', 34.78101571, 126.387931), ('김대중노벨평화상기념관', '전라남도 목포시 삼학로 92번길 68', 34.78285177, 126.3922506), ('목포자연사박물관', '전라남도 목포시 남농로 135', 34.79372717, 126.4210674), ('갓바위', '전라남도 목포시 남농로 135', 34.79372717, 126.4210674), ('도림사계곡', '전라남도 곡성군 곡성읍 도림로 175', 35.26724907, 127.2575171), ('태안사계곡', '전라남도 곡성군 죽곡면 태안로 622-215', 35.13195828, 127.3863807), ('태안사', '전라남도 곡성군 죽곡면 태안로 622-215', 35.13195828, 127.3863807), ('도림사', '전라남도 곡성군 곡성읍 도림로 175', 35.26727743, 127.2575047), ('괴강', '충청북도 괴산군 괴산읍 충민로검승3길 10', 36.80676203, 127.8252917), ('수옥정', '충청북도 괴산군 연풍면 수옥정길 174, 수옥정관광지관리사무소', 36.81361722, 128.036232), ('창원단감테마공원', '경상남도 창원시 의창구 동읍 동읍로 359번길 27', 35.29915488, 128.6632468), ('창원의집', '경상남도 창원시 의창구 사림로 16번길 59', 35.24444959, 128.6813533), ('주남저수지', '경상남도 창원시 의창구 동읍 주남로 101번길 26', 35.30732964, 128.6779306), ('마금산온천', '경상남도 창원시 의창구 북면 천주로 1167', 35.35464845, 128.6094168), ('창원과학체험관', '경상남도 창원시 성산구 충혼로 72번길 16', 35.230268, 128.6617774), ('마산해양레포츠센터', '경상남도 창원시 마산합포구 제2부두로 56', 35.19796654, 128.5759021), ('웅천도요지전시관', '경상남도 창원시 진해구 두동도요로 198', 35.12738997, 128.8155983), ('진해해양레포츠센터', '경상남도 창원시 진해구 천자로 160', 35.14129989, 128.6883002), ('태조대왕태실', '충청남도 금산군 추부면 마전리 산 1-86', 36.19976177, 127.4543633), ('보석사', '충청남도 금산군 남이면 보석사1길 30', 36.054757, 127.4753969), ('보석사   은행나무', '충청남도 금산군 남이면 보석사1길 30', 36.054757, 127.4753969), ('안성남사당공연장', '안성시 보개면 남사당로 198-2', 37.03172757, 127.3101553), ('안성맞춤랜드', '안성시 보개면 남사당로 198', 37.03326816, 127.3102514), ('안성맞춤캠핑장', '안성시 보개면 남사당로 196-31', 37.0301714, 127.3134194), ('사계절썰매장', '안성시 보개면 남사당로 198-3', 37.03132577, 127.3113281), ('천문과학관', '안성시 보개면 남사당로 198-9', 37.03291074, 127.3125372), ('공예문화센터', '안성시 보개면 남사당로 198-11', 37.0345765, 127.3112908), ('안성맞춤박물관', '안성시 대덕면 서동대로 4726-15', 37.011318, 127.228354), ('안성3.1운동기념관', '안성시 원곡면 만세로 868', 37.06278592, 127.1752942), ('박두진문학관', '안성시 보개면 남사당로 198-11', 37.03497856, 127.3108743), ('고양관광정보센터', '경기도 고양시 일산동구 중앙로1271-1', 37.65948674, 126.7726989), ('행주산성', '경기도 고양시 덕양구 행주로 15번길 89', 37.59610775, 126.8287118), ('산정호수', '경기도 포천시 영북면 산정호수로411번길 5 일원', 38.065669, 127.315722), ('백운계곡', '경기도 포천시 이동면 포화로 236-24 일원', 38.072165, 127.409889), ('창녕 남지 개비리', '경상남도 창녕군 남지읍 용산리 160-2', 35.3945598, 128.4405466), ('산토끼노래동산', '경상남도 창녕군 이방면 이방로 623', 35.57772326, 128.38358), ('창녕 영산 만년교', '경상남도 창녕군 영산면 동리 433', 35.45282939, 128.5283272), ('서울 한양도성(서울성곽)', '서울특별시 용산구 후암동 산1-2', 37.551483, 126.9853756), ('후암동 조선은행 사택지', '서울특별시 용산구 후암로16가길 7', 37.547716, 126.9786783), ('오성한옥마을', '전라북도 완주군 소양면 오도길 73', 35.90695186, 127.2367573), ('무궁화오토캠핑장', '전라북도 완주군 고산면 고산휴양림로 89', 35.97398132, 127.2315513), ('송광사&벚꽃길', '전라북도 완주군 소양면 송광수만로 255-16', 35.885763, 127.2417509), ('위봉사&위봉폭포&위봉산성', '전라북도 완주군 소양면 위봉길 53', 35.9105769, 127.2572831), ('화암사', '전라북도 완주군 경천면 화암사길 271', 36.06629938, 127.2872339), ('능암온천', '충청북도 충주시 앙성면 새바지길 37', 37.09370046, 127.802172), ('충온온천', '충청북도 충주시 앙성면 산전장수1길 103', 37.10173095, 127.7962316), ('수동관광지', '경기도 남양주시 수동면 비룡로 1635', 37.75797919, 127.2753596), ('부곡온천관광특구', '경상남도 창녕군 부곡면 원앙로 218', 35.4361725, 128.5916371), ('낙동강 유채단지', '경상남도 창녕군 남지읍 남지리 835-25', 35.384718, 128.4715696), ('함평자연생태공원', '전라남도 함평군 대동면 학동로 1398-77', 35.13500123, 126.5057), ('함평양서파충류생태공원', '전라남도 함평군 신광면 학동로 1398-9', 35.13834076, 126.4937003), ('함평엑스포공원', '전라남도 함평군 함평읍 곤재로 27', 35.05813923, 126.5226056), ('일강김철선생기념관', '전라남도 함평군 신광면 일강로 873-12', 35.18893103, 126.4801235), ('꽃무릇공원', '전라남도 함평군 해보면 용천사길 209', 35.18352786, 126.5452774), ('함평자동차극장', '전라남도 함평군 함평읍 함장로 1162', 35.05813923, 126.5226056), ('무주 덕유산 리조트', '전북특별자치도 무주군 설천면 만선로 185', 35.889797, 127.731389), ('대둔산도립공원', '전라북도 완주군 운주면 대둔산공원길 23', 36.11708032, 127.3328815), ('모악산도립공원', '전라북도 완주군 구이면 모악산길 91', 35.72946608, 127.1098589), ('고산자연휴양림', '전라북도 완주군 고산면 고산휴양림로 246', 35.96224205, 127.2328719), ('상관 공기마을 편백숲', '전라북도 완주군 상관면 죽림편백길 191-24', 35.737491, 127.2023884), ('대아수목원&대아호', '전라북도 완주군 동상면 대아수목로 94-34', 35.97623865, 127.3003851), ('비비정', '전라북도 완주군 삼례읍 비비정길 96-9', 35.90154901, 127.071693), ('비비정예술열차', '전라북도 완주군 삼례읍 비비정길 73-21', 35.89880244, 127.0676418), ('삼례문화예술촌', '전라북도 완주군 삼례읍 삼례역로 81-13', 35.90623513, 127.0658535), ('삼례책마을', '전라북도 완주군 삼례읍 삼례역로 68', 35.90745719, 127.065756), ('그림책미술관', '전라북도 완주군 삼례읍 삼례역로 48-1', 35.90832046, 127.068059), ('대한민국술테마박물관', '전라북도 완주군 구이면 덕천전원길 232-58', 35.726048, 127.1361627), ('완주전통문화체험장', '전라북도 완주군 고산면 대아저수로 416', 35.98879491, 127.261342), ('청소년전통문화체험관', '전라북도 완주군 고산면 대아저수로 416', 35.98879491, 127.261342), ('놀토피아', '전라북도 완주군 고산면 대아저수로 416', 35.98879491, 127.261342), ('증평인삼문화센터', '충청북도 증평군 증평읍 중부로 2451', 36.79588146, 127.5817602), ('에듀팜특구관광단지(벨포레리조트)', '충청북도 증평군 도안면 벨포레길 346', 36.84514868, 127.5821182), ('부평아트센터', '인천광역시 부평구 아트센터로 166', 37.4822383, 126.7049334), ('기후변화체험관', '인천광역시 부평구 장제로 267', 37.50932668, 126.7307645), ('부평역사박물관', '인천광역시 부평구 굴포로 151', 37.51214216, 126.7379081), ('부평안전체험관', '인천광역시 부평구 굴포로 110', 37.51098789, 126.7333177), ('인천나비공원', '인천광역시 부평구 평천로 26-47', 37.52026697, 126.6927245), ('칠백의총', '충청남도 금산군 금성면 의총길 50', 36.12942797, 127.492435), ('가야랜드', '경상남도 김해시 인제로 368(삼방동)', 35.25817158, 128.9024348), ('김해천문대', '경상남도 김해시 가야테마길 254(어방동)', 35.25306424, 128.887298), ('롯데워터파크', '경상남도 김해시 장유로 555(신문동)', 35.17891121, 128.8285605), ('클레이아크김해미술관', '경상남도 김해시 진례면 분청로 25', 35.25086479, 128.7457858), ('김해한옥체험관', '경상남도 김해시 왕릉길 40(봉황동)', 35.23436297, 128.8766847), ('노무현대통령생가', '경상남도 김해시 진영읍 봉하로129', 35.31448783, 128.7712136), ('김해수로왕릉', '경상남도 김해시 왕릉길 26(서상동)', 35.23487875, 128.8772054), ('석정온천관광지', '전북특별자치도 고창군 고창읍 석정2로 173', 35.43111098, 126.7405968), ('한탄강관광지', '경기도연천군전곡읍선사로76', 38.00887699, 127.0588604), ('연천재인폭포', '경기도 연천군 연천읍 부곡리 193', 38.07672631, 127.142345), ('연천호로고루', '경기도 연천군 장남면 원당리 1258', 37.98583872, 126.8596065), ('당포성', '경기도 연천군 미산면 동이리 778', 38.02350322, 126.9854182), ('연천전곡리유적', '경기도연천군전곡읍양연로1510', 38.01564838, 127.0614526), ('만리포 관광지', '충청남도 태안군 소원면 만리포2길 190-3', 36.79000781, 126.1465709), ('좌구산휴양랜드', '충청북도 증평군 증평읍 솟점말길 107', 36.70556821, 127.6511216), ('좌구산 천문대', '충청북도 증평군 증평읍 솟점말길 187', 36.70370345, 127.6530963), ('증평민속체험박물관', '충청북도 증평군 증평읍 둔덕길 89', 36.7638886, 127.5955694), ('보강천 미루나무숲', '충청북도 증평군 증평읍 송산리 649-45', 36.78834006, 127.5810146), ('완도타워', '전라남도 완도군 완도읍 장보고대로 330', 34.31443839, 126.763267), ('장도청해진유적지', '전라남도 완도군 완도읍 장좌리 787', 34.36012506, 126.7369503), ('장보고기념관', '전라남도 완도군 완도읍 청해진로 1455', 34.355097, 126.730164), ('해양생태전시관', '전라남도 완도군 완도읍 청해진로 1459', 34.351532, 126.731489), ('완도수목원', '전라남도 완도군 군외면 초평1길 156', 34.36065258, 126.6635321), ('청해포구촬영장', '전라남도 완도군 완도읍 청해진서로 1161-8', 34.32293555, 126.6705189), ('어촌민속전시관', '전라남도 완도군 완도읍 화흥포길 149', 34.307331, 126.688559), ('슬로시티청산도', '전라남도 완도군 청산면 청산로 132', 34.17179242, 126.8621742), ('용대관광지', '강원도 인제군 북면 십이선녀탕길 16', 38.18568475, 128.3061965), ('방동관광지', '강원도 인제군 기린면 방동약수로 89-59', 37.94387136, 128.3962628), ('에코촌유스호스텔', '전라남도 순천시 해룡면 생태배움길 123', 34.92310938, 127.5164423), ('순천자연휴양림', '전라남도 순천시 서면 청소년수련원길 170', 35.04388083, 127.4744949), ('기독교역사박물관', '전라남도 순천시 매산길 61(매곡동)', 34.96043473, 127.4801207), ('승주골프장', '전라남도 순천시 별량면 오실길 333', 34.89477542, 127.4482083), ('파인힐스골프장', '전라남도 순천시 주암면 송광사길 99', 35.03454985, 127.2723022), ('레이크힐스골프장', '전라남도 순천시 주암면 행정1길 77', 35.0453779, 127.3092617), ('순천컨트리클럽', '전라남도 순천시 별량면 동화사길 85', 34.86653556, 127.4059214), ('부영컨트리클럽', '전라남도 순천시 해룡면 신대로 188', 34.94005246, 127.5555065), ('고석정', '강원도 철원군 동송읍 태봉로 1825', 38.18542141, 127.2874037), ('직탕', '강원도 철원군 갈말읍 직탕길 32', 38.2069247, 127.2690548), ('보길윤선도원림', '전라남도 완도군 보길면 부황길 57', 34.16247899, 126.5559442), ('신지명사십리해수욕장', '전라남도 완도군 신지면 명사십리길 85-105', 34.327329, 126.8088597), ('해조류센터', '전라남도 완도군 완도읍 해변공원로 84', 34.320789, 126.748793), ('율포해수욕장', '전라남도 보성군 회천면 우암길 24', 34.67002926, 127.0890241), ('한국차문화공원', '전라남도 보성군 보성읍 녹차로 775', 34.71987643, 127.0817099), ('그림책도서관', '전라남도 순천시 도서관길 33(동외동)', 34.9579091, 127.4860252), ('순천시청소년수련원', '전라남도 순천시 서면 청소년수련원길 167', 35.04652524, 127.4733227), ('순천용오름마을', '전라남도 순천시 주암면 운룡2길 17', 35.0921171, 127.2020571), ('순천향매실마을', '전라남도 순천시 월등면 계월길 138', 35.0622232, 127.4045173), ('순천생태마을', '전라남도 순천시 승주읍 고산도목길 9', 35.06648711, 127.3602718), ('거차뻘배체험마을', '전라남도 순천시 별량면 거차길 130', 34.8375993, 127.4474457), ('낙안민속자연휴양림', '전라남도 순천시 낙안면 민속마을길 1600', 34.91226789, 127.3523884), ('포로수용소유적공원', '경상남도 거제시 계룡로 61', 34.87637998, 128.625371), ('거제해금강', '경상남도 거제시 남부면 갈곶리 산 1', 34.73266258, 128.683858), ('외도', '경남 거제시 일운면 외도길 17', 34.769507, 128.7129368), ('공곶이', '경상남도 거제시 일운면 와현리 87', 34.79451496, 128.713906), ('김영삼전대통령생가기록전시관', '경상남도 거제시 장목면 옥포대첩로 743', 34.931413, 128.715439), ('문동휴양지', '경상남도 거제시 문동동 산 8', 34.85648266, 128.6628354), ('바람의언덕', '경상남도 거제시 남부면 갈곶리 산14-47', 34.74401583, 128.6632699), ('우제봉전망대', '경남 거제시 남부면 갈곶리 산2-16', 34.732023, 128.673563), ('방화동가족휴가촌', '전북특별자치도 장수군 번암면 방화동로 778', 35.59064179, 127.5268761), ('운일암반일암', '전북특별자치도 진안군 주천면 동상주천로 1924', 35.97596963, 127.408748), ('간현관광지', '강원도 원주시 지정면 소금산길 26', 37.36489327, 127.832544), ('명승 거창 수승대', '경상남도 거창군 위천면 은하리길 2', 35.760923, 127.8336763), ('가조온천', '경상남도 거창군 가조면 온천길161', 35.69875771, 128.0231044), ('김포평화누리길 1코스', '경기도 김포시 대곶면 대명리 561(대명항)', 37.6535376, 126.5533799), ('김포평화누리길 2코스', '경기도 김포시 월곶면 성동리 산49(문수산성)', 37.73869766, 126.5484911), ('김포평화누리길 3코스', '경기도 김포시 하성면 전류리 43-4(전류리 포구)', 37.70077831, 126.6622045), ('김포 장릉', '경기도 김포시 장릉로 79(풍무동)', 37.6107519, 126.7126583), ('애기봉 평화생태공원', '경기도 김포시 월곶면 평화공원로 289', 37.75316598, 126.5942484), ('문수산성', '경기도 김포시 월곶면 성동리 산50', 37.7429948, 126.5467533), ('김포함상공원', '경기도 김포시 대곶면 대명항1로 110-36', 37.6406387, 126.539261), ('덕포진', '경기도 김포시 대곶면 덕포진로103번길 90', 37.6506248, 126.5387862), ('덕포진교육박물관', '경기도 김포시 대곶면 덕포진로103번길 90', 37.6506248, 126.5387862), ('꽃보라동산', '대구광역시 북구 산격동 1500-6', 35.88935018, 128.601059), ('운암지수변공원', '대구광역시 북구 구암동 349', 35.93241418, 128.5674979), ('팔달대교 야경', '대구광역시 북구 팔달동 524-4', 35.89629048, 128.5491628), ('금호강하중도', '대구광역시 북구 노곡동 673', 35.90019471, 128.5595886), ('경북대학교 캠퍼스', '대구광역시 북구 대학로80', 35.88909849, 128.6143217), ('함지공원', '대구광역시 북구 동암로38길 9', 35.9424608, 128.570482), ('구암서원', '대구광역시 북구 연암공원로17길 20', 35.89881592, 128.5989989), ('침산정', '대구광역시 북구 침산남로9길 118', 35.897221, 128.5848591), ('탄금공원', '충청북도 충주시 남한강로 24', 36.98782919, 127.9055691), ('충주호체험', '충청북도 충주시 중앙탑면 중앙탑길 150', 37.02308256, 127.8619229), ('창녕 우포늪', '경상남도 창녕군 유어면 우포늪길 220', 35.54456088, 128.4191195), ('화왕산', '경상남도 창녕군 창녕읍 옥천리 산322', 35.54419647, 128.5344873), ('사계고택', '충청남도 계룡시 두마면 사계로 122-4', 36.265691, 127.2719783), ('모원재', '충청남도 계룡시 두마면 왕대2길6-10', 36.254886, 127.2732753), ('염선재', '충청남도 계룡시 사계로 5-16(금암동)', 36.2693923, 127.261851), ('이심원충신정려현판', '충청남도 계룡시 금암1 길 7(금암동)', 36.280034, 127.2470773), ('신원재', '충청남도 계룡시 두마면 왕대2길43', 36.255245, 127.2705257), ('달성토성마을', '대구광역시 서구 국채보상로 83길 21 (비산동) 일대', 35.87331563, 128.5753765), ('대성관광지', '경기도 가평군 청평면 대성강변길 14', 37.681431, 127.379521), ('산장관광지', '경기도 가평군 상면 덕현산장길 71 산장관광지', 37.754183, 127.410629), ('동악산', '전라남도 곡성군 곡성읍 도림로 175', 35.26726372, 127.2575047), ('봉두산', '전라남도 곡성군 죽곡면 태안로 622-215', 35.13195828, 127.3863807), ('설산', '전라남도 곡성군 옥과면 설옥리 산 256-1', 35.29950593, 127.1025224), ('통명산', '전라남도 곡성군 삼기면 금계리 산 137-1', 35.20404024, 127.2592454), ('함평해수찜', '전라남도 함평군 손불면 석산로 61', 35.10050068, 126.4648643), ('능강관광지', '충청북도 제천시 수산면 옥순봉로 1248', 36.99002277, 128.1956764), ('금월봉관광지', '충청북도 제천시 금성면 청풍호로 1316', 37.04384263, 128.1736527), ('계산관광지', '충청북도 제천시 청풍면 계산리 9-4', 37.017861, 128.130911), ('만남의광장', '충청북도 제천시 청풍면 청풍호로50길 6', 37.01083356, 128.1811895), ('제천 성내관광지', '충청북도 제천시 금성면 청풍호로 1482', 37.03366403, 128.1751729), ('제천온천관광지', '충청북도 제천시 수산면 내리 산24', 36.92183352, 128.1819714), ('교리관광지', '충청북도 제천시 청풍면 청풍호로 1798', 37.01656717, 128.1768531), ('팔봉산관광지', '강원특별자치도 홍천군 서면 한치골길 1124', 37.703098, 127.695183), ('소노벨비발디파크', '강원특별자치도 홍천군 서면 한치골길 262', 37.65233706, 127.6873348), ('오전약수관광지', '경상북도 봉화군 물야면 문수로 1541', 37.01209531, 128.7457209), ('다덕약수관광지', '경상북도 봉화군 봉성면 다덕로 873', 36.9141215, 128.8273232), ('다도박물관', '경기도 김포시 월곶면 애기봉로275번길 187-49', 37.7375035, 126.5744366), ('당고개 순교성지', '서울특별시 용산구 청파로 139-26', 37.53559012, 126.966963), ('옛 풍국제과 공장(현 오리온)', '서울특별시 용산구 백범로90다길 13', 37.53567207, 126.9695135), ('용산신학교', '서울특별시 용산구 원효로19길 49', 37.53420937, 126.9545934), ('금마관광지', '전북특별자치도 익산시 금마면 고도9길 41-14', 36.0015063, 127.0570781), ('미륵사지관광지', '전북특별자치도 익산시 금마면 미륵사지로 362', 36.01154232, 127.0287139), ('왕궁보석테마 관광지', '전북특별자치도 익산시 왕궁면 호반로 8', 35.99061279, 127.1025486);
-- ----------------------------------- CHECK DATAS IN TABLE -----------------------------------
SELECT *
FROM `user`;
SELECT *
FROM `item`;
SELECT *
FROM `item_trade`;
SELECT *
FROM `course`;