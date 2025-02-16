# QuickChat - A Real-time Chat Application

## Overview
QuickChat is a real-time chat application that enables users to communicate with each other seamlessly. It is built using **Spring Boot** for the backend and **MongoDB** as the database. The application follows a structured and scalable design, ensuring secure authentication and efficient data management.

## Tech Stack
- **Backend:** Spring Boot (Gradle 8)
- **Database:** MongoDB
- **Authentication:** JWT-based authentication
- **API Testing:** Postman

## Features
- User authentication with JWT
- Secure chat system using role-based user identification
- Real-time message storage and retrieval
- Scalable data model with optimized structure

## Project Structure
```
quickchat/
├── src/main/java/com/app/quickchat/
│   ├── controller/       # Handles API requests
│   ├── service/          # Business logic layer
│   ├── repository/       # Data access layer
│   ├── model/            # Data models (User, Chat, etc.)
│   ├── config/           # Configuration files
│   └── QuickChatApplication.java   # Main application file
├── src/main/resources/
│   ├── application.yml   # Configuration settings
├── build.gradle          # Gradle dependencies
├── README.md             # Project documentation
```


## Setup Instructions
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/quickchat.git
   ```
2. Navigate to the project directory:
   ```sh
   cd quickchat
   ```
3. Build the project:
   ```sh
   ./gradlew build
   ```
4. Run the application:
   ```sh
   ./gradlew bootRun
   ```
5. The server will start on `http://localhost:8080/`

## Future Enhancements
- WebSocket integration for real-time messaging
- User presence status (online/offline)
- Message read receipts
- Deployment to cloud infrastructure
- AI addition

## License
This project is yet to be licensed.

