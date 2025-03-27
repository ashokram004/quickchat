import { createRoot } from 'react-dom/client'
import { createStore, applyMiddleware, compose } from 'redux';
import { thunk } from 'redux-thunk';
import { Provider } from 'react-redux';
import reducer from './Reducers/Reducer';
import './index.css'
import App from './App.jsx'
import Home from './Home.jsx';
import { BrowserRouter, Routes, Route } from 'react-router';
import { persistStore, persistReducer } from "redux-persist";
import { PersistGate } from "redux-persist/integration/react";
import storage from "redux-persist/lib/storage"; 

const persistConfig = {
  key: "root", 
  storage, 
  whitelist: ["isAuthenticated", "user", "chat"]
};

const persistedReducer = persistReducer(persistConfig, reducer);
const x = compose(applyMiddleware(thunk),  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());
const store = createStore(persistedReducer, x);
const persistor = persistStore(store);

createRoot(document.getElementById('root')).render(
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/chat" element={<App />} />
          </Routes>
        </BrowserRouter>
      </PersistGate>
    </Provider>
)
