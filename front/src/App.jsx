import './App.css'
import { useState, createContext, useReducer } from 'react'
import { Routes, Route } from 'react-router-dom'

import HomePage from './pages/HomePage';
import JoinPage from './pages/JoinPage';
import LoginPage from './pages/LoginPage';
import TodoPage from './pages/TodoPage';

export const TodoStatusContext = createContext();
export const TodoDispatchContext = createContext();

function reducer(state, action) {

  switch (action.type) {
    case 'LOGIN':
      return {
          ...state,
          memberName: action.payload.memberName,
          isLogin: true
      };
    case 'LOGOUT':
      return {
          ...state,
          memberName: "",
          isLogin: false
      };
    default:
      return state;
  }
}

function App() {
  const [count, setCount] = useState(0)
  const [info, dispatch] = useReducer(reducer, {
    memberName : "",
    isLogin : false
  });

  return (
    <>
      <TodoStatusContext.Provider value={{info, dispatch}}>
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
