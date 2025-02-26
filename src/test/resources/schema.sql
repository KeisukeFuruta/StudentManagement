CREATE TABLE students(
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(191) NOT NULL,
    furigana VARCHAR(191) NOT NULL,
    nickname VARCHAR(191) NOT NULL,
    email_address VARCHAR(191) NOT NULL,
    residential_area VARCHAR(191) NOT NULL,
    age INTEGER NOT NULL,
    gender VARCHAR(191) NOT NULL,
    remark VARCHAR(191),
    is_deleted boolean
);

CREATE TABLE students_courses(
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_name VARCHAR(191) NOT NULL,
    start_date DATE,
    expected_end_date DATE
);

CREATE TABLE course_status(
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    status enum('仮申込','本申込','受講中','受講終了')
);
