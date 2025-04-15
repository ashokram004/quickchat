# QuickChat - A Real-time Chat Application

## Overview
QuickChat is a powerful, real-time chat application designed to provide users with seamless communication. Built with **Spring Boot** for the backend, **MongoDB** as the database, and leveraging **JWT-based authentication** for secure login, it delivers fast and scalable messaging capabilities. The app incorporates **WebSockets** for real-time communication, allowing users to stay connected with minimal delay.

## Tech Stack
- **Frontend:** ReactJs, Vite
- **Backend:** Spring Boot (Gradle 8)
- **Database:** MongoDB
- **Authentication:** JWT-based authentication
- **Real-Time Communication:** WebSockets (STOMP protocol)
- **API Testing:** Postman
- **Styling:** Tailwind CSS (Frontend)
- **UI Animations:** Framer Motion (Frontend)

## Features
- 🔐 **Secure Authentication with JWT**  
    Robust user authentication with JSON Web Tokens (JWT) and encrypted passwords, ensuring secure, stateless sessions for all users.

- 💬 **Real-Time Messaging with WebSockets**  
    Instant, bidirectional messaging using WebSockets for seamless real-time communication between users.

- 🧠 **Smart Chat Synchronization**  
    Efficient chat state management that ensures chat windows only update when relevant messages are received, avoiding unnecessary re-renders.

- 🗂️ **Scalable & Optimized Data Model**  
    Well-structured data models for users, chats, and messages to support scalable growth as the application expands.

- 🎨 **Modern UI with Tailwind CSS & Framer Motion**  
    A sleek and responsive user interface built with Tailwind CSS and smooth transitions powered by Framer Motion.

- 🌈 **Theme Customization**  
    Personalize your chat experience with multiple built-in color schemes and themes to match user preferences.

- 📦 **Fallback Message Delivery**  
    A REST API fallback mechanism ensures that messages are still delivered if the WebSocket connection is temporarily unavailable.

- 🧪 **Robust Error Handling**  
    Comprehensive error handling and logging on both frontend and backend to ensure a smooth user experience and facilitate debugging.

## Project Structure
```
quickchat/
├── backend/
│   ├── src/main/java/com/app/quickchat/
│   │   ├── controller/        # Handles API requests (e.g., LoginController, ChatController)
│   │   ├── service/           # Business logic layer (e.g., UserService, ChatService)
│   │   ├── repository/        # Data access layer (e.g., UserRepository, ChatRepository)
│   │   ├── model/             # Data models (e.g., User, Chat, Message)
│   │   ├── config/            # Configuration files (e.g., JWTConfig, WebSocketConfig)
│   │   └── QuickChatApplication.java # Main application file
│   ├── src/main/resources/
│   │   ├── application.yml    # Configuration settings (database, WebSocket, etc.)
│   │   ├── static/            # Static resources (if any)
│   │   └── templates/         # Templates (if using Thymeleaf or similar)
│   ├── build.gradle           # Gradle dependencies
│   └── settings.gradle        # Gradle settings
├── frontend/
│   ├── public/                # Public assets (e.g., index.html, favicon)
│   ├── src/
│   │   ├── components/        # Reusable React components (e.g., ChatBox, MessageList)
│   │   ├── pages/             # Page-level components (e.g., LoginPage, ChatPage)
│   │   ├── redux/             # Redux store, actions, reducers
│   │   ├── styles/            # CSS or SCSS files
│   │   ├── utils/             # Utility functions (e.g., WebSocket connection, API calls)
│   │   ├── App.jsx            # Main React component
│   │   ├── api.js             # Axios configuration and API calls
│   │   └── index.js           # React entry point
│   ├── package.json           # Frontend dependencies
│   ├── package-lock.json      # Lockfile for npm
│   └── webpack.config.js      # Webpack configuration (if applicable)
├── README.md                  # Project documentation
├── .gitignore                 # Git ignore file
└── .env                       # Environment variables (if any)
```

## Setup Instructions

### Backend
1. **Clone the repository:**
     ```sh
     git clone https://github.com/ashokram004/quickchat.git
     ```

2. **Navigate to the backend directory:**
     ```sh
     cd quickchat/backend
     ```

3. **Build the project:**
     ```sh
     ./gradlew build
     ```

4. **Run the application:**
     ```sh
     ./gradlew bootRun
     ```

5. The server will start on `http://localhost:8080/`.

### Frontend
1. **Navigate to the frontend directory:**
     ```sh
     cd quickchat/frontend
     ```

2. **Install dependencies:**
     ```sh
     npm install
     ```

3. **Start the development server:**
     ```sh
     npm start
     ```

4. The frontend will start on `http://localhost:3000/`.

## API Endpoints

### **User Controller**

- **GET /users**  
    Retrieve all users from the system.  
    **Response:**  
    A list of all users.

- **GET /users/{mobileNo}**  
    Retrieve a specific user by their mobile number.  
    **Path Parameter:**  
    `mobileNo` - The mobile number of the user to retrieve.  
    **Response:**  
    Details of the user with the specified mobile number.

- **GET /users/search**  
    Search for users based on a mobile number prefix.  
    **Request Parameter:**  
    `mobileNo` - The mobile number prefix to search for.  
    **Response:**  
    A list of mobile numbers matching the search prefix.

- **POST /users/login**  
    User login with JWT authentication.  
    **Request Body:**  
    `User` - The user's mobile number and password.  
    **Response:**  
    A JWT token and the user data upon successful login.

- **POST /users/register**  
    User registration for creating a new account.  
    **Request Body:**  
    `User` - The user’s mobile number, name, and password.  
    **Response:**  
    A success message and user creation status.

- **PUT /users/update/{mobileNo}**  
    Update user details for a given mobile number.  
    **Path Parameter:**  
    `mobileNo` - The mobile number of the user to update.  
    **Request Body:**  
    `User` - The updated user details.  
    **Response:**  
    Updated user details upon successful update.

---

### **Chat Controller**

- **GET /chats/fetchMessages/{chatId}**  
    Retrieve messages from a specific chat.  
    **Path Parameter:**  
    `chatId` - The ID of the chat to fetch messages from.  
    **Response:**  
    A list of messages in the chat.

- **POST /chats/initiateChat/{chatId}**  
    Initiate a new chat with the given `chatId`.  
    **Path Parameter:**  
    `chatId` - The ID of the chat to initiate.  
    **Response:**  
    Success message upon successful chat initiation.

- **POST /chats/sendMessage/{chatId}**  
    Send a message to a specific chat.  
    **Path Parameter:**  
    `chatId` - The ID of the chat to send the message to.  
    **Request Body:**  
    `ChatMessage` - The message to send.  
    **Response:**  
    Success message upon successful message sending.

- **MessageMapping /app/sendMessage/{chatId}**  
    WebSocket endpoint for sending messages from the client.  
    **Path Parameter:**  
    `chatId` - The ID of the chat to send the message to.  
    **Request Body:**  
    `ChatMessage` - The message to send.  
    **Response:**  
    The message sent to the chat.


## Future Enhancements
- 🟢 **Password Reset (LOL)**  
    Provide password reset option incase of forgot password.

- 🟢 **User Presence Status**  
    Display user status (online/offline) in the chat interface.

- 🟡 **Message Read Receipts**  
    Introduce message read receipts, displaying an indicator when a message has been read by the recipient.

- 🟣 **Deployment to Cloud Infrastructure**  
    Prepare the app for deployment on cloud platforms like AWS, Google Cloud, or Heroku.

- 🤖 **AI Integration**  
    Add AI-based features, such as smart chatbots, automatic replies, or sentiment analysis.

## License
This project is yet to be licensed.