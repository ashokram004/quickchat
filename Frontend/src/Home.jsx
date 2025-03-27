import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useNavigate } from "react-router";
import { setUserState } from "./actions/action";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";


export default function Home() {
  const [isLogin, setIsLogin] = useState(true);
  const [loginForm, setLoginForm] = useState({
    mobileNo: "",
    password: "",
  });
  const [registerForm, setRegisterForm] = useState({
    mobileNo: "",
    name: "",
    password: "",
  });
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const switchAuthMode = () => {
    setIsLogin((prev) => !prev);
  };

  const handleLogin = async () => {
    try {
        const response = await axios.post("http://localhost:8080/users/login", loginForm);

        if (response.data.success) {
            dispatch(setUserState(response.data.data)); // Store user data
            toast.success("Logged in successfully! 🎉", { position: "top-center" });
            setTimeout(() => navigate("/chat"), 1000);
        } else {
            toast.error(response.data.message || "Invalid credentials!", { position: "top-center" });
            console.error("Login failed:", response.data.message);
        }
    } catch (error) {
        toast.error("Login failed. Please try again.", { position: "top-center" });
        console.error("Login failed:", error);
    }
  };

  const handleRegister = async () => {
    try {
        const response = await axios.post("http://localhost:8080/users/register", registerForm);

        if (response.data.success) {
            toast.success("Registration successful! 🎉", { position: "top-center" });
            setTimeout(() => switchAuthMode(), 1000);
        } else {
            toast.error(response.data.message || "Registration failed!", { position: "top-center" });
            console.error("Registration failed:", response.data.message);
        }
    } catch (error) {
        toast.error("Registration failed. Please try again.", { position: "top-center" });
        console.error("Registration failed:", error);
    }
  };

  return (
    <div className="flex items-center justify-center h-screen bg-gradient-to-br from-[#141E30] to-[#243B55] relative overflow-hidden">

      <ToastContainer autoClose={2000} />
      {/* Moving Blobs */}
      <motion.div
        className="absolute top-[-50px] left-0 w-40 h-40 bg-blue-500 rounded-full filter blur-3xl opacity-40"
        animate={{ y: [0, 50, -50, 0] }}
        transition={{ duration: 10, repeat: Infinity, ease: "easeInOut" }}
      ></motion.div>
      <motion.div
        className="absolute bottom-[-50px] right-0 w-40 h-40 bg-green-500 rounded-full filter blur-3xl opacity-40"
        animate={{ y: [0, -50, 50, 0] }}
        transition={{ duration: 10, repeat: Infinity, ease: "easeInOut" }}
      ></motion.div>
      
      <motion.div
        className="w-[380px] h-[450px] p-8 bg-white/10 backdrop-blur-lg shadow-2xl rounded-2xl relative overflow-hidden border border-white/20 flex flex-col justify-center"
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5, ease: "easeInOut" }}
      >
        <AnimatePresence mode="wait">
          {isLogin ? (
            <motion.div
              key="login"
              initial={{ y: 50, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              exit={{ y: -50, opacity: 0 }}
              transition={{ duration: 0.5 }}
            >
              <h2 className="text-3xl font-bold text-white text-center mb-4">
                Welcome Back! 👋
              </h2>
              <p className="text-gray-300 text-sm text-center mb-6">
                Login to continue chatting on QuickChat.
              </p>

              <input
                type="tel"
                placeholder="Phone Number"
                className="w-full p-3 border-none rounded-md mb-3 bg-white/20 text-white placeholder-gray-300 focus:ring-2 focus:ring-blue-400"
                value={loginForm.mobileNo} onChange={e => setLoginForm({...loginForm, mobileNo: e.target.value})}
              />
              <input
                type="password"
                placeholder="Password"
                className="w-full p-3 border-none rounded-md mb-4 bg-white/20 text-white placeholder-gray-300 focus:ring-2 focus:ring-blue-400"
                value={loginForm.password} onChange={e => setLoginForm({...loginForm, password: e.target.value})}
              />
              <button className="w-full bg-blue-500 hover:bg-blue-600 text-white p-3 rounded-md shadow-lg transition" onClick={handleLogin}>
                Login
              </button>

              <p className="text-center text-sm mt-4 text-gray-300">
                Don't have an account?{" "}
                <button
                  className="text-blue-400 hover:underline"
                  onClick={switchAuthMode}
                >
                  Register
                </button>
              </p>
            </motion.div>
          ) : (
            <motion.div
              key="register"
              initial={{ y: -50, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              exit={{ y: 50, opacity: 0 }}
              transition={{ duration: 0.5 }}
            >
              <h2 className="text-3xl font-bold text-white text-center mb-4">
                Create an Account 🚀
              </h2>
              <p className="text-gray-300 text-sm text-center mb-6">
                Join QuickChat and start chatting instantly.
              </p>

              <input
                type="text"
                placeholder="Full Name"
                className="w-full p-3 border-none rounded-md mb-3 bg-white/20 text-white placeholder-gray-300 focus:ring-2 focus:ring-green-400"
                value={registerForm.name} onChange={e => setRegisterForm({...registerForm, name: e.target.value})}
              />
              <input
                type="tel"
                placeholder="Phone Number"
                className="w-full p-3 border-none rounded-md mb-3 bg-white/20 text-white placeholder-gray-300 focus:ring-2 focus:ring-green-400"
                value={registerForm.mobileNo} onChange={e => setRegisterForm({...registerForm, mobileNo: e.target.value})}
              />
              <input
                type="password"
                placeholder="Password"
                className="w-full p-3 border-none rounded-md mb-4 bg-white/20 text-white placeholder-gray-300 focus:ring-2 focus:ring-green-400"
                value={registerForm.password} onChange={e => setRegisterForm({...registerForm, password: e.target.value})}
              />
              <button className="w-full bg-green-500 hover:bg-green-600 text-white p-3 rounded-md shadow-lg transition" onClick={handleRegister}>
                Register
              </button>

              <p className="text-center text-sm mt-4 text-gray-300">
                Already have an account?{" "}
                <button
                  className="text-green-400 hover:underline"
                  onClick={switchAuthMode}
                >
                  Login
                </button>
              </p>
            </motion.div>
          )}
        </AnimatePresence>
      </motion.div>
    </div>
  );
}
