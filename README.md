# Learning Management System (LMS)

## Overview

This project is a comprehensive Learning Management System (LMS) designed to streamline user authentication and authorization, course management, and student management. The system offers secure login/logout functionalities and provides role-based access control, differentiating between admin and normal users.

## Features

- **Secure Authentication and Authorization**: Users can log in and log out securely. Depending on their role, users gain access to different functionalities.
- **Role-Based Access**:
  - **Admin Users**: Full CRUD (Create, Read, Update, Delete) operations on students, courses, and users.
  - **Normal Users**: Restricted access with the ability to view student lists and course lists.
- **Course Management**: Admin users can manage courses, including creating, updating, and deleting course information.
- **Student Management**: Admin users can manage student records, including adding, updating, and removing student information.

## Technologies Used

- **Backend**: Spring Boot
- **Frontend**: Thymeleaf, Bootstrap
- **Database**: MySQL
- **IDE**: IntelliJ IDEA

## Getting Started

### Prerequisites

- JDK 11 or higher
- MySQL Database
- IntelliJ IDEA

### Database Setup (MySQL)

1. Find script named `spring_course.sql`.
2. Use the provided SQL scripts to set up your MySQL database:
    - Open your MySQL command line or a MySQL client like MySQL Workbench.
    - Run the `spring_course.sql` script to create the database schema.

### Project Setup (IntelliJ IDEA)

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/justoanff/Simple-LMS-App.git
   
2. Open the `Simple-LMS-App` folder in IntelliJ IDEA:
    - Ensure the project is imported as a Maven project.
  
3. Configure the MySQL database connection:
    - Open `src/main/resources/application.properties`.
    - Update the database connection properties:
    ```properties
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```
4. Run the application:
    - Locate the main application class annotated with `@SpringBootApplication`.
    - Right-click on the class and select `Run 'CourseRegistrationApplication'`.

5. The application should now be running.

6. Open a web browser and navigate to http://localhost:8080

## Usage

- Admin Login: Admins can log in to manage students, courses, and users.
- Normal User Login: Normal users can log in to view student and course lists.

## License

This project is licensed under the MIT License.
