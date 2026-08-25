import './App.css'
import { useState, createContext, useReducer, useEffect } from 'react'
import { Routes, Route } from 'react-router-dom'

import HomePage from './pages/HomePage';
import JoinPage from './pages/JoinPage';
import LoginPage from './pages/LoginPage';
import New from './pages/New';
import TodoTypePage from './pages/TodotypePage';
import { getMembrInfoByToken, getToken } from './hooks/Token';

export const TodoStatusContext = createContext();
export const TodoDispatchContext = createContext();

function reducer(state, action) {
  switch (action.type) {
    case 'LOGIN':
      return {
          ...state,
          memberId : action.payload.memberId,
          memberName: action.payload.memberName,
          isLogin: true
      };
    case 'LOGOUT':
      return {
          ...state,
          memberId: "",
          memberName: "",
          isLogin: false
      };
    default:
      return state;
  }
}

function App() {
  const [info, dispatch] = useReducer(reducer, {
    memberId : "",
    memberName : "",
    isLogin : false,
  });

  useEffect(() => {
    
    if( getToken() ) {
      dispatch({
        type: 'LOGIN',
        payload: {
          memberId: getMembrInfoByToken('id'),
          memberName: getMembrInfoByToken('name')
        }
      })
    }
  }, []);

  return (
    <>
      <TodoStatusContext.Provider value={{info, dispatch}}>
        <TodoDispatchContext.Provider value={{}}>
          <Routes>
            <Route path="/" element={<HomePage/>}></Route>
            <Route path="/join" element={<JoinPage/>}></Route>
            <Route path="/login" element={<LoginPage/>}></Route>
            <Route path="/new" element={<New/>}></Route>
            <Route path="/todoType" element={<TodoTypePage/>}></Route>
          </Routes>
        </TodoDispatchContext.Provider>
      </TodoStatusContext.Provider>
    </>
  )
}

export default App
