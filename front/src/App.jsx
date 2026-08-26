import './App.css'
import { createContext, useReducer } from 'react'
import AppContent from './AppContent';

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

  return (
    <>
      <TodoStatusContext.Provider value={{info, dispatch}}>
        <TodoDispatchContext.Provider value={{}}>
          <AppContent/>
        </TodoDispatchContext.Provider>
      </TodoStatusContext.Provider>
    </>
  )
}

export default App
