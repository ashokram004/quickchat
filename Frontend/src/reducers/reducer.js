const initialState = {
    isAuthenticated: false,
    user: {
        name: 'John Doe',
        mobileNo: '123',
        password: '123',
        theme: 'cyan',
        chatHistory: [
            {
                friendMobileNo: '456',
                chatId: '123.456'
            }
        ]
    },
    token: null,
    chat: {
        chatId: "123.456",
        chatMessages: [
            {
                sender: '123',
                message: 'hello reyyyy',
                timestamp: '2025-02-16T18:16:00.000+00:00'
            }
        ]
    }    
}


const reducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_AUTH':
            return {
                ...state,
                isAuthenticated: true,
                user: action.payload.user,
                token: action.payload.token, // Store token
            }

        case 'SET_USER':
            return {
                ...state,
                user: action.user,
                isAuthenticated: true
            }

        case "SET_CHAT":
            return {
                ...state,
                chat: action.chat || state.chat, // Use existing chat if null
            };

        case 'ADD_TEMP_CHAT_USER':
            return {
                ...state,
                user: {
                    ...state.user,
                    chatHistory: [
                        ...state.user.chatHistory,
                        {
                            friendMobileNo: action.chatUser.friendMobileNo,
                            chatId: action.chatUser.chatId
                        }
                    ]
                }
            }
        
        case 'ADD_MESSAGE':
            return {
                ...state,
                chat: {
                    ...state.chat,
                    chatMessages: [...state.chat.chatMessages, action.message],
                },
            };
        
        case 'LOGOUT':
            return {
                isAuthenticated: false,
                token: null,
                user: {
                    name: 'John Doe',
                    mobileNo: '123',
                    password: '123',
                    theme: 'cyan',
                    chatHistory: [
                        {
                            friendMobileNo: '456',
                            chatId: '123.456'
                        }
                    ]
                },
                chat: {
                    chatId: "123.456",
                    chatMessages: [
                        {
                            sender: '123',
                            message: 'hello reyyyy',
                            timestamp: '2025-02-16T18:16:00.000+00:00'
                        }
                    ]
                }
            }

        default:
            return state
    }
}

export default reducer