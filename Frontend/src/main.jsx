import { createRoot } from 'react-dom/client'
import { createStore, applyMiddleware } from 'redux';
import { thunk } from 'redux-thunk';
import { Provider } from 'react-redux';
import reducer from './Reducers/Reducer';
import './index.css'
import App from './App.jsx'


const store = createStore(reducer, applyMiddleware(thunk));

createRoot(document.getElementById('root')).render(
    <Provider store={store}>
      <App />
    </Provider>
)
