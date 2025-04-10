import axios from "axios";
import { store } from "./main"; // Import your Redux store

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
      if (error.response && error.response.status === 401) {
        // Token has expired or is invalid
        console.error("Token expired or unauthorized:", error.response.data);
  
        // Dispatch logout action to clear Redux state
        store.dispatch(logout());

        // Optionally, show a message to the user
        alert("Your session has expired. Please log in again.");
      }
      return Promise.reject(error); // Reject the error for further handling
    }
  );

export default api;