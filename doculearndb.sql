-- Xóa database nếu đã tồn tại
DROP DATABASE IF EXISTS doculearndb;

-- Tạo lại database
CREATE DATABASE doculearndb;

use doculearndb;
-- ============================ khóa học → summary (tóm tắt chương) → câu hỏi → các lựa chọn.
-- Bảng khóa học (môn A, B,C)
CREATE TABLE Course (  
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATETIME,
    end_date DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('DRAFT', 'PUBLISH', 'CLOSED') NOT NULL DEFAULT 'DRAFT'
);


-- Bảng Summary (Mối quan hệ N - 1 với Subject)
CREATE TABLE Summary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    title VARCHAR(255) NOT NULL, -- chuong 1,2,3
    sections json,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('DRAFT', 'PUBLISH', 'CLOSED') NOT NULL DEFAULT 'DRAFT',
    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE
);

-- Bảng Question (Mối quan hệ N - 1 với Summary )
CREATE TABLE Question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    summary_id INT,
    course_id INT NOT NULL,
    content TEXT NOT NULL,
    response_type ENUM('SINGLE_CHOICE', 'TEXT') NOT NULL DEFAULT 'SINGLE_CHOICE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
	update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE cascade,
    FOREIGN KEY (summary_id) REFERENCES Summary(id) ON DELETE set null
);

-- Bảng  Option (Mối quan hệ N - 1 question)
CREATE TABLE QuestionOptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    content VARCHAR(200),
    is_correct Boolean,
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES Question(id) ON DELETE CASCADE
);





-- 1. Thêm 1 khóa học
INSERT INTO Course (title, description, start_date, end_date, status)
VALUES (
    'Khóa học Nhập môn Trí tuệ nhân tạo',
    JSON_OBJECT('level', 'Cơ bản', 'language', 'Tiếng Việt'),
    '2025-09-01 00:00:00',
    '2025-12-01 00:00:00',
    'PUBLISH'
);

-- 2. Thêm 1 summary thuộc khóa học trên
INSERT INTO Summary (course_id, title, sections, status)
VALUES (
    1,
    'Chương 1: Giới thiệu AI',
    JSON_ARRAY(
        JSON_OBJECT('heading', '1.1 Lịch sử AI', 'content', 'AI bắt đầu từ...'),
        JSON_OBJECT('heading', '1.2 Ứng dụng AI', 'content', 'AI trong y tế, giáo dục...')
    ),
    'PUBLISH'
);

-- 3. Thêm 1 câu hỏi thuộc summary & course
INSERT INTO Question (summary_id, course_id, content, response_type)
VALUES (
    1,
    1,
    'AI là gì?',
    'SINGLE_CHOICE'
);

-- 4. Thêm 2 lựa chọn cho câu hỏi
INSERT INTO QuestionOptions (question_id, content, is_correct)
VALUES
    (1, 'Một lĩnh vực của CNTT', FALSE),
    (1, 'Một ngành của trí tuệ nhân tạo nghiên cứu cách mô phỏng hành vi con người', TRUE);





