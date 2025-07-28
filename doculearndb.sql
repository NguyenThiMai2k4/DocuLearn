-- Xóa database nếu đã tồn tại
DROP DATABASE IF EXISTS doculearndb;

-- Tạo lại database
CREATE DATABASE doculearndb;

use doculearndb;

-- bảng chính
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,        -- Tên người dùng
    birthday DATE,                     -- Ngày sinh nhật
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_TEACHER') NOT NULL,
    bio TEXT,                          -- Tiểu sử / giới thiệu bản thân
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE,   -- Chờ admin xác nhận
    avatar VARCHAR(255)
);

-- Bảng Teacher (mối quan hệ 1-1 với User)
CREATE TABLE Teacher (
    user_id INT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- Bảng Admin (mối quan hệ 1-1 với User)
CREATE TABLE Admin (
    user_id INT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- Bảng Student (mối quan hệ 1-1 với User)
CREATE TABLE Student (
    user_id INT PRIMARY KEY,
    student_code VARCHAR(50) UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- Bảng Subject (Mối quan hệ N - 1 với Admin, Teacher)
CREATE TABLE Subject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,		-- Mỗi môn học bắt buộc phải được tạo bởi một (Admin)
    teacher_id INT NOT NULL,	-- Mỗi môn học cũng bắt buộc phải có một (Teacher) 
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATETIME,
    end_date DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('DRAFT', 'PUBLISH', 'CLOSED') NOT NULL DEFAULT 'DRAFT',
    FOREIGN KEY (admin_id) REFERENCES Admin(user_id) ON DELETE CASCADE,
	FOREIGN KEY (teacher_id) REFERENCES Teacher(user_id) ON DELETE CASCADE
);

-- Bảng StudentEnrollment sinh ra từ mối quan hệ nhiều nhiều giữa user và inviation post
CREATE TABLE StudentEnrollment (
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    enrolled_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id, subject_id),
    FOREIGN KEY (student_id) REFERENCES Student(user_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES Subject(id) ON DELETE CASCADE
);

-- Bảng Summary (Mối quan hệ N - 1 với Subject)
CREATE TABLE Summary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATETIME,
    end_date DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('DRAFT', 'PUBLISH', 'CLOSED') NOT NULL DEFAULT 'DRAFT',
    FOREIGN KEY (subject_id) REFERENCES Subject(id) ON DELETE CASCADE
);

-- Bảng Question (Mối quan hệ N - 1 với Summary )
CREATE TABLE SummaryQuestion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    summary_id INT,
    subject_id INT NOT NULL,
    content TEXT NOT NULL,
    response_type ENUM('SINGLE_CHOICE', 'TEXT') NOT NULL DEFAULT 'SINGLE_CHOICE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES Subject(id) ON DELETE cascade,
    FOREIGN KEY (summary_id) REFERENCES Summary(id) ON DELETE set null
);

-- Bảng  Option (Mối quan hệ N - 1 question)
CREATE TABLE SummaryOption (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    content VARCHAR(200),
    is_correct Boolean,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES SummaryQuestion(id) ON DELETE CASCADE
);

-- Bảng ResponseOption sinh ra từ uSER VÀ Survey Option
CREATE TABLE ResponseOption (
    user_id INT NOT NULL,
    option_id INT NOT NULL,
    responded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, option_id),
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES SummaryOption(id) ON DELETE CASCADE
);



INSERT INTO User (username, name, birthday, gender, password, role, bio, is_active, avatar) VALUES
('admin1', 'Mai', '1990-01-01', 'FEMALE', '123', 'ROLE_ADMIN', 'I am admin', TRUE, 'avatar1.jpg'),
('teacher1', 'Mr.An', '1985-05-10', 'MALE', '123', 'ROLE_TEACHER', 'I teach math', TRUE, 'avatar2.jpg'),
('student1', 'Phuong', '2002-09-15', 'MALE', '123', 'ROLE_STUDENT', 'I love learning', TRUE, 'avatar3.jpg');


INSERT INTO Admin (user_id) VALUES (1);
INSERT INTO Teacher (user_id) VALUES (2);
INSERT INTO Student (user_id, student_code) VALUES 
(3, '2251010077'),
(1, '2251010062'),  -- giả lập thêm admin cũng làm student
(2, 'STU003');  -- giả lập thêm teacher cũng làm student

INSERT INTO Subject (admin_id, teacher_id, title, description, start_date, end_date, status) VALUES
(1, 2, 'Pháp luật đại cương', 'Nguồn gốc, bản chất, đặc điểm, chức năng của Nhà nước, kiểu nhà nước, hình thức nhà nước và phân tích cấu trúc của bộ máy Nhà nước, chức năng và thẩm quyền của các cơ quan nhà nước trong bộ máy Nhà nước Việt Nam.', '2025-08-01', '2025-12-01', 'PUBLISH'),
(1, 2, 'Khai phá dữ liệu', 'Basic physics course', '2025-09-01', '2025-12-31', 'DRAFT'),
(1, 2, 'Kiểm thử phần mềm', 'Intro to TEST CASE', '2025-08-15', '2025-11-15', 'PUBLISH');

INSERT INTO StudentEnrollment (student_id, subject_id) VALUES
(3, 1),
(3, 2),
(3, 3);

INSERT INTO Summary (subject_id, title, description, start_date, end_date, status) VALUES
(1, 'CHƯƠNG 1', '08552d7f-cfcf-4d7c-9290-45b3217d6991_summary', '2025-08-01', '2025-08-07', 'PUBLISH');

INSERT INTO SummaryQuestion (summary_id, subject_id ,content, response_type) VALUES
(1,1, 'What is 2+2?', 'SINGLE_CHOICE'),
(1,1, 'Define velocity.', 'TEXT');


INSERT INTO SummaryOption (question_id, content, is_correct) VALUES
(1, '4', TRUE),
(1, '3', FALSE);

INSERT INTO ResponseOption (user_id, option_id) VALUES
(3, 1);




