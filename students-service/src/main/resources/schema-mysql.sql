USE `students-db`;

create table if not exists students(
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(36),
    student_name VARCHAR (50),
    student_age VARCHAR(50),
    student_email VARCHAR(50),
    student_school VARCHAR (50)
);