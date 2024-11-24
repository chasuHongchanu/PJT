-- -----------------------------------------------------
-- Schema trend
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `trend` ;

-- -----------------------------------------------------
-- Schema trend
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trend` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `trend` ;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
                                      `user_id` VARCHAR(50) NOT NULL,
    `user_password` VARCHAR(128) NULL DEFAULT NULL,
    `user_nickname` VARCHAR(50) NULL DEFAULT NULL,
    `user_address` VARCHAR(100) NULL DEFAULT NULL,
    `user_email` VARCHAR(50) NULL DEFAULT NULL,
    `user_phone_number` VARCHAR(50) NULL DEFAULT NULL,
    `user_profile_img` VARCHAR(100) NULL DEFAULT NULL,
    `user_introduction` VARCHAR(300) NULL DEFAULT NULL,
    `user_activity_score` DOUBLE NULL DEFAULT NULL,
    `user_rating` DOUBLE NULL DEFAULT NULL,
    `country` VARCHAR(30) NULL DEFAULT NULL,
    `user_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `user_deleted_at` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`user_id`));


-- -----------------------------------------------------
-- Table `article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article` (
                                         `article_id` INT NOT NULL AUTO_INCREMENT,
                                         `writer_id` VARCHAR(50) NOT NULL,
    `article_title` VARCHAR(50) NULL DEFAULT NULL,
    `article_content` TEXT NULL DEFAULT NULL,
    `view_count` INT NULL DEFAULT NULL,
    `article_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_id`),
    CONSTRAINT `FK_user_TO_article_1`
    FOREIGN KEY (`writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);

-- -----------------------------------------------------
-- Table `article_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_comment` (
                                                 `article_comment_id` INT NOT NULL AUTO_INCREMENT,
                                                 `article_id` INT NOT NULL,
                                                 `comment_writer_id` VARCHAR(50) NOT NULL,
    `parent_comment_id` INT NULL DEFAULT NULL,
    `article_comment_content` VARCHAR(500) NULL DEFAULT NULL,
    `article_comment_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_comment_id`),
    CONSTRAINT `FK_article_TO_article_comment_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `article` (`article_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_article_comment_1`
    FOREIGN KEY (`comment_writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `article_comment_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_comment_like` (
                                                      `article_comment_id` INT NOT NULL,
                                                      `user_id` VARCHAR(50) NOT NULL,
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
    ON UPDATE RESTRICT);


-- -----------------------------------------------------
-- Table `course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course` (
                                        `course_id` INT NOT NULL AUTO_INCREMENT,
                                        `course_writer_id` VARCHAR(50) NOT NULL,
    `course_title` VARCHAR(50) NULL DEFAULT NULL,
    `course_content` TEXT NULL DEFAULT NULL,
    `province` VARCHAR(30) NULL DEFAULT NULL,
    `district` VARCHAR(30) NULL DEFAULT NULL,
    `town` VARCHAR(30) NULL DEFAULT NULL,
    `view_count` INT NULL DEFAULT NULL,
    `course_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`course_id`),
    CONSTRAINT `FK_user_TO_course_1`
    FOREIGN KEY (`course_writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);

-- -----------------------------------------------------
-- Table `course_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_comment` (
                                                `course_comment_id` INT NOT NULL AUTO_INCREMENT,
                                                `course_id` INT NOT NULL,
                                                `comment_writer_id` VARCHAR(50) NOT NULL,
    `parents_comment_id` INT NULL DEFAULT NULL,
    `course_comment_content` VARCHAR(500) NULL DEFAULT NULL,
    `course_comment_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`course_comment_id`),
    CONSTRAINT `FK_course_TO_course_comment_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_course_comment_1`
    FOREIGN KEY (`comment_writer_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `course_comment_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_comment_like` (
                                                     `course_comment_id` INT NOT NULL,
                                                     `user_id` VARCHAR(50) NOT NULL,
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
    ON UPDATE RESTRICT);

-- -----------------------------------------------------
-- Table `article_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_image` (
                                               `article_img_id` INT NOT NULL AUTO_INCREMENT,
                                               `article_id` INT NOT NULL,
                                               `article_image` VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (`article_img_id`),
    CONSTRAINT `FK_article_TO_article_image_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `article` (`article_id`)
    ON DELETE CASCADE);

-- -----------------------------------------------------
-- Table `article_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article_like` (
                                              `user_id` VARCHAR(50) NOT NULL,
    `article_id` INT NOT NULL,
    PRIMARY KEY (`user_id`,`article_id`),
    CONSTRAINT `FK_article_TO_article_like_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `article` (`article_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_article_like_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item` (
                                      `item_id` INT NOT NULL AUTO_INCREMENT,
                                      `user_id` VARCHAR(50) NOT NULL,
    `item_name` VARCHAR(50) NULL DEFAULT NULL,
    `main_category` VARCHAR(30) NULL DEFAULT NULL,
    `sub_category` VARCHAR(30) NULL DEFAULT NULL,
    `sub_subcategory` VARCHAR(30) NULL DEFAULT NULL,
    `item_price` INT NULL DEFAULT NULL,
    `address` VARCHAR(100) NULL DEFAULT NULL,
    `item_latitude` DOUBLE NULL DEFAULT NULL,
    `item_longitude` DOUBLE NULL DEFAULT NULL,
    `item_content` TEXT NULL DEFAULT NULL,
    `available_rental_start_date` TIMESTAMP NULL DEFAULT NULL,
    `available_rental_end_date` TIMESTAMP NULL DEFAULT NULL,
    `item_status` VARCHAR(50) NULL DEFAULT '대여 가능' COMMENT '대여 가능, 예약 중, 대여 중',
    `view_count` INT NULL DEFAULT 0,
    `item_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `item_deleted_at` TIMESTAMP NULL DEFAULT NULL,
    `thumbnail` VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (`item_id`),
    CONSTRAINT `FK_user_TO_item_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `chat_room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_room` (
                                           `room_id` INT NOT NULL AUTO_INCREMENT,
                                           `item_id` INT NOT NULL,
                                           `lessor_id` VARCHAR(50) NOT NULL,
    `lessee_id` VARCHAR(50) NOT NULL,
    `room_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`room_id`),
    CONSTRAINT `FK_item_TO_chat_room_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`),
    CONSTRAINT `FK_user_TO_chat_room_1`
    FOREIGN KEY (`lessor_id`)
    REFERENCES `user` (`user_id`),
    CONSTRAINT `FK_user_TO_chat_room_2`
    FOREIGN KEY (`lessee_id`)
    REFERENCES `user` (`user_id`));


-- -----------------------------------------------------
-- Table `chat_message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat_message` (
                                              `message_id` INT NOT NULL AUTO_INCREMENT,
                                              `room_id` INT NOT NULL,
                                              `sender_id` VARCHAR(50) NOT NULL,
    `message_content` TEXT NULL DEFAULT NULL,
    `message_img` VARCHAR(100) NULL DEFAULT NULL,
    `chat_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`message_id`),
    CONSTRAINT `FK_chat_room_TO_chat_message_1`
    FOREIGN KEY (`room_id`)
    REFERENCES `chat_room` (`room_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_chat_message_1`
    FOREIGN KEY (`sender_id`)
    REFERENCES `user` (`user_id`));


-- -----------------------------------------------------
-- Table `course_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_image` (
                                              `course_img_id` INT NOT NULL AUTO_INCREMENT,
                                              `course_id` INT NOT NULL,
                                              `course_img` VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (`course_img_id`),
    CONSTRAINT `FK_course_TO_course_image_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `course_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_like` (
                                             `course_id` INT NOT NULL,
                                             `user_id` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`course_id`,`user_id`),
    CONSTRAINT `FK_course_TO_course_like_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_course_like_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spot` (
                                      `spot_id` INT NOT NULL AUTO_INCREMENT,
                                      `spot_name` VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (`spot_id`));


-- -----------------------------------------------------
-- Table `course_spot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_spot` (
                                             `course_spot_id` INT NOT NULL AUTO_INCREMENT,
                                             `course_id` INT NOT NULL,
                                             `spot_id` INT NOT NULL,
                                             `visit_order` INT NULL DEFAULT NULL,
                                             PRIMARY KEY (`course_spot_id`),
    CONSTRAINT `FK_course_TO_course_spot_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`course_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_spot_TO_course_spot_1`
    FOREIGN KEY (`spot_id`)
    REFERENCES `spot` (`spot_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `item_trade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item_trade` (
                                            `trade_id` INT NOT NULL AUTO_INCREMENT,
                                            `item_id` INT NOT NULL,
                                            `lessor_id` VARCHAR(50) NOT NULL,
    `lessee_id` VARCHAR(50) NOT NULL,
    `trade_price` INT NULL DEFAULT NULL,
    `trade_deposit` INT NULL DEFAULT NULL,
    `payment_account_number` INT NULL DEFAULT NULL,
    `rental_start_date` TIMESTAMP NULL DEFAULT NULL,
    `rental_end_date` TIMESTAMP NULL DEFAULT NULL,
    `trade_state` VARCHAR(10) NULL DEFAULT '대여 전' COMMENT '\"대여 전\", \"대여 중\", \"반납 완료\"',
    `payment_status` VARCHAR(10) NULL DEFAULT '입금 전' COMMENT '\"입금 전\", \"입금 완료\"',
    `status_updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `payment_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`trade_id`),
    CONSTRAINT `FK_item_TO_item_trade_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`),
    CONSTRAINT `FK_user_TO_item_trade_1`
    FOREIGN KEY (`lessor_id`)
    REFERENCES `user` (`user_id`),
    CONSTRAINT `FK_user_TO_item_trade_2`
    FOREIGN KEY (`lessee_id`)
    REFERENCES `user` (`user_id`));


-- -----------------------------------------------------
-- Table `item_condition_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item_condition_image` (
                                                      `condition_image_id` INT NOT NULL AUTO_INCREMENT,
                                                      `trade_id` INT NOT NULL,
                                                      `condition_img` VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (`condition_image_id`),
    CONSTRAINT `FK_item_trade_TO_item_condition_image_1`
    FOREIGN KEY (`trade_id`)
    REFERENCES `item_trade` (`trade_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `item_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `item_image` (
                                            `item_img_id` INT NOT NULL AUTO_INCREMENT,
                                            `item_id` INT NOT NULL,
                                            `item_img` VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (`item_img_id`),
    CONSTRAINT `FK_item_TO_item_image_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `payment_status_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `payment_status_history` (
                                                        `status_history_id` INT NOT NULL AUTO_INCREMENT,
                                                        `status` VARCHAR(50) NULL DEFAULT NULL COMMENT '결제대기, 결제완료, 환불대기, 환불완료',
    `changed_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '상태 변경 일자',
    `remark` VARCHAR(100) NULL DEFAULT NULL COMMENT '상태 변경 설명 (예: 환불 사유)',
    `trade_id` INT NOT NULL,
    PRIMARY KEY (`status_history_id`),
    CONSTRAINT `FK_item_trade_TO_payment_status_history_1`
    FOREIGN KEY (`trade_id`)
    REFERENCES `item_trade` (`trade_id`)
                                                          ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `refresh_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `refresh_token` (
                                               `token_id` INT NOT NULL AUTO_INCREMENT,
                                               `user_id` VARCHAR(50) NOT NULL,
    `refresh_token` VARCHAR(255) NULL DEFAULT NULL,
    `expires_at` TIMESTAMP NULL DEFAULT NULL,
    `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`token_id`),
    CONSTRAINT `FK_user_TO_refresh_token_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`));


-- -----------------------------------------------------
-- Table `trade_review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trade_review` (
                                              `trade_review_id` INT NOT NULL AUTO_INCREMENT,
                                              `trade_id` INT NOT NULL,
                                              `author_user_id` VARCHAR(50) NOT NULL,
    `lessor_id` VARCHAR(50) NOT NULL,
    `lessee_id` VARCHAR(50) NOT NULL,
    `trade_review_content` VARCHAR(500) NULL DEFAULT NULL,
    `trade_review_rating` DOUBLE NULL DEFAULT NULL,
    `review_created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
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
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `user_chatroom`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_chatroom` (
                                               `user_chat_id` INT NOT NULL AUTO_INCREMENT,
                                               `room_id` INT NOT NULL,
                                               `user_id` VARCHAR(50) NOT NULL,
    `deleted_at` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`user_chat_id`),
    CONSTRAINT `FK_chat_room_TO_user_chatroom_1`
    FOREIGN KEY (`room_id`)
    REFERENCES `chat_room` (`room_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_user_chatroom_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);


-- -----------------------------------------------------
-- Table `wishlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wishlist` (
                                          `wish_id` INT NOT NULL AUTO_INCREMENT,
                                          `user_id` VARCHAR(50) NOT NULL,
    `item_id` INT NOT NULL,
    PRIMARY KEY (`wish_id`),
    CONSTRAINT `FK_item_TO_wishlist_1`
    FOREIGN KEY (`item_id`)
    REFERENCES `item` (`item_id`)
    ON DELETE CASCADE,
    CONSTRAINT `FK_user_TO_wishlist_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE);

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
INSERT INTO `spot` (`spot_name`)
VALUES ('Namsan Tower'),
       ('Han River Park'),
       ('Gyeongbok Palace');

-- Step 7: Insert data into `course` table
INSERT INTO `course` (`course_writer_id`, `course_title`, `course_content`, `province`, `district`, `town`,
                      `view_count`)
VALUES ('user1', 'Seoul Tour', '서울 투어 추천합니다.', 'Seoul', 'Jongno', 'Gwanghwamun', 100),
       ('user2', 'Busan Adventure', '부산 투어 추천합니다.', 'Busan', 'Haeundae', 'Haeundae Beach', 200),
       ('user3', 'Incheon Day Trip', '인천 투어 추천합니다.', 'Incheon', 'Songdo', 'Central Park', 150);

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

-- ----------------------------------- CHECK DATAS IN TABLE -----------------------------------
SELECT *
FROM `user`;
SELECT *
FROM `item`;
SELECT *
FROM `item_trade`;
SELECT *
FROM `course`;