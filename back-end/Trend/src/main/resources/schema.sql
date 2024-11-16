DROP
DATABASE IF EXISTS `trend`;
CREATE
DATABASE IF NOT EXISTS `trend`;
USE
`trend`;

-- ----------------------------------- CREATE TABLES -----------------------------------

CREATE TABLE `user`
(
    `user_id`           varchar(50) NOT NULL PRIMARY KEY,
    `user_password`     varchar(128) NULL,
    `user_nickname`     varchar(50) NULL,
    `user_address`      varchar(100) NULL,
    `user_email`        varchar(50) NULL,
    `user_phone_number` varchar(50) NULL,
    `user_profile_img`  varchar(100) NULL,
    `user_introduction` varchar(300) NULL,
    `user_activity_score` double NULL,
    `user_rating` double NULL,
    `country`           varchar(30) NULL,
    `user_created_at`   timestamp NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `item`
(
    `item_id`                     int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`                     varchar(50) NOT NULL,
    `item_name`                   varchar(50) NULL,
    `main_category`               varchar(30) NULL,
    `sub_category`                varchar(30) NULL,
    `sub_subcategory`             varchar(30) NULL,
    `item_price`                  int NULL,
    `country`                     varchar(30) NULL,
    `province`                    varchar(30) NULL,
    `district`                    varchar(30) NULL,
    `town`                        varchar(30) NULL,
    `item_latitude` double NULL,
    `item_longitude` double NULL,
    `item_content`                text NULL,
    `available_rental_start_date` timestamp NULL,
    `available_rental_end_date`   timestamp NULL,
    `item_status`                 varchar(50) NULL COMMENT '공개, 비공개, 대여 중',
    `item_registed_at`            timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `view_count`                  int default 0
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
    `payment_status`         varchar(10) NULL COMMENT '"입금 전", "입금 완료"',
    `payment_created_at`     timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `status_updated_at`      timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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
    `trade_review_rating` double NULL,
    `review_created_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `spot`
(
    `spot_id`   int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `spot_name` varchar(50) NULL
);

CREATE TABLE `course`
(
    `course_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `course_writer_id`  varchar(50) NOT NULL,
    `course_title`      varchar(50) NULL,
    `province`          varchar(30) NULL,
    `district`          varchar(30) NULL,
    `town`              varchar(30) NULL,
    `view_count`        int NULL,
    `course_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `course_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `course_comment`
(
    `course_comment_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `course_id`                 int         NOT NULL,
    `comment_writer_id`         varchar(50) NOT NULL,
    `parents_comment_id`        int NULL,
    `course_comment_content`    varchar(500) NULL,
    `course_comment_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `course_comment_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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
    `status`            varchar(50) NULL COMMENT '결제대기, 결제완료, 환불대기, 환불완료',
    `changed_at`        timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '상태 변경 일자',
    `remark`            varchar(100) NULL COMMENT '상태 변경 설명 (예: 환불 사유)',
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
    `view_count`         int NULL,
    `article_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `article_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `article_comment`
(
    `article_comment_id`         int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `article_id`                 int         NOT NULL,
    `comment_writer_id`          varchar(50) NOT NULL,
    `parent_comment_id`          int NULL,
    `article_comment_content`    varchar(500) NULL,
    `article_comment_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `article_comment_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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
    `room_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `chat_message`
(
    `message_id`      int         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `room_id`         int         NOT NULL,
    `sender_id`       varchar(50) NOT NULL,
    `message_content` text NULL,
    `message_img`     varchar(100) NULL,
    `chat_created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `refresh_token`
(
    `token_id`      bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`       varchar(50) NOT NULL,
    `refresh_token` varchar(255) NULL,
    `expires_at`    timestamp NULL,
    `created_at`    timestamp NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `item_image`
(
    `item_img_id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `item_id`     int NOT NULL,
    `item_img`    varchar(50) NULL
);

CREATE TABLE `course_image`
(
    `course_img_id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `course_id`     int NOT NULL,
    `course_img`    varchar(100) NULL
);

CREATE TABLE `article_image`
(
    `article_img_id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `article_id`     int NOT NULL,
    `article_image`  varchar(100) NULL
);

CREATE TABLE `item_condition_image`
(
    `condition_image_id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `trade_id`           int NOT NULL,
    `condition_img`      varchar(100) NULL
);
-- ----------------------------------- SET FOREIGN KEY -----------------------------------
ALTER TABLE `item`
    ADD CONSTRAINT `FK_user_TO_item_1` FOREIGN KEY (
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
            )
            ON DELETE CASCADE;

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
            )
            ON DELETE CASCADE;

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

ALTER TABLE `course_spot`
    ADD CONSTRAINT `FK_spot_TO_course_spot_1` FOREIGN KEY (
                                                           `spot_id`
        )
        REFERENCES `spot` (
                           `spot_id`
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
            )
            ON DELETE CASCADE;

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

ALTER TABLE `refresh_token`
    ADD CONSTRAINT `FK_user_TO_refresh_token_1` FOREIGN KEY (
                                                             `user_id`
        )
        REFERENCES `user` (
                           `user_id`
            );

ALTER TABLE `item_image`
    ADD CONSTRAINT `FK_item_TO_item_image_1` FOREIGN KEY (
                                                          `item_id`
        )
        REFERENCES `item` (
                           `item_id`
            )
            ON DELETE CASCADE;

ALTER TABLE `course_image`
    ADD CONSTRAINT `FK_course_TO_course_image_1` FOREIGN KEY (
                                                              `course_id`
        )
        REFERENCES `course` (
                             `course_id`
            );

ALTER TABLE `article_image`
    ADD CONSTRAINT `FK_article_TO_article_image_1` FOREIGN KEY (
                                                                `article_id`
        )
        REFERENCES `article` (
                              `article_id`
            );

ALTER TABLE `item_condition_image`
    ADD CONSTRAINT `FK_item_trade_TO_item_condition_image_1` FOREIGN KEY (
                                                                          `trade_id`
        )
        REFERENCES `item_trade` (
                                 `trade_id`
            );


----------------------------------- INSERT DUMMY DATAS INTO TABLES ---------------------------------
use
trend;

-- Step 1: Insert data into `user` table
INSERT INTO `user` (`user_id`, `user_password`, `user_nickname`, `user_address`, `user_email`, `user_phone_number`,
                    `user_profile_img`, `user_introduction`, `user_activity_score`, `user_rating`, `country`)
VALUES ('user1', 'password1', 'nickname1', '서울시 강서구 등촌동', 'user1@example.com', '010-1111-1111', 'profile1.jpg',
        'Introduction 1', 10, 4.5, '대한민국'),
       ('user2', 'password2', 'nickname2', '서울시 강남구 역삼동', 'user2@example.com', '010-2222-2222', 'profile2.jpg',
        'Introduction 2', 20, 4.0, '대한민국'),
       ('user3', 'password3', 'nickname3', '인천시 연수구 송도동', 'user3@example.com', '010-3333-3333', 'profile3.jpg',
        'Introduction 3', 15, 3.8, '미쿡');

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

-- Step 6: Insert data into `spot` table
INSERT INTO `spot` (`spot_name`)
VALUES ('Namsan Tower'),
       ('Han River Park'),
       ('Gyeongbok Palace');

-- Step 7: Insert data into `course` table
INSERT INTO `course` (`course_writer_id`, `course_title`, `province`, `district`, `town`, `view_count`)
VALUES ('user1', 'Seoul Tour', 'Seoul', 'Jongno', 'Gwanghwamun', 100),
       ('user2', 'Busan Adventure', 'Busan', 'Haeundae', 'Haeundae Beach', 200),
       ('user3', 'Incheon Day Trip', 'Incheon', 'Songdo', 'Central Park', 150);

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