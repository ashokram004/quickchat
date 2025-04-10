# QuickChat - A Real-time Chat Application

## Overview
QuickChat is a powerful, real-time chat application designed to provide users with seamless communication. Built with **Spring Boot** for the backend, **MongoDB** as the database, and leveraging **JWT-based authentication** for secure login, it delivers fast and scalable messaging capabilities. The app incorporates **WebSockets** for real-time communication, allowing users to stay connected with minimal delay.

## Tech Stack
- **Backend:** Spring Boot (Gradle 8)
- **Database:** MongoDB
- **Authentication:** JWT-based authentication
- **Real-Time Communication:** WebSockets (STOMP protocol)
- **API Testing:** Postman
- **Styling:** Tailwind CSS (Frontend)
- **UI Animations:** Framer Motion (Frontend)

## Features
- 🔐 **Secure Authentication with JWT**  
  Robust user authentication with JSON Web Tokens (JWT), ensuring secure, stateless sessions for all users.

- 💬 **Real-Time Messaging with WebSockets**  
  Instant, bidirectional messaging using WebSockets for seamless real-time communication between users.

- 🧠 **Smart Chat Synchronization**  
  Efficient chat state management that ensures chat windows only update when relevant messages are received, avoiding unnecessary re-renders.

- 🗂️ **Scalable & Optimized Data Model**  
  Well-structured data models for users, chats, and messages to support scalable growth as the application expands.

- 🎨 **Modern UI with Tailwind CSS & Framer Motion**  
  A sleek and responsive user interface built with Tailwind CSS and smooth transitions powered by Framer Motion.

- 📦 **Fallback Message Delivery**  
  A REST API fallback mechanism ensures that messages are still delivered if the WebSocket connection is temporarily unavailable.

- 🧪 **Robust Error Handling**  
  Comprehensive error handling and logging on both frontend and backend to ensure a smooth user experience and facilitate debugging.

## Project Structure
quickchat/
├── backend/
│   ├── src/main/java/com/app/quickchat/
│   │   ├── controller/         # Handles API requests (e.g., LoginController, ChatController)
│   │   ├── service/            # Business logic layer (e.g., UserService, ChatService)
│   │   ├── repository/         # Data access layer (e.g., UserRepository, ChatRepository)
│   │   ├── model/              # Data models (e.g., User, Chat, Message)
│   │   ├── config/             # Configuration files (e.g., JWTConfig, WebSocketConfig)
│   │   └── QuickChatApplication.java # Main application file
│   ├── src/main/resources/
│   │   ├── application.yml     # Configuration settings (database, WebSocket, etc.)
│   │   ├── static/             # Static resources (if any)
│   │   └── templates/          # Templates (if using Thymeleaf or similar)
│   ├── build.gradle            # Gradle dependencies
│   └── settings.gradle         # Gradle settings
│
├── frontend/
│   ├── public/                 # Public assets (e.g., index.html, favicon)
│   ├── src/
│   │   ├── components/         # Reusable React components (e.g., ChatBox, MessageList)
│   │   ├── pages/              # Page-level components (e.g., LoginPage, ChatPage)
│   │   ├── redux/              # Redux store, actions, reducers
│   │   ├── styles/             # CSS or SCSS files
│   │   ├── utils/              # Utility functions (e.g., WebSocket connection, API calls)
│   │   ├── App.jsx             # Main React component
│   │   ├── api.js              # Axios configuration and API calls
│   │   └── index.js            # React entry point
│   ├── package.json            # Frontend dependencies
│   ├── package-lock.json       # Lockfile for npm
│   └── webpack.config.js       # Webpack configuration (if applicable)
│
├── README.md                   # Project documentation
├── .gitignore                  # Git ignore file
└── .env                        # Environment variables (if any)

bash
Copy
Edit

## Setup Instructions
1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-username/quickchat.git
Navigate to the project directory:

sh
Copy
Edit
cd quickchat
Build the project:

sh
Copy
Edit
./gradlew build
Run the application:

sh
Copy
Edit
./gradlew bootRun
The server will start on http://localhost:8080/.

API Endpoints
POST /users/login
User login with JWT authentication.

POST /users/register
User registration for creating a new account.

GET /chats/{chatId}
Retrieve messages from a specific chat.

POST /chats/sendMessage/{chatId}
Send a message to a specified chat.

Future Enhancements
🟢 User Presence Status
Display user status (online/offline) in the chat interface.

🟡 Message Read Receipts
Introduce message read receipts, displaying an indicator when a message has been read by the recipient.

🟣 Deployment to Cloud Infrastructure
Prepare the app for deployment on cloud platforms like AWS, Google Cloud, or Heroku.

🤖 AI Integration
Add AI-based features, such as smart chatbots, automatic replies, or sentiment analysis.

License
This project is yet to be licensed.