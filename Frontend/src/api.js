import axios from "axios";
import { store } from "./main"; // Import your Redux store
import { logOut } from "./actions/action";

// Create an Axios instance
const api = axios.create({
  baseURL: "http://localhost:8080", // Replace with your backend URL
});

// Add an interceptor to attach the token
api.interceptors.request.use(
  (config) => {
    const state = store.getState(); // Access the Redux state
    const token = state.token; // Get the token from the state
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle token expiration
api.interceptors.response.use(
    (response) => {
      return response; // Return the response if successful
    },
    (error) => {
      if (error.response) {
        // Token has expired or is invalid
        console.error("Request failed with error:", error.response.data);
  
        // Dispatch logout action to clear Redux state
        store.dispatch(logOut());

        // Optionally, show a message to the user
        alert("Our systems are under maintenance. Please log in after some time.");
      }
      return Promise.reject(error); // Reject the error for further handling
    }
  );

export default api;