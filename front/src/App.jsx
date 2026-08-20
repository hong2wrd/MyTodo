import './App.css'
import { useState, createContext, useReducer } from 'react'
import { Routes, Route } from 'react-router-dom'

import HomePage from './pages/HomePage';
import JoinPage from './pages/JoinPage';
import LoginPage from './pages/LoginPage';
import TodoPage from './pages/TodoPage';

const TodoStatusContext = createContext();
const TodoDispatchContext = createContext();

function reducer(state, action) {

  let nextState = [action.data, ...state];

  return nextState;
}

function App() {
  const [count, setCount] = useState(0)
  const [data, dispatch] = useReducer(reducer, []);

  return (
    <>
      <TodoStatusContext.Provider value={data}>
        <TodoDispatchContext.Provider value={{}}>
          <Routes>
            <Route path="/" element={<HomePage/>}></Route>
            <Route path="/join" element={<JoinPage/>}></Route>
            <Route path="/login" element={<LoginPage/>}></Route>
            <Route path="/todo" element={<TodoPage/>}></Route>
          </Routes>
        </TodoDispatchContext.Provider>
      </TodoStatusContext.Provider>
    </>
  )
}

export default App
