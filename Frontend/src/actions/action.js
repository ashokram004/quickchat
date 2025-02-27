import axios from "axios";

export function setUserState(mobileNo) {
    console.log("inside action")
    return async (dispatch) => {
        try {
            const response = await axios.get(`http://localhost:8080/users/${mobileNo}`);
            dispatch({
                type: "SET_USER",
                user: response.data
            });
        } catch (error) {
            console.error("Error fetching user data:", error);
        }
    };
}

export function setChatState(chatId) {
    return async (dispatch) => {
        try {
            const response = await axios.get(`http://localhost:8080/chats/fetchMessages/${chatId}`);
            dispatch({
                type: "SET_CHAT",
                chat: response.data
            });
        } catch (error) {
            console.error("Error fetching chat messages:", error);
        }
    };
}

export function sendMessage(chatId, chatMessage, chat) {
    return async (dispatch) => {
        try {
            if (chat == null) {
                await dispatch(initiateChat(chatId));
            }
            await axios.post(`http://localhost:8080/chats/sendMessage/${chatId}`, chatMessage);
            dispatch(setChatState(chatId)); // Refresh chat after sending message
        } catch (error) {
            console.error("Error sending message:", error);
        }
    };
}

export function updateUserState(user) {
    return async (dispatch) => {
        try {
            await axios.put(`http://localhost:8080/users/update/${user['mobileNo']}`, user);
            dispatch(setUserState(user['mobileNo']));
        } catch (error) {
            console.error("Error updating user:", error);
        }
    };
}

export function initiateChat(chatId) {
    return async (dispatch) => {
        try {
            await axios.post(`http://localhost:8080/chats/initiateChat/${chatId}`);
        } catch (error) {
            console.error("Error initiating chat", error);
        }
    };
}

export function addTempChatUser(chatUser) {
    return {
        type: "ADD_TEMP_CHAT_USER",
        chatUser
    };
}
