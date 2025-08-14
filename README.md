# DocuLearn
- Mô tả: Website thiết kế dưới dạng tool provider nhằm hổ trợ bổ sung cho Moodle chức năng chính là Upload file pdf -> generate tóm tắt và các câu hỏi dưới dạng trắc nghiệm (Single-choice) & Tự luận.
- Công nghệ chính:
  + Backend: Spring boot, nodejs, LTI.
  + Fontend: ...

## Mục lục
4. Cài đặt và chạy thử:
   4.1. Yêu cầu hệ thống
   - Java: JDK 23.0.2
   - CSDL: MySQL 
   - Git: để clone poject

  4.2. Clone project 
    git clone https://github.com/username/project.git
    cd project

  4.3. Cấu hình CSDL MySQL 
    b1: Sử dụng install "MySQL Workbench"
    b2: Setup new connection với:
        -Username: root
        -password: root
    b3: Trong "MySQL Workbench", khởi tạo một sql rỗng
    b4: paste nội dung trong file "doculearndb.sql" trong folder Doculearn sau khi clone về 
    b5: run file "doculearndb.sql" to finish config database.

  4.4. Cấu hình file application.properties
      spring.application.name=demo
      server.servlet.context-path=/doculearn
      
      spring.servlet.multipart.max-file-size=20MB
      spring.servlet.multipart.max-request-size=20MB
    
      #key openAPI không thể public. Cần đến link: <i>https://platform.openai.com/api-keys</i> Tạo Key và paste key vào "spring.ai.openai.api-key"
      spring.ai.openai.api-key="path your key-openAPI"
      
      #database source 
      spring.datasource.url=jdbc:mysql://localhost:3306/doculearndb
      spring.datasource.username=root
      spring.datasource.password=root
      
      spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
      spring.jpa.show-sql=true
      spring.jpa.properties.hibernate.format_sql=true

   4.5.  Chạy web test
      - b1: chạy lệnh "mvn clean install" trong terminal để build các thư viện trong project.
      - b2: run file "DemoApplication".
      - b3: Đợi 1 khoảng thời gian để hệ thống đọc các file và dữ liệu.
      - b4: Mở trình duyệt chrome nhập đường link: http://localhost:8080/doculearn/  
      HOÀN THÀNH
      
  
  



    
   
    
