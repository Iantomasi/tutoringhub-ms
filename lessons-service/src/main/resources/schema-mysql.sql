USE `lessons-db`;

create table if not exists lessons(

                                      id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                      lesson_id VARCHAR(36),
    lesson_subject VARCHAR(50),
    lesson_date VARCHAR(50),
    lesson_duration VARCHAR(50),
    lesson_classroom VARCHAR(50),
    lesson_status VARCHAR (50),
    street_address VARCHAR (50),
    city VARCHAR(50),
    postal_code VARCHAR(9)
    );