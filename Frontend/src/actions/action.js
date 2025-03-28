import axios from "axios";

// export function setUserState(mobileNo) {
//     return async (dispatch) => {
//         try {
//             const response = await axios.get(`http://localhost:8080/users/${mobileNo}`);
//             dispatch({
//                 type: "SET_USER",
//                 user: response.data
//             });
//         } catch (error) {
//             console.error("Error fetching user data:", error);
//         }
//     };
// }

export function setUserState(user) {
    return {
        type: "SET_USER",
        user
    };
}

export function setChatState(chatId) {
    return async (dispatch) => {
        try {
            const response = await axios.get(`http://localhost:8080/chats/fetchMessages/${chatId}`);

            if (response.data.success) {
                dispatch({
                    type: "SET_CHAT",
                    chat: response.data.data // Extracting `data` from response
                });
            } else {
                console.error("Failed to fetch chat messages:", response.data.message);
                dispatch({
                    type: "SET_CHAT",
                    chat: {
                        chatMessages: []
                    }
                });
            }
        } catch (error) {
            console.error("Error fetching chat messages:", error);
            dispatch({
                type: "SET_CHAT",
                chat: {
                    chatMessages: []
                }
            });
        }
    };
}

export function sendMessage(chatId, chatMessage, chat) {
    return async (dispatch) => {
        try {
            console.log(chat.chatMessages)
            if (chat == null || chat == "" || chat.chatMessages.length == 0) {
                const initResponse = await dispatch(initiateChat(chatId));
                if (!initResponse || !initResponse.success) {
                    console.error("Failed to initiate chat:", initResponse?.message);
                    return; // Stop execution if chat initiation fails
                }
            }

            const response = await axios.post(`http://localhost:8080/chats/sendMessage/${chatId}`, chatMessage);
            console.log(response.data)
            if (response.data.success) {
                dispatch(setChatState(chatId)); // Refresh chat only if message sent successfully
            } else {
                console.error("Failed to send message:", response.data.message);
            }
        } catch (error) {
            console.error("Error sending message:", error);
        }
    };
}

export function updateUserState(user) {
    return async (dispatch) => {
        try {
            const response = await axios.put(`http://localhost:8080/users/update/${user.mobileNo}`, user);

            if (response.data.success) {
                dispatch(setUserState(response.data.data));
                toast.success("User updated successfully! 🎉", { position: "top-center" });
                console.log("User updated successfully:", response.data.message);
            } else {
                toast.error("User update failed!", { position: "top-center" });
                console.error("User update failed:", response.data.message);
            }
        } catch (error) {
            console.error("Error updating user:", error);
        }
    };
}


export function initiateChat(chatId) {
    return async (dispatch) => {
        try {
            const response = await axios.post(`http://localhost:8080/chats/initiateChat/${chatId}`);

            if (response.data.success) {
                console.log("Chat initiated successfully:", response.data.message);
                return response.data; // Return response for further handling
            } else {
                console.error("Failed to initiate chat:", response.data.message);
                return response.data;
            }
        } catch (error) {
            console.error("Error initiating chat:", error);
            return { success: false, message: "Error initiating chat" }; // Ensure a consistent return structure
        }
    };
}

export function addTempChatUser(chatUser) {
    return {
        type: "ADD_TEMP_CHAT_USER",
        chatUser
    };
}

export function logOut() {
    return {
        type: "LOGOUT",
    }
}