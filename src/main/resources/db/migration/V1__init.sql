CREATE TABLE `stuff`
(
    `id`            bigint    NOT NULL AUTO_INCREMENT COMMENT '물건고유번호',
    `status`        tinyint   NOT NULL DEFAULT '0' COMMENT '물건상태 대기:0, 활성:1, 비활성:2',
    `name`          varchar(60)        DEFAULT NULL COMMENT '이름',
    `description`   varchar(255)       DEFAULT NULL COMMENT '설명',
    `make_datetime` timestamp NULL     DEFAULT NULL COMMENT '만든시각',
    `created_at`    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시각',
    `updated_at`    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '변경시각',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='물건';