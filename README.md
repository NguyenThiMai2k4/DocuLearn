# 📑 DocuLearn - Smart Learning Support from LTI-Integrated Course Materials
This project introduces a smart Tool Provider integrated via the LTI standard, enabling seamless connection with LMS platforms like Moodle or Canvas. The tool allows instructors to upload teaching materials, then automatically generates summaries and practice questions using OpenAI’s natural language processing. Built with Spring Boot and MySQL, it helps students quickly grasp core content while reducing preparation time for teachers, contributing to more effective digital learning and paving the way for future AI-powered educational tools.
## 🛠 Tech Stack
The project is built with a modern and scalable stack:
- **Backend:** Spring Boot (≥ 2.7) for API development, with Thymeleaf (3.x) for server-side rendering.
- **Database:** MySQL 8.0+ for relational data management with JSON support.
- **Testing:** Postman (10.x) for API simulation and validation.
- **AI Integration:** OpenAI API (≥ GPT-3.5-Turbo; GPT-4+ recommended) for natural language processing, content summarization, and question generation.
- **LTI 1.3** — LMS integration (Canvas, Moodle, etc.)
- **Development Tools:** IntelliJ IDEA Community 2023.1 for coding and Git 2.40+ for version control.

## 📂 Project Structure
<img width="200" alt="image" src="https://github.com/user-attachments/assets/e1859e6b-4cf5-4dfb-a24a-99b0cf47e208" /> 

## ⚙️ Installation
**Prerequisites** <br>
Ensure you have **JDK 17+** and **mvn** installed.
**Steps to Run the Project** <br>
1. Clone the repository
   ```bash
   
2. Configure the database
   - Ensure MySQL 8.0+ is running.
   - Create a database (e.g., doculearn).
   - Update the .env file or application.properties with your credentials:
     ```bash
     spring.datasource.url=jdbc:mysql://localhost:3306/doculearn?useSSL=false&serverTimezone=UTC
     spring.datasource.username=YOUR_DB_USER
     spring.datasource.password=YOUR_DB_PASS
     spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
     
3. Set up environment variables (for LTI & OpenAI)
   ```bash
   # OpenAI Configuration
   OPENAI_API_KEY=your_openai_api_key
   # Ngrok to public internet (if you want to deploy)
   ngrok.url=  https://your-random-key.ngrok-free.app
4. Build and run the application
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
5. Access the application
   Open your browser at: <br>
    ```bash
    http://localhost:8080

===================  ✨ Thank you for taking the time to explore this project.  <br>
                      Wishing you a smooth setup and a successful deployment! 🚀 ===================
   
