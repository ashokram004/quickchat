/* eslint-disable no-unused-vars */
import { useState, useEffect, useRef } from "react";
import themesList from './themes'
import { useDispatch, useSelector } from "react-redux";
import { setChatState, setUserState, sendMessage, updateUserState, addTempChatUser, logOut} from './actions/action';
import { connectWebSocket, sendMessage as sendMessageSocket, stompClient } from './utils/websocket';
import { useNavigate } from "react-router";
import { debounce } from "lodash";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";
import "./app.css"

// Default human icon URL
const DEFAULT_PROFILE_PICTURE = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";

// Theme configuration
const themes = themesList;

export default function App() {
  
  const chat = useSelector((state) => state.chat);
  const user = useSelector((state) => state.user);
  const isAuthenticated = useSelector((state) => state.isAuthenticated);
  const [selectedUser, setSelectedUser] = useState(null);
  const [selectedTheme, setSelectedTheme] = useState(themes['cyan']);
  const [newMessage, setNewMessage] = useState("");
  const [isSettingsOpen, setIsSettingsOpen] = useState(false); // Toggle settings sidebar
  const [tempName, setTempName] = useState(user.name); // Temporary theme for editing
  const [tempTheme, setTempTheme] = useState(user.theme); // Temporary theme for editing
  const [profilePicture, setProfilePicture] = useState(DEFAULT_PROFILE_PICTURE); // Default profile picture
  const [isDropdownOpen, setIsDropdownOpen] = useState(false); // Toggle theme dropdown
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState([]); // Stores fetched mobileNos
  const [isSearching, setIsSearching] = useState(false);
  const messagesEndRef = useRef(null);


  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/");
    }
  }, [isAuthenticated])

  useEffect(() => {
    const unsubscribe = connectWebSocket(
      chat.chatId,
      (message) => {
          if (message.chatId === chat.chatId) {
              dispatch({ type: "ADD_MESSAGE", message });
          } else {
              toast.success("You recieved a new message from " + message.sender, { position: "top-center" });
              console.log("New notification recieved.");
          }
      }
    );

    // Cleanup the subscription when chat.chatId changes
    return () => {
        if (unsubscribe) unsubscribe();
    };
  }, [chat.chatId]);

  useEffect(() => {
    console.log(chat)
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [chat]);

  useEffect(() => {
    setSelectedTheme(themes[user.theme])
  }, [user]);

  const handleApplySettings = () => {
    dispatch(updateUserState({...user, name: tempName, theme: tempTheme}))
    setIsSettingsOpen(false); 
  };

  const handleSendMessage = () => {
    if(newMessage == ""){
      return;
    }
    const chatMessage = {sender: user.mobileNo, message: newMessage, timestamp: new Date().toISOString()}
    dispatch(sendMessage(selectedUser.chatId, chatMessage, chat));
    setNewMessage("")
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const handleSelectUser = (chatId) => {
    dispatch(setChatState(chatId));
  };

  const handleLogOut = () => {
    navigate("/");
    dispatch(logOut());
  }

  const addTemporaryUserChat = (friendMobileNo) => {
    const chatId = user['mobileNo']+"."+friendMobileNo
    setSelectedUser({chatId, friendMobileNo})
    dispatch(addTempChatUser({chatId, friendMobileNo}))
    setTimeout(() => {
      handleSelectUser(chatId);
    }, 0);
  }

  const handleProfilePictureChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setProfilePicture(reader.result); // Set the uploaded image as profile picture
      };
      reader.readAsDataURL(file);
    }
  };

  const fetchUsers = debounce(async (query) => {
    if (!query) {
        setSearchResults([]);
        return;
    }

    setIsSearching(true);
    try {
        const response = await axios.get(`http://localhost:8080/users/search?mobileNo=${query}`);

        if (response.data.success) {
            const data = response.data.data.filter(m => m !== user.mobileNo);
            setSearchResults(data);
        } else {
            console.error("User search failed:", response.data.message);
        }
    } catch (error) {
        console.error("Error fetching users:", error);
    }
    setIsSearching(false);
  }, 300);
  
  
  // Update searchQuery state and trigger API fetch
  const handleSearchChange = (e) => {
    const value = e.target.value;
    setSearchQuery(value);
    fetchUsers(value);
  };

  const handleSelectUserFromSearch = (friendMobileNo) => {
    // Check if user already exists in chat history
    const existingChat = user.chatHistory.find(chat => chat.friendMobileNo === friendMobileNo);
  
    if (existingChat) {
      // If already present, just select that user
      setSelectedUser(existingChat);
      handleSelectUser(existingChat.chatId);
    } else {
      // If new user, add temporary chat
      addTemporaryUserChat(friendMobileNo);
    }
  
    // Clear search results
    setSearchQuery("");
    setSearchResults([]);
  };
  

  return (
    <div className={`flex h-screen w-full items-center justify-center bg-gradient-to-br ${selectedTheme.gradient} animate-gradient`}>
      <ToastContainer autoClose={2000} />
      <div className="flex h-[90%] w-[90%] md:w-[80%] bg-gray-900/30 backdrop-blur-lg shadow-2xl rounded-2xl overflow-hidden border border-gray-800/50">
        {/* Sidebar */}
        <div className="w-1/3 bg-gray-900/20 p-4 border-r border-gray-800/50 flex flex-col relative">
          {/* Settings Sidebar */}
          {isSettingsOpen ? (
            <div className="absolute inset-0 bg-gray-900/20 backdrop-blur-lg p-4 flex flex-col gap-4 animate-jelly">
              {/* Close Button */}
              <button
                className="self-end text-gray-400 hover:text-gray-100 transition-all"
                onClick={() => setIsSettingsOpen(false)}
              >
                ✕
              </button>

              {/* Profile Picture */}
              <div className="flex justify-center">
                <label htmlFor="profile-picture" className="cursor-pointer">
                  <img
                    src={profilePicture}
                    alt="Profile"
                    className="rounded-full w-20 h-20 border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                  />
                  <input
                    type="file"
                    id="profile-picture"
                    accept="image/*"
                    className="hidden"
                    onChange={handleProfilePictureChange}
                  />
                </label>
              </div>

              {/* Name Edit */}
              <div>
                <label className="text-gray-100 text-sm font-semibold">Name:</label>
                <input
                  type="text"
                  value={tempName}
                  onChange={(e) => setTempName(e.target.value)}
                  className="w-full p-2 rounded-lg border border-gray-700/50 bg-gray-700/20 placeholder-gray-400 text-gray-100 focus:outline-none focus:ring-2 focus:ring-green-500/50"
                />
              </div>

              {/* Bio Edit */}
              <div>
                <label className="text-gray-100 text-sm font-semibold">Bio:</label>
                <textarea
                  value='QuickChat User'
                  className="w-full p-2 rounded-lg border border-gray-700/50 bg-gray-700/20 placeholder-gray-400 text-gray-100 focus:outline-none focus:ring-2 focus:ring-green-500/50"
                  rows={3}
                  readOnly
                />
              </div>

              {/* Theme Dropdown */}
              <div>
                <label className="text-gray-100 text-sm font-semibold">Theme:</label>
                <div className="relative">
                  <button
                    className="w-full p-2 rounded-lg border border-gray-700/50 bg-gray-700/20 text-gray-100 focus:outline-none focus:ring-2 focus:ring-green-500/50 text-left"
                    onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                  >
                    {tempTheme.charAt(0).toUpperCase() + tempTheme.slice(1)}
                  </button>
                  {isDropdownOpen && (
                    <div className="absolute w-full mt-1 rounded-lg border border-gray-700/50 bg-gray-800/50 backdrop-blur-lg">
                      {Object.keys(themes).map((themeKey) => (
                        <div
                          key={themeKey}
                          className={`p-2 cursor-pointer hover:bg-gray-700/50 text-gray-100 transition-all ${
                            tempTheme === themeKey ? "bg-gray-700/50" : ""
                          }`}
                          onClick={() => {
                            setTempTheme(themeKey);
                            setIsDropdownOpen(false);
                          }}
                        >
                          {themeKey.charAt(0).toUpperCase() + themeKey.slice(1)}
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>

              {/* Apply Button */}
              <button
                className="bg-gray-700/50 text-gray-100 px-4 py-2 rounded-lg hover:bg-gray-600/50 transition-all"
                onClick={handleApplySettings}
              >
                Apply
              </button>

              <div className="flex-grow"></div>

              {/* Logout Button */}
              <button
                className="bg-red-700/50 px-4 py-2 rounded-lg hover:bg-red-600/50 text-white transition-all"
                onClick={handleLogOut}
              >
                Logout
              </button>
            </div>
          ) : (
            <>
              {/* Profile & Settings */}
              <div className="flex items-center gap-3 mb-4">
                <img
                  src={profilePicture}
                  alt="Profile"
                  className="rounded-full w-12 h-12 cursor-pointer border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                  onClick={() => setIsSettingsOpen(true)}
                />
                <span className="font-semibold text-lg text-gray-100">{user.name}</span>
              </div>

              {/* Search Bar */}
              <input
                type="text"
                placeholder="Search by mobile number..."
                value={searchQuery}
                onChange={handleSearchChange}
                className="p-2 rounded-lg border border-gray-800/50 bg-gray-800/20 placeholder-gray-400 text-gray-100 focus:outline-none focus:ring-2 focus:ring-green-500/50 focus:border-transparent transition-all mb-4"
              />

              {/* Dynamic Search Results Dropdown */}
              {searchQuery && (
                <div className="absolute mt-1 w-full bg-gray-900/50 backdrop-blur-lg rounded-lg border border-gray-700/50">
                  {isSearching ? (
                    <div className="p-2 text-gray-300">Searching...</div>
                  ) : searchResults.length > 0 ? (
                    searchResults.map((mNo) => (
                      <div
                        key={mNo}
                        className="p-2 cursor-pointer hover:bg-gray-700/50 text-gray-100 transition-all"
                        onClick={() => handleSelectUserFromSearch(mNo)}
                      >
                        {mNo}
                      </div>
                    ))
                  ) : (
                    <div className="p-2 text-gray-300">No users found</div>
                  )}
                </div>
              )}
              {/* User List */}
              <div className="flex flex-col gap-2">
                {user.chatHistory.map((friend) => (
                  <div
                    key={friend.chatId}
                    className={`flex items-center p-2 cursor-pointer rounded-lg hover:bg-gray-800/30 transition-all ${
                      selectedUser?.chatId === friend.chatId ? "bg-gray-800/40" : ""
                    }`}
                    onClick={() => {setSelectedUser(friend); handleSelectUser(friend.chatId)}}
                  >
                    <img
                      src={DEFAULT_PROFILE_PICTURE}
                      alt={friend.friendMobileNo}
                      className="rounded-full w-10 h-10 mr-3 border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                    />
                    <span className="text-gray-100">{friend.friendMobileNo}</span>
                  </div>
                ))}
              </div>
            </>
          )}
        </div>

        {/* Chat Section */}
        <div className="flex-1 flex flex-col">
          {selectedUser ? (
            <>
              {/* Chat Header */}
              <div className="p-4 border-b border-gray-800/50 flex items-center gap-3 bg-gray-900/20">
                <img
                  src={DEFAULT_PROFILE_PICTURE}
                  alt={selectedUser.friendMobileNo}
                  className="rounded-full w-10 h-10 border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                />
                <span className="font-semibold text-lg text-gray-100">{selectedUser.friendMobileNo}</span>
              </div>

              {/* Chat Messages */}
              <div id="chat-messages" className="flex-1 p-4 overflow-y-auto hide-scrollbar">
                {chat.chatMessages?.map((msg, index) => (
                  <div
                    key={index}
                    className={`p-3 my-2 w-fit max-w-xs rounded-lg animate-message ${
                      msg.sender === user.mobileNo
                        ? `${selectedTheme.messageColor} text-gray-100 self-end ml-auto`
                        : "bg-gray-800/40 text-gray-100"
                    }`}
                  >
                    <span>{msg.message}</span>
                  </div>
                ))}
                {/* Invisible div to ensure scrolling to bottom */}
                <div ref={messagesEndRef} />
              </div>

              {/* Message Input */}
              <div className="p-4 border-t border-gray-800/50 flex gap-2 bg-gray-900/20">
                <input
                  type="text"
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  onKeyDown={handleKeyDown}
                  className={`flex-1 p-2 border border-gray-800/50 bg-gray-800/20 rounded-lg placeholder-gray-400 text-gray-100 focus:outline-none focus:ring-2 ${selectedTheme.focusRing} focus:border-transparent transition-all`}
                  placeholder="Type a message..."
                />
                <button
                  className={`${selectedTheme.buttonColor} text-gray-100 px-4 py-2 rounded-lg transition-all`}
                  onClick={handleSendMessage}
                >
                  Send
                </button>
              </div>
            </>
          ) : (
            <div className="flex items-center justify-center h-full text-gray-400">
              Select a user to start chatting
            </div>
          )}
        </div>
      </div>
    </div>
  );
}