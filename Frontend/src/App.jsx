import { useState } from "react";

const users = [
  { id: 1, name: "John Doe", avatar: "https://via.placeholder.com/50" },
  { id: 2, name: "Jane Smith", avatar: "https://via.placeholder.com/50" },
  { id: 3, name: "Alice Brown", avatar: "https://via.placeholder.com/50" },
];

const messages = {
  1: [
    { sender: "John Doe", text: "Hey! How's it going?" },
    { sender: "You", text: "All good! How about you?" },
  ],
  2: [
    { sender: "Jane Smith", text: "Hi there!" },
    { sender: "You", text: "Hey! What's up?" },
  ],
  3: [
    { sender: "Alice Brown", text: "Long time no see!" },
    { sender: "You", text: "Yeah! How have you been?" },
  ],
};

// Theme configuration
const themes = {
  green: {
    gradient: "from-emerald-900 via-teal-800 to-green-900",
    accent: "green",
    messageColor: "bg-green-600",
    buttonColor: "bg-green-600 hover:bg-green-700",
    focusRing: "focus:ring-green-500/50",
  },
  red: {
    gradient: "from-red-900 via-rose-800 to-pink-900",
    accent: "red",
    messageColor: "bg-red-600",
    buttonColor: "bg-red-600 hover:bg-red-700",
    focusRing: "focus:ring-red-500/50",
  },
  yellow: {
    gradient: "from-amber-900 via-orange-800 to-yellow-900",
    accent: "yellow",
    messageColor: "bg-yellow-600",
    buttonColor: "bg-yellow-600 hover:bg-yellow-700",
    focusRing: "focus:ring-yellow-500/50",
  },
  blue: {
    gradient: "from-indigo-900 via-blue-800 to-cyan-900",
    accent: "blue",
    messageColor: "bg-blue-600",
    buttonColor: "bg-blue-600 hover:bg-blue-700",
    focusRing: "focus:ring-blue-500/50",
  },
  orange: {
    gradient: "from-orange-900 via-amber-800 to-yellow-900",
    accent: "orange",
    messageColor: "bg-orange-600",
    buttonColor: "bg-orange-600 hover:bg-orange-700",
    focusRing: "focus:ring-orange-500/50",
  },
  pink: {
    gradient: "from-pink-900 via-rose-800 to-fuchsia-900",
    accent: "pink",
    messageColor: "bg-pink-600",
    buttonColor: "bg-pink-600 hover:bg-pink-700",
    focusRing: "focus:ring-pink-500/50",
  },
  gray: {
    gradient: "from-gray-900 via-gray-800 to-gray-700",
    accent: "gray",
    messageColor: "bg-gray-600",
    buttonColor: "bg-gray-600 hover:bg-gray-700",
    focusRing: "focus:ring-gray-500/50",
  },
  purple: {
    gradient: "from-purple-900 via-violet-800 to-indigo-900",
    accent: "purple",
    messageColor: "bg-purple-600",
    buttonColor: "bg-purple-600 hover:bg-purple-700",
    focusRing: "focus:ring-purple-500/50",
  },
  teal: {
    gradient: "from-teal-900 via-cyan-800 to-emerald-900",
    accent: "teal",
    messageColor: "bg-teal-600",
    buttonColor: "bg-teal-600 hover:bg-teal-700",
    focusRing: "focus:ring-teal-500/50",
  },
  lime: {
    gradient: "from-lime-900 via-green-800 to-emerald-900",
    accent: "lime",
    messageColor: "bg-lime-600",
    buttonColor: "bg-lime-600 hover:bg-lime-700",
    focusRing: "focus:ring-lime-500/50",
  },
  cyan: {
    gradient: "from-cyan-900 via-sky-800 to-blue-900",
    accent: "cyan",
    messageColor: "bg-cyan-600",
    buttonColor: "bg-cyan-600 hover:bg-cyan-700",
    focusRing: "focus:ring-cyan-500/50",
  },
};

export default function Home() {
  const [selectedUser, setSelectedUser] = useState(null);
  const [newMessage, setNewMessage] = useState("");
  const [theme, setTheme] = useState("green"); // Default theme is green
  const [isSettingsOpen, setIsSettingsOpen] = useState(false); // Toggle settings sidebar
  const [name, setName] = useState("Your Profile"); // Editable name
  const [bio, setBio] = useState("Hey there! I'm using this app."); // Editable bio
  const [tempName, setTempName] = useState(name); // Temporary name for editing
  const [tempBio, setTempBio] = useState(bio); // Temporary bio for editing
  const [tempTheme, setTempTheme] = useState(theme); // Temporary theme for editing

  const handleSendMessage = () => {
    if (!newMessage.trim() || !selectedUser) return;
    messages[selectedUser.id].push({ sender: "You", text: newMessage });
    setNewMessage("");
  };

  const handleApplySettings = () => {
    setName(tempName); // Save name
    setBio(tempBio); // Save bio
    setTheme(tempTheme); // Save theme
    setIsSettingsOpen(false); // Close settings sidebar
  };

  const selectedThemeConfig = themes[theme];

  return (
    <div className={`flex h-screen w-full items-center justify-center bg-gradient-to-br ${selectedThemeConfig.gradient} animate-gradient`}>
      <div className="flex h-[90%] w-[80%] bg-gray-900/30 backdrop-blur-lg shadow-2xl rounded-2xl overflow-hidden border border-gray-800/50">
        {/* Sidebar */}
        <div className="w-1/3 bg-gray-900/20 p-4 border-r border-gray-800/50 flex flex-col relative">
          {/* Settings Sidebar */}
          {isSettingsOpen ? (
            <div className="absolute inset-0 bg-gray-900/20 backdrop-blur-lg p-4 flex flex-col gap-4 animate-slide-in">
              {/* Close Button */}
              <button
                className="self-end text-gray-400 hover:text-gray-100 transition-all"
                onClick={() => setIsSettingsOpen(false)}
              >
                ✕
              </button>

              {/* Profile Picture */}
              <div className="flex justify-center">
                <img
                  src="https://via.placeholder.com/50"
                  alt="Profile"
                  className="rounded-full w-20 h-20 border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                />
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
                  value={tempBio}
                  onChange={(e) => setTempBio(e.target.value)}
                  className="w-full p-2 rounded-lg border border-gray-700/50 bg-gray-700/20 placeholder-gray-400 text-gray-100 focus:outline-none focus:ring-2 focus:ring-green-500/50"
                  rows={3}
                />
              </div>

              {/* Theme Dropdown */}
              <div>
                <label className="text-gray-100 text-sm font-semibold">Theme:</label>
                <select
                  value={tempTheme}
                  onChange={(e) => setTempTheme(e.target.value)}
                  className="w-full p-2 rounded-lg border border-gray-700/50 bg-gray-700/20 text-gray-100 focus:outline-none focus:ring-2 focus:ring-green-500/50 appearance-none bg-[url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktY2hldnJvbi1kb3duIiB2aWV3Qm94PSIwIDAgMTYgMTYiPjxwYXRoIGZpbGwtcnVsZT0iZXZlbm9kZCIgZD0iTTEuNjQ2IDQuNjQ2YS41LjUgMCAwIDEgLjcwOCAwTDggMTAuMjkzbDUuNjQ2LTUuNjQ3YS41LjUgMCAwIDEgLjcwOC43MDhsLTYgNmEuNS41IDAgMCAxLS43MDggMGwtNi02YS41LjUgMCAwIDEgMC0uNzA4eiIvPjwvc3ZnPg==')] bg-no-repeat bg-[right_0.5rem_center] bg-[length:1.5rem]"
                >
                  {Object.keys(themes).map((themeKey) => (
                    <option key={themeKey} value={themeKey}>
                      {themeKey.charAt(0).toUpperCase() + themeKey.slice(1)}
                    </option>
                  ))}
                </select>
              </div>

              {/* Apply Button */}
              <button
                className="bg-gray-700/50 text-gray-100 px-4 py-2 rounded-lg hover:bg-gray-600/50 transition-all"
                onClick={handleApplySettings}
              >
                Apply
              </button>
            </div>
          ) : (
            <>
              {/* Profile & Settings */}
              <div className="flex items-center gap-3 mb-4">
                <img
                  src="https://via.placeholder.com/50"
                  alt="Profile"
                  className="rounded-full w-12 h-12 cursor-pointer border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                  onClick={() => setIsSettingsOpen(true)}
                />
                <span className="font-semibold text-lg text-gray-100">{name}</span>
              </div>

              {/* Search Bar */}
              <input
                type="text"
                placeholder="Search..."
                className="p-2 rounded-lg border border-gray-800/50 bg-gray-800/20 placeholder-gray-400 text-gray-100 focus:outline-none focus:ring-2 focus:ring-green-500/50 focus:border-transparent transition-all mb-4"
              />

              {/* User List */}
              <div className="flex flex-col gap-2">
                {users.map((user) => (
                  <div
                    key={user.id}
                    className={`flex items-center p-2 cursor-pointer rounded-lg hover:bg-gray-800/30 transition-all ${
                      selectedUser?.id === user.id ? "bg-gray-800/40" : ""
                    }`}
                    onClick={() => setSelectedUser(user)}
                  >
                    <img
                      src={user.avatar}
                      alt={user.name}
                      className="rounded-full w-10 h-10 mr-3 border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                    />
                    <span className="text-gray-100">{user.name}</span>
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
                  src={selectedUser.avatar}
                  alt={selectedUser.name}
                  className="rounded-full w-10 h-10 border-2 border-gray-700/50 hover:border-gray-600/80 transition-all"
                />
                <span className="font-semibold text-lg text-gray-100">{selectedUser.name}</span>
              </div>

              {/* Chat Messages */}
              <div className="flex-1 p-4 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-800/50 scrollbar-track-transparent">
                {messages[selectedUser.id]?.map((msg, index) => (
                  <div
                    key={index}
                    className={`p-3 my-2 w-fit max-w-xs rounded-lg ${
                      msg.sender === "You"
                        ? `${selectedThemeConfig.messageColor} text-gray-100 self-end ml-auto`
                        : "bg-gray-800/40 text-gray-100"
                    }`}
                  >
                    <span>{msg.text}</span>
                  </div>
                ))}
              </div>

              {/* Message Input */}
              <div className="p-4 border-t border-gray-800/50 flex gap-2 bg-gray-900/20">
                <input
                  type="text"
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  className={`flex-1 p-2 border border-gray-800/50 bg-gray-800/20 rounded-lg placeholder-gray-400 text-gray-100 focus:outline-none focus:ring-2 ${selectedThemeConfig.focusRing} focus:border-transparent transition-all`}
                  placeholder="Type a message..."
                />
                <button
                  className={`${selectedThemeConfig.buttonColor} text-gray-100 px-4 py-2 rounded-lg transition-all`}
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