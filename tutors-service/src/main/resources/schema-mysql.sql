USE `tutors-db`;

create table if not exists tutors(
                                     id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                     tutor_id VARCHAR(36),
    tutor_name VARCHAR (50),
    tutor_age VARCHAR(50),
    tutor_email VARCHAR(50),
    specialty VARCHAR(50),
    experience VARCHAR(50)
    );