# COMS309
OneCampus is a matchmaking application designed to connect students on campus based on their needs and preferences, such as finding mentors, dorm matches, or peers in the same major. Inspired by platforms like Tinder, it pairs students through a swipe-based system, helping build meaningful connections within the campus community.

Features
User Registration & Authentication
Secure login system for students to create and manage profiles.

Survey-Based Matching
Users complete a survey upon login to specify preferences (e.g., mentor/mentee roles, major, interests).

Real-Time Matching
Matches users with compatible peers using WebSocket technology for instant notifications.

Chat Functionality
Integrated chat system for seamless communication, powered by OpenAI for enhanced messaging.

Customizable User Profiles
Profiles include information such as classification (e.g., freshman, sophomore) and areas of mentorship or interest.

Full-Stack Implementation
Built with SpringBoot and Java for backend management, Android Studio for frontend development, and Gradle/Maven for build management.

Technology Stack
Frontend: Android Studio
Backend: SpringBoot, WebSocket
Build Tools: Gradle, Maven
Database: PostgreSQL
Programming Language: Java
Additional Libraries: OpenAI for chat enhancement
Installation and Setup
Clone the Repository

bash
Copy code
git clone https://github.com/your-repo/one-campus.git
cd one-campus
Set Up Backend

Ensure you have Java JDK 21 installed.
Build the backend using Gradle or Maven.
Configure the database connection in application.properties.
Set Up Frontend

Open the Android project in Android Studio.
Connect it to the backend endpoints.
Run the Application

Start the backend server with:
bash
Copy code
./gradlew bootRun
Deploy the Android app on an emulator or a physical device.
